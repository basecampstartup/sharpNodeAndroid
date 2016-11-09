package com.sharpnode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = getClass().getSimpleName();
    private Button btnSignIn;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_layout);
        mContext = this;
        initializeComponents();

    }

    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                //Navigate to home screen
                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                break;
        }
    }
}
