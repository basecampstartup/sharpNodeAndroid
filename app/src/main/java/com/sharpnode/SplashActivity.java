//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 09/11/2016
//===============================================================================
package com.sharpnode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        goToNextScreen();
    }

    /**
     * Method  to Finish Splash screen after some time.
     */
    private void goToNextScreen() {
        final int splashTime = 1000;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                goToLandingPage();
            }
        }, splashTime);
    }

    /**
     * Method  to redirect to Landing Page.
     */
    private void goToLandingPage() {
        startActivity(new Intent(SplashActivity.this, LandingPageActivity.class));
        finish();
    }
}
