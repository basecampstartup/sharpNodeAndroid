package com.sharpnode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SignInActivity extends AppCompatActivity {
    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_layout);
        initializeComponents();

    }


    //Initialize the items
    public void initializeComponents()
    {
        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new OnComponentClick());
    }
    //this class to manage components click
    class OnComponentClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {

                case R.id.btnSignIn:
                    //code for button click here
                    Intent intentSignUp = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intentSignUp);
                    break;
            }

        }
    }

}
