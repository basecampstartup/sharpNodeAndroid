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
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.AccountModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.EmailSyntaxChecker;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak {
    private final String TAG = getClass().getSimpleName();
    EditText edtEmail, edtPassword;
    TextView txtResetPassword, txtCreateAccount;
    String strEmail, strPassword;
    private Button btnSignIn;
    private Context mContext;
    private ProgressDialog loader = null;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_layout);
        mContext = this;

        //Initialize toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.SignInBtn));
        Utils.setTitleFontTypeface(mToolbar);
        initializeComponents();
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
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
        if(Utils.multipleTapDelayLONG())
            return;

        switch (v.getId()) {
            case R.id.btnSignIn:
                strEmail = edtEmail.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();
                if (!validate()) {
                    return;
                }

                if (CheckNetwork.isInternetAvailable(mContext)) {
                    loader.show();
                    //Call API Request after check internet connection
                    new Communicator(mContext, null, APIUtils.CMD_SIGN_IN,
                            getLoginRequestMap(APIUtils.CMD_SIGN_IN,
                                    strEmail, strPassword));
                } else {
                    Logger.i(TAG, "Not connected to Internet.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                //startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                //finish();
                break;
            case R.id.txtCreateAccount:
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                finish();
                break;
            case R.id.txtResetPassword:
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
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

        if (password.isEmpty()/* || password.length() < 4 || password.length() > 10*/) {
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
        loader.dismiss();
        try {
            Logger.i(TAG, "Response: " + object);
            if (APIUtils.CMD_SIGN_IN.equalsIgnoreCase(name)) {
                AccountModel model = ResponseParser.parseLoginResponse(object);
                if (model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    AppSPrefs.setAlreadyLoggedIn(true);
                    AppSPrefs.setString(Commons.ACCESS_TOKEN, model.getAccessToken());
                    AppSPrefs.setString(Commons.EMAIL, model.getUserId());
                    AppSPrefs.setString(Commons.USER_ID, model.getUserId());
                    AppSPrefs.setString(Commons.NAME, model.getName());
                    AppSPrefs.setString(Commons.PHONE, model.getPhoneNo());
                    AppSPrefs.setString(Commons.PHOTO, model.getPhoto());
                    startActivity(new Intent(SignInActivity.this, HomeDashboardActivity.class));
                    overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                } else {
                    Toast.makeText(mContext, model.getResponseMsg(),
                            Toast.LENGTH_LONG).show();
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        loader.dismiss();
        Toast.makeText(mContext, "Login response Failure", Toast.LENGTH_LONG).show();
    }
}



