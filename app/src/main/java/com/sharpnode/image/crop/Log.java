//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Lib
// Original Date: 09 Nov 2015
// Changes By : Dheeraj Nagar
//===============================================================================
package com.sharpnode.image.crop;

class Log {

    private static final String TAG = "android-crop";

    public static void e(String msg) {
        android.util.Log.e(TAG, msg);
    }

    public static void e(String msg, Throwable e) {
        android.util.Log.e(TAG, msg, e);
    }

}
