package com.sharpnode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity {
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeComponents();
    }
    //Initialize the items
    public void initializeComponents()
    {
        btnSignUp=(Button)findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new OnComponentClick());
        btnSignUp.setOnClickListener(new OnComponentClick());
    }
    //this class to manage components click
    class OnComponentClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {

                case R.id.btnSignUp:
                    //code for button click here
                    Intent intentSignUp = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(intentSignUp);
                    break;
            }

        }
    }
}
