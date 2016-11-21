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
import android.widget.Toast;

import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.AccountModel;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.EmailSyntaxChecker;
import com.sharpnode.utils.Logger;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak {
    private final String TAG = getClass().getSimpleName();
    EditText edtEmail, edtPassword;
    TextView txtResetPassword, txtCreateAccount;
    String strEmail, strPassword;
    private Button btnSignIn;
    private Context mContext;
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
        edtEmail = (EditText) findViewById(R.id.edtEmailID);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        txtResetPassword = (TextView) findViewById(R.id.txtResetPassword);
        txtCreateAccount = (TextView) findViewById(R.id.txtCreateAccount);
        txtResetPassword.setOnClickListener(this);
        txtCreateAccount.setOnClickListener(this);

        ((TextView) findViewById(R.id.tvLable)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
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
                strEmail = edtEmail.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();

                //Call API Request after check internet connection
                new Communicator(mContext, APIUtils.CMD_SIGN_IN,
                        getLoginRequestMap(APIUtils.CMD_SIGN_IN,
                                strEmail, strPassword));

                //startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                //finish();
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
     *
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

    /**
     * @param method
     * @param userId
     * @param password
     * @return
     */
    public HashMap<String, String> getLoginRequestMap(String method, String userId, String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.EMAIL, userId);
        map.put(Commons.PASSWORD, password);
        return map;
    }

    @Override
    public void onSuccess(String name, Object object) {
        try{
            Logger.i(TAG, "Response: "+object);
            if (APIUtils.CMD_SIGN_IN.equalsIgnoreCase(name)) {
                if(parseLoginResponse(object).getResponseCode().equalsIgnoreCase(Commons.CODE_200)){
                    AppSPrefs.setAlreadyLoggedIn(true);
                    AppSPrefs.setString(Commons.ACCESS_TOKEN, parseLoginResponse(object).getAccessToken());
                    AppSPrefs.setString(Commons.USER_ID, parseLoginResponse(object).getUserId());
                    AppSPrefs.setString(Commons.NAME, parseLoginResponse(object).getName());
                    AppSPrefs.setString(Commons.PHONE, parseLoginResponse(object).getPhoneNo());
                    AppSPrefs.setString(Commons.PHOTO, parseLoginResponse(object).getPhoto());
                    startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(mContext, parseLoginResponse(object).getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        Toast.makeText(mContext, "Login response Failure", Toast.LENGTH_LONG).show();
    }

    private AccountModel parseLoginResponse(Object object){
        JSONObject jsonObj = null;
        AccountModel model = new AccountModel();
        try {
            jsonObj = new JSONObject(object.toString());
            model.setResponseCode(jsonObj.optString(Commons.RESPONSE_CODE));
            model.setResponseMsg(jsonObj.optString(Commons.TXT));
            model.setUserId(jsonObj.optString(Commons.USER_ID));
            model.setPhoneNo(jsonObj.optString(Commons.PHONE));
            model.setPhoto(jsonObj.optString(Commons.PHOTO));
            model.setAccessToken(jsonObj.optString(Commons.ACCESS_TOKEN));
            model.setName(jsonObj.optString(Commons.NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }
}



