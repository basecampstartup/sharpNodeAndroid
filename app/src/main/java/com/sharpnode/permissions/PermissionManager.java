//===============================================================================
// © 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Jogendra Gouda
// Original Date: 25 April 2016
//===============================================================================
package com.sharpnode.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Class to manage permissions of marshmallow of Android
 */
public class PermissionManager {

    public static final int PERMISSION_REQUEST_CODE_LOCATION = 122;
    public static final int PERMISSION_REQUEST_CODE_PHONE_CALL = 123;
    public static final int PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE = 124;
    public static final int PERMISSION_REQUEST_CODE_READ_PHONE_STATE = 124;
    public static final int PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 125;
    public static final int PERMISSION_REQUEST_CODE = 126;
    public static final int PERMISSION_REQUEST_CODE_FOR_DOWNLOAD_APP = 127;

    /**
     * Method to check current version is Marshmallow or not
     */
    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * @param permission should be like "Manifest.permission.WRITE_EXTERNAL_STORAGE"
     */
    public static boolean checkPermission(Context activity, String permission) {

        if (!isMarshmallow()) {
            return true;
        }
        // Here, thisActivity is the current activity
        else return ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * permission should be like "Manifest.permission.WRITE_EXTERNAL_STORAGE"
     *
     * @param permission should be like "Manifest.permission.WRITE_EXTERNAL_STORAGE"
     */
    public static void requestPermission(Activity activity, String permission, int PERMISSION_REQUEST_CODE) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            //Request for location
            if (permission.equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(activity, new String[]{permission, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE);
            }
        } else {
            //Request for location
            if (permission.equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(activity, new String[]{permission, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE);
            }
        }
    }

}
