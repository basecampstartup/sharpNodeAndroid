package com.sharpnode.commons;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sharpnode.context.ContextHelper;

/**
 * Created by admin on 11/15/2016.
 */

public class SharedPreferenceUtils {

    private static SharedPreferenceUtils sharedInstance;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static SharedPreferenceUtils getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new SharedPreferenceUtils();
            preferences = PreferenceManager.getDefaultSharedPreferences(ContextHelper.getContext());
            editor = preferences.edit();
        }
        return sharedInstance;
    }
}
