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

    public static String getDeviceId(){
        return getSPrefsInstance().getString(Commons.CONFIGURED_DEVICE_ID, "");
    }

    public static void setDeviceId(String deviceId){
        editor = getSPrefsInstance().edit();
        editor.putString(Commons.CONFIGURED_DEVICE_ID, deviceId);
    }
}
