package com.sharpnode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        goToNextScreen();
    }

    /**
     * Method  to Finish Splash screen after some time
     */
    private void goToNextScreen() {
        final int splashTime = 1000;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                goToHomeScreen();
            }
        }, splashTime);
    }
    private void goToHomeScreen() {
        Intent intent = new Intent(SplashScreenActivity.this, LandingPageActivity.class);
        startActivity(intent);
        finish();

    }
}
