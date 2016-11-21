package com.sharpnode.setupdevice;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.sharpnode.R;
import com.sharpnode.commons.Commons;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleUser;
import io.particle.android.sdk.cloud.Responses;
import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;
import io.particle.android.sdk.devicesetup.ui.DeviceSetupState;
import io.particle.android.sdk.devicesetup.ui.DiscoverDeviceActivity;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.SoftAPConfigRemover;
import io.particle.android.sdk.utils.ui.Toaster;
import static io.particle.android.sdk.utils.Py.truthy;

/**
 * Created by admin on 11/15/2016.
 */

public class DeviceSetupActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_SETUP_LAUNCHED_TIME = "io.particle.devicesetup.sharpnode.SETUP_LAUNCHED_TIME";
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private Button btnSetupDevice;
    private ParticleDeviceSetupLibrary.DeviceSetupCompleteReceiver receiver = null;
    private Toolbar mToolbar;
    private Async.AsyncApiWorker<ParticleCloud, Responses.ClaimCodeResponse> claimCodeWorker;
    private ParticleCloud sparkCloud;
    private SoftAPConfigRemover softAPConfigRemover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setup);

        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.LeftPanelDeviceManual));

        sparkCloud = ParticleCloudSDK.getCloud();
        softAPConfigRemover = new SoftAPConfigRemover(this);
        softAPConfigRemover.removeAllSoftApConfigs();
        softAPConfigRemover.reenableWifiNetworks();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date expiryDate = calendar.getTime();
        sparkCloud.setAccessToken(AppSPrefs.getString(Commons.ACCESS_TOKEN), expiryDate);
        ParticleUser.fromNewCredentials(AppSPrefs.getString(Commons.USER_ID),
                AppSPrefs.getString(Commons.PASSWORD));;

        btnSetupDevice = (Button) findViewById(R.id.btnSetupDevice);
        btnSetupDevice.setOnClickListener(this);

        receiver = new ParticleDeviceSetupLibrary.DeviceSetupCompleteReceiver() {

            @Override
            public void onSetupSuccess(@NonNull String configuredDeviceId) {
                Logger.i(TAG, "onSetupSuccess, deviceId=" + configuredDeviceId);
                AppSPrefs.setDeviceId(configuredDeviceId);

                Toaster.s(mContext, "Hooray, you set up device " + configuredDeviceId);
                try {
                    if (receiver != null) {
                        receiver.unregister(mContext);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSetupFailure() {
                Logger.i(TAG, "onSetupFailure");
                Toaster.s(mContext, "Sorry, device setup failed.  (sad trombone)");
            }
        };

        receiver.register(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetupDevice:
                //invokeDeviceSetup();
                //invokeDeviceSetupWithCustomIntentBuilder();
                onReadyButtonClicked();
                break;
        }
    }

    public void invokeDeviceSetup() {
        ParticleDeviceSetupLibrary.startDeviceSetup(mContext);
    }

    private void invokeDeviceSetupWithCustomIntentBuilder() {
        final String setupLaunchedTime = new Date().toString();
        DeviceSetupCompleteIntentBuilder deviceSetupCompleteIntentBuilder = new DeviceSetupCompleteIntentBuilder(setupLaunchedTime);
        // Important: don't use an anonymous inner class to implement SetupCompleteIntentBuilder, otherwise you will cause a memory leak.
        ParticleDeviceSetupLibrary.startDeviceSetup(this, deviceSetupCompleteIntentBuilder);
    }

    private void onReadyButtonClicked() {
        // FIXME: check here that another of these tasks isn't already running
        DeviceSetupState.reset();
        //showProgress(true);
        final Context ctx = this;
        claimCodeWorker = Async.executeAsync(sparkCloud, new Async.ApiWork<ParticleCloud, Responses.ClaimCodeResponse>() {
            @Override
            public Responses.ClaimCodeResponse callApi(ParticleCloud sparkCloud) throws ParticleCloudException {
                Resources res = ctx.getResources();
                if (res.getBoolean(io.particle.android.sdk.devicesetup.R.bool.organization)) {
                    return sparkCloud.generateClaimCodeForOrg("sbi",
                            "448");
                } else {
                    return sparkCloud.generateClaimCode();
                }
            }

            @Override
            public void onTaskFinished() {
                claimCodeWorker = null;
                //showProgress(false);
            }

            @Override
            public void onSuccess(Responses.ClaimCodeResponse result) {
                Logger.i(TAG, "Claim code generated: " + result.claimCode);

                DeviceSetupState.claimCode = result.claimCode;
                if (truthy(result.deviceIds)) {
                    DeviceSetupState.claimedDeviceIds.addAll(Arrays.asList(result.deviceIds));
                }

                if (isFinishing()) {
                    return;
                }

                moveToDeviceDiscovery();
            }

            @Override
            public void onFailure(ParticleCloudException error) {
                Logger.i(TAG, "Generating claim code failed");
                ParticleCloudException.ResponseErrorData errorData = error.getResponseData();
                if (errorData != null && errorData.getHttpStatusCode() == 401) {

                    /*if (isFinishing()) {
                        sparkCloud.logOut();
                        startLoginActivity();
                        return;
                    }*/

                    String errorMsg = getString(io.particle.android.sdk.devicesetup.R.string.get_ready_must_be_logged_in_as_customer,
                            getString(io.particle.android.sdk.devicesetup.R.string.brand_name));
                    new AlertDialog.Builder(mContext)
                            .setTitle(io.particle.android.sdk.devicesetup.R.string.access_denied)
                            .setMessage(errorMsg)
                            .setPositiveButton(io.particle.android.sdk.devicesetup.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Logger.i(TAG, "Logging out user");
                                    /*sparkCloud.logOut();
                                    startLoginActivity();*/
                                    finish();
                                }
                            })
                            .show();

                } else {
                    if (isFinishing()) {
                        return;
                    }

                    // FIXME: we could just check the internet connection here ourselves...
                    String errorMsg = getString(io.particle.android.sdk.devicesetup.R.string.get_ready_could_not_connect_to_cloud);
                    if (error.getMessage() != null) {
                        errorMsg = errorMsg + "\n\n" + error.getMessage();
                    }
                    new AlertDialog.Builder(mContext)
                            .setTitle(io.particle.android.sdk.devicesetup.R.string.error)
                            .setMessage(errorMsg)
                            .setPositiveButton(io.particle.android.sdk.devicesetup.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
    }

    private void moveToDeviceDiscovery() {
        //if (PermissionsFragment.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            startActivity(new Intent(mContext, DiscoverDeviceActivity.class));
       // } else {
            //PermissionsFragment.get(this).ensurePermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        //}
    }
}
