package com.sharpnode.sprefs;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sharpnode.commons.Commons;
import com.sharpnode.context.ContextHelper;

/**
 * Created by admin on 11/15/2016.
 */

public class AppSPrefs {

    private static SharedPreferences sPrefs;
    private static SharedPreferences.Editor editor;

    /**
     *
     * @return
     */
    private static SharedPreferences getSPrefsInstance() {
        if (sPrefs == null) {
            sPrefs = PreferenceManager.getDefaultSharedPreferences(ContextHelper.getContext());
        }
        return sPrefs;
    }

    public static void clearAppSPrefs(){
        editor = getSPrefsInstance().edit();
        editor.clear();
        editor.commit();
    }

    public static String getString(String key){
        return getSPrefsInstance().getString(key, "");
    }

    public static void setString(String key, String value){
        editor = getSPrefsInstance().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean isAlreadyLoggedIn(){
        return getSPrefsInstance().getBoolean(Commons.IS_ALREADY_LOGGED_IN, false);
    }

    public static void setAlreadyLoggedIn(boolean value){
        editor = getSPrefsInstance().edit();
        editor.putBoolean(Commons.IS_ALREADY_LOGGED_IN, value);
        editor.commit();
    }

    public static String getDeviceId(){
        return getSPrefsInstance().getString(Commons.CONFIGURED_DEVICE_ID, "");
    }

    public static void setDeviceId(String deviceId){
        editor = getSPrefsInstance().edit();
        editor.putString(Commons.CONFIGURED_DEVICE_ID, deviceId);
        editor.commit();
    }
}
