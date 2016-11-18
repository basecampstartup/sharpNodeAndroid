package com.sharpnode.setupdevice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.sharpnode.R;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;

import java.util.Date;

import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;
import io.particle.android.sdk.utils.ui.Toaster;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setup);

        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
      /*  getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.LeftPanelDeviceManual));*/

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
                invokeDeviceSetupWithCustomIntentBuilder();
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
}
