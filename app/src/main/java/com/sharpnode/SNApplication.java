package com.sharpnode;

import android.graphics.Typeface;
import android.support.multidex.MultiDexApplication;

import com.sharpnode.utils.Utils;

/**
 * Created by admin on 11/9/2016.
 */

public class SNApplication extends MultiDexApplication{
    public static SNApplication snApp;

    public static Typeface APP_FONT_TYPEFACE;

    @Override
    public void onCreate() {
        super.onCreate();
        snApp = this;
        APP_FONT_TYPEFACE = Utils.getTypeface(this, 1);
    }
}
