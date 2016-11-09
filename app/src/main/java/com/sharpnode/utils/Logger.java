package com.sharpnode.utils;

import android.util.Log;

/**
 * Created by admin on 11/9/2016.
 */

public class Logger {
    private static boolean loggerEnable = true;
    private static boolean errorLoggerEnable = true;

    public static void i(String tag, String msg){
        if(loggerEnable)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg){
        if(loggerEnable)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg){
        if(loggerEnable || errorLoggerEnable)
            Log.e(tag, msg);
    }
}
