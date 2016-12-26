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
import android.widget.TextView;

import com.sharpnode.sprefs.AppSPrefs;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        ((TextView)findViewById(R.id.txtAppName)).setTypeface(SNApplication.APP_FONT_TYPEFACE);

        if (AppSPrefs.isAlreadyLoggedIn()) {
            goToHomePage();
        } else {
            goToNextScreen();
        }
    }

    /**
     * Method  to Finish Splash screen after some time.
     */
    private void goToNextScreen() {
        final int splashTime = 2000;
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
        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

    /**
     * Method  to redirect to Landing Page.
     */
    private void goToHomePage() {
        Intent intent = new Intent(SplashActivity.this, HomeDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        finish();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);*/
    }
}
