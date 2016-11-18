package com.sharpnode;

import android.graphics.Typeface;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sharpnode.context.ContextHelper;
import com.sharpnode.setupdevice.DeviceSetupActivity;
import com.sharpnode.utils.Utils;

import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;

/**
 * Created by admin on 11/9/2016.
 */

public class SNApplication extends MultiDexApplication {
    public static final String TAG = SNApplication.class.getSimpleName();
    public static SNApplication snApp;
    public static Typeface APP_FONT_TYPEFACE;
    private RequestQueue mRequestQueue;

    public static synchronized SNApplication getInstance() {
        return snApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        snApp = this;
        ContextHelper.setContext(this);

        APP_FONT_TYPEFACE = Utils.getTypeface(this, 1);
        ParticleDeviceSetupLibrary.init(this, DeviceSetupActivity.class);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
