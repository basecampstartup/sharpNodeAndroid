//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 09/11/2016
//===============================================================================
package com.sharpnode;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sharpnode.utils.EmailSyntaxChecker;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = getClass().getSimpleName();
    private Button btnSignUp;
    private Context mContext;

    EditText edtName,edtEmail,edtPhone,edtPassword;
    TextView txtAlreadyHaveAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mContext = this;
        initializeComponents();
    }

    /**
     * Initialize the UI Components.
     */
    public void initializeComponents() {
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        edtName=(EditText)findViewById(R.id.edtName);
        edtEmail=(EditText)findViewById(R.id.edtEmailID);
        edtPhone=(EditText)findViewById(R.id.edtPhone);
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        txtAlreadyHaveAccount=(TextView)findViewById(R.id.txtAlreadyHaveAccount);
        txtAlreadyHaveAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:

               /* if (!validate()) {
                    return;
                }*/
                //Code for further process Signup.
                //Navigating to home screen
                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                this.finish();
                break;
            case R.id.txtAlreadyHaveAccount:
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                this.finish();
                 break;
        }
    }

    /**
     * This method validate all the required fields.
     * @return
     */
    public boolean validate() {
        boolean valid = true;
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String phone = edtPhone.getText().toString();

        if (name.isEmpty()) {
            edtName.setError(getString(R.string.SignUpNameRequired));
            valid = false;
        } else {
            edtName.setError(null);
        }

        if (email.isEmpty() || !EmailSyntaxChecker.check(email)) {
            edtEmail.setError(getString(R.string.SignUpEmailRequired));
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        if (phone.isEmpty()) {
            edtPhone.setError("Phone number is required");
            valid = false;
        } else {
            edtName.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edtPassword.setError(getString(R.string.SignUpPasswordRequired));
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        return valid;
    }
}
