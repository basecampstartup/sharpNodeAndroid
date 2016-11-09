package com.sharpnode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

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
     * Initialize the UI component.
     */
    public void initializeComponents() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                //...
                startActivity(new Intent(LandingPageActivity.this, SignInActivity.class));
                break;
            case R.id.btnSignUp:
                //...
                startActivity(new Intent(LandingPageActivity.this, SignUpActivity.class));
                break;
        }
    }
}
