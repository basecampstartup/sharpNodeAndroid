//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 09/11/2016
//===============================================================================
package com.sharpnode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sharpnode.utils.Utils;

public class LandingPageActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = getClass().getSimpleName();
    private Button btnLogin, btnSignUp;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        mContext = this;
        initializeComponents();
    }

    /**
     * Initialize the UI Components.
     */
    public void initializeComponents() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnLogin.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        btnSignUp.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView)findViewById(R.id.txtAppName)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView)findViewById(R.id.txtTagLine)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                //Navigate to Sign In screen
                startActivity(new Intent(LandingPageActivity.this, SignInActivity.class));
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.btnSignUp:
                //Navigate to Sign Up screen
                startActivity(new Intent(LandingPageActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        //Utils.exitFromApp(this);
    }
}
