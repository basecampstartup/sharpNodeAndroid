package com.sharpnode;

import android.support.multidex.MultiDexApplication;

/**
 * Created by admin on 11/9/2016.
 */

public class SNApplication extends MultiDexApplication{
    public static SNApplication snApp;

    @Override
    public void onCreate() {
        super.onCreate();
        snApp = this;
    }
}
