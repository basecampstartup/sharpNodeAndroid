package com.sharpnode;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class LandingPageActivity extends AppCompatActivity {

    Button btnLogin,btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        initializeComponents();
    }

    //Initialize the items
    public void initializeComponents()
    {
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnSignUp=(Button)findViewById(R.id.btnSignUp);
        btnLogin.setOnClickListener(new OnComponentClick());
        btnSignUp.setOnClickListener(new OnComponentClick());
    }
    //this class to manage components click
    class OnComponentClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btnLogin:
                    //code for button click here
                    Intent intentLogin = new Intent(LandingPageActivity.this, SignInActivity.class);
                    startActivity(intentLogin);
                    break;
                case R.id.btnSignUp:
                    //code for button click here
                    Intent intentSignUp = new Intent(LandingPageActivity.this, SignUpActivity.class);
                    startActivity(intentSignUp);
                    break;
            }

        }
    }
}
