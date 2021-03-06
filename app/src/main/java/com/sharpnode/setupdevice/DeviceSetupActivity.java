package com.sharpnode.setupdevice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.HomeDashboardActivity;
import com.sharpnode.R;
import com.sharpnode.SNApplication;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.ConfiguredDevices;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.particle.android.sdk.accountsetup.CreateAccountActivity;
import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleUser;
import io.particle.android.sdk.cloud.Responses;
import io.particle.android.sdk.cloud.SDKGlobals;
import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;
import io.particle.android.sdk.devicesetup.ui.DeviceSetupState;
import io.particle.android.sdk.devicesetup.ui.DiscoverDeviceActivity;
import io.particle.android.sdk.ui.BaseActivity;
import io.particle.android.sdk.ui.NextActivitySelector;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.SoftAPConfigRemover;
import io.particle.android.sdk.utils.ui.Toaster;

import static io.particle.android.sdk.utils.Py.truthy;

/**
 * Created by admin on 11/15/2016.
 */

public class DeviceSetupActivity extends BaseActivity implements View.OnClickListener, APIRequestCallbacak{

    public final static String EXTRA_SETUP_LAUNCHED_TIME = "io.particle.devicesetup.sharpnode.SETUP_LAUNCHED_TIME";
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private Button btnSetupDevice;
    private ParticleDeviceSetupLibrary.DeviceSetupCompleteReceiver receiver = null;
    private Toolbar mToolbar;
    private Async.AsyncApiWorker<ParticleCloud, Responses.ClaimCodeResponse> claimCodeWorker;
    private ParticleCloud sparkCloud;
    private SoftAPConfigRemover softAPConfigRemover;
    private ProgressDialog loader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setup);

        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.DeviceSetup));
        //Set Custom font to title.
        try {
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView titleText = (TextView) f.get(mToolbar);
            titleText.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        loader.setCancelable(false);

        initDeviceSetupProperty();

        btnSetupDevice = (Button) findViewById(R.id.btnSetupDevice);
        btnSetupDevice.setOnClickListener(this);
        ((TextView)findViewById(R.id.tvConfigureDeviceLbl)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView)findViewById(R.id.tvInstructionLbl)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView)findViewById(R.id.tvInstruction1)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView)findViewById(R.id.tvInstruction2)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView)findViewById(R.id.tvInstruction3)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        //((TextView)findViewById(R.id.tvInstruction4)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        //((TextView)findViewById(R.id.tvInstruction5)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        btnSetupDevice.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        receiver = new ParticleDeviceSetupLibrary.DeviceSetupCompleteReceiver() {

            @Override
            public void onSetupSuccess(@NonNull String configuredDeviceId) {
                Logger.i(TAG, "onSetupSuccess, deviceId=" + configuredDeviceId);
                addDevice(configuredDeviceId);
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
                if (CheckNetwork.isInternetAvailable(mContext)) {
                    loader.show();
                    onReadyButtonClicked();
                } else {
                    Logger.i(TAG, "Not connected to Internet.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        this.finish();
    }

    private void initDeviceSetupProperty(){
        btnSetupDevice = (Button) findViewById(R.id.btnSetupDevice);
        btnSetupDevice.setOnClickListener(this);

        sparkCloud = ParticleCloudSDK.getCloud();
        softAPConfigRemover = new SoftAPConfigRemover(this);
        softAPConfigRemover.removeAllSoftApConfigs();
        softAPConfigRemover.reenableWifiNetworks();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date expiryDate = calendar.getTime();

        sparkCloud.setAccessToken(AppSPrefs.getString(Commons.ACCESS_TOKEN), expiryDate);
        ParticleUser.fromNewCredentials(AppSPrefs.getString(Commons.USER_ID),
                AppSPrefs.getString(Commons.PASSWORD));

        //onLoginSuccess(sparkCloud);
    }

    private void onLoginSuccess(ParticleCloud cloud) {
        startActivity(NextActivitySelector.getNextActivityIntent(
                DeviceSetupActivity.this,
                cloud,
                SDKGlobals.getSensitiveDataStorage(),
                null));
    }

    private void onReadyButtonClicked() {
        // FIXME: check here that another of these tasks isn't already running
        DeviceSetupState.reset();
        final Context ctx = this;
        claimCodeWorker = Async.executeAsync(sparkCloud, new Async.ApiWork<ParticleCloud, Responses.ClaimCodeResponse>() {
            @Override
            public Responses.ClaimCodeResponse callApi(ParticleCloud sparkCloud) throws ParticleCloudException {
                Resources res = ctx.getResources();
                if (res.getBoolean(io.particle.android.sdk.devicesetup.R.bool.organization)) {
                    return sparkCloud.generateClaimCodeForOrg("sbi", "448");
                } else {
                    return sparkCloud.generateClaimCode();
                }
            }

            @Override
            public void onTaskFinished() {
                claimCodeWorker = null;
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
                loader.dismiss();
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
        loader.dismiss();
        //if (PermissionsFragment.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
        startActivity(new Intent(mContext, DiscoverDeviceActivity.class));
        // } else {
        //PermissionsFragment.get(this).ensurePermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        //}
    }

    private void addDevice(String configuredDeviceId){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            try{
                loader.show();
            }catch (Exception e){
                e.printStackTrace();
            }
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_ADD_DEVICE,
                    addDeviceRequestMap(APIUtils.CMD_ADD_DEVICE, configuredDeviceId, AppSPrefs.getString(Commons.ACCESS_TOKEN)));
        } else {
            finish();
            Logger.i(TAG, "Not connected to Internet.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * @param method
     * @param accessToken
     * @return
     */
    public HashMap<String, String> addDeviceRequestMap(String method, String configuredDeviceId, String accessToken) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.CONFIGURED_DEVICE_ID, configuredDeviceId);
        map.put(Commons.ACCESS_TOKEN, accessToken);
        return map;
    }

    @Override
    public void onSuccess(String name, Object object) {
        loader.dismiss();
        try {
            Logger.i(TAG, "Name: "+name+"Response: " + object);
            if (APIUtils.CMD_ADD_DEVICE.equalsIgnoreCase(name)) {
                ConfiguredDevices model = ResponseParser.parseGetDevicesResponse(object);
                if (model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    Intent intent = new Intent(HomeDashboardActivity.homeActivity, MyDevicesActivity.class);
                    HomeDashboardActivity.homeActivity.startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(mContext, model.getResponseMsg(),
                            Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        loader.dismiss();
        Toast.makeText(mContext, "Login response Failure", Toast.LENGTH_LONG).show();
    }

}
