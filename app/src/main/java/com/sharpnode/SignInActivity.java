//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 09/11/2016
//===============================================================================
package com.sharpnode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sharpnode.commons.Commons;
import com.sharpnode.utils.EmailSyntaxChecker;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = getClass().getSimpleName();
    private Button btnSignIn;
    private Context mContext;
    private EditText edtEmail,edtPassword;
    private TextView txtResetPassword,txtCreateAccount;
    private long mLastClickTime = 0;

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
        edtEmail=(EditText)findViewById(R.id.edtEmailID);
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        txtResetPassword=(TextView)findViewById(R.id.txtResetPassword);
        txtCreateAccount=(TextView)findViewById(R.id.txtCreateAccount);
        txtResetPassword.setOnClickListener(this);
        txtCreateAccount.setOnClickListener(this);

        btnSignIn.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtEmail.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtPassword.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        txtResetPassword.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        txtCreateAccount.setTypeface(SNApplication.APP_FONT_TYPEFACE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                //This will check if your click on button successively.
                if (SystemClock.elapsedRealtime() - mLastClickTime < Commons.THRESHOLD_TIME_POST_SCREEN) {
                    return;
                }
              /*  if (!validate()) {
                    return;
                }*/
                //Navigate to home screen
                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
               finish();
                break;
            case R.id.txtCreateAccount:
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
                break;
            case R.id.txtResetPassword:
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
                break;
        }
    }

    /**
     * This method validate all the required fields.
     * @return
     */
    public boolean validate() {
        boolean valid = true;
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (email.isEmpty() || !EmailSyntaxChecker.check(email)) {
            edtEmail.setError(getString(R.string.SignUpEmailRequired));
            valid = false;
        } else {
            edtEmail.setError(null);
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



