package com.sharpnode.setupdevice;

import android.content.Context;
import android.content.Intent;
import io.particle.android.sdk.devicesetup.SetupCompleteIntentBuilder;
import io.particle.android.sdk.devicesetup.SetupResult;

/**
 * Created by admin on 11/17/2016.
 */

public class DeviceSetupCompleteIntentBuilder implements SetupCompleteIntentBuilder {
    private final String setupLaunchedTime;

    public DeviceSetupCompleteIntentBuilder(String setupLaunchedTime) {
        this.setupLaunchedTime = setupLaunchedTime;
    }

    @Override
    public Intent buildIntent(Context ctx, SetupResult result) {
        Intent intent = new Intent(ctx, DeviceSetupActivity.class);
        intent.putExtra(DeviceSetupActivity.EXTRA_SETUP_LAUNCHED_TIME, setupLaunchedTime);

        return intent;
    }
}
