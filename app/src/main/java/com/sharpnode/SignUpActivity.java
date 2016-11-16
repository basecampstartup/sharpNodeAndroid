//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 09/11/2016
//===============================================================================
package com.sharpnode;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.commons.Commons;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.utils.EmailSyntaxChecker;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak{
    private final String TAG = getClass().getSimpleName();
    private Button btnSignUp;
    private Context mContext;

    EditText edtName,edtEmail,edtPhone,edtPassword;
    TextView txtAlreadyHaveAccount;
    String strName,strEmail,strPhone,strPassword;
    private long mLastClickTime = 0;
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
        edtName=(EditText)findViewById(R.id.edtName);
        edtEmail=(EditText)findViewById(R.id.edtEmailID);
        edtPhone=(EditText)findViewById(R.id.edtPhone);
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        txtAlreadyHaveAccount=(TextView)findViewById(R.id.txtAlreadyHaveAccount);
        txtAlreadyHaveAccount.setOnClickListener(this);

        btnSignUp.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtEmail.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtPhone.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtPassword.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        txtAlreadyHaveAccount.setTypeface(SNApplication.APP_FONT_TYPEFACE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                //This will check if your click on button successively.
                if (SystemClock.elapsedRealtime() - mLastClickTime < Commons.THRESHOLD_TIME_POST_SCREEN) {
                    return;
                }
               /* if (!validate()) {
                    return;
                }*/
                //Code for further process Signup.
                //Navigating to home screen
                //Call API Request after check internet connection
                strEmail=edtEmail.getText().toString().trim();
                strName=edtName.getText().toString().trim();
                strPassword=edtPassword.getText().toString().trim();
                strPhone=edtPhone.getText().toString().trim();

                new Communicator(mContext, APIUtils.CMD_SIGN_IN,
                        getSignUpRequestMap(APIUtils.CMD_SIGN_UP,
                                strEmail, strName, strPassword, strPhone));

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


    public HashMap<String, String> getSignUpRequestMap(String method, String email,String name,String password, String phone) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.CMD, method);
        map.put(Commons.EMAIL, email);
        map.put(Commons.PHONE, phone);
        map.put(Commons.NAME, name);
        map.put(Commons.PASSWORD, password);
        return map;
    }

    @Override
    public void onSuccess(String name, Object object) {
        // Toast.makeText(mContext,"SignUp response Success",Toast.LENGTH_LONG).show();
        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
        this.finish();
    }

    @Override
    public void onFailure(String name, Object object) {
        //Toast.makeText(mContext,"Signup response Success",Toast.LENGTH_LONG).show();
    }
}
