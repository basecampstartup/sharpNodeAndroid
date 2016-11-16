package com.sharpnode.context;

import android.content.Context;

/**
 * Created by admin on 11/15/2016.
 */

public class ContextHelper {

    private static Context context;

    /**
     * get context
     *
     * @return Context initialized
     * @throws Exception
     */
    public static Context getContext() {
        return context;
    }

    /**
     * set context
     *
     * @param context for initialization
     */
    public static void setContext(Context context) {
        ContextHelper.context = context;
    }
}
