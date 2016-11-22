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
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak {
    private final String TAG = getClass().getSimpleName();
    EditText edtName, edtEmail, edtPhone, edtPassword;
    TextView txtAlreadyHaveAccount, tvTermsLabel;
    String strName, strEmail, strPhone, strPassword;
    private Button btnSignUp;
    private Context mContext;
    private long mLastClickTime = 0;
    private ProgressDialog loader = null;
    private Toolbar mToolbar;
    private CheckBox chkTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mContext = this;

        //Initialize toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.SingUpSignUpBtn));

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
        this.finish();
    }

    /**
     * Initialize the UI Components.
     */
    public void initializeComponents() {
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmailID);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        tvTermsLabel = (TextView) findViewById(R.id.tvTermsLabel);
        txtAlreadyHaveAccount = (TextView) findViewById(R.id.txtAlreadyHaveAccount);
        txtAlreadyHaveAccount.setOnClickListener(this);

        ((TextView) findViewById(R.id.tvLable)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        btnSignUp.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtEmail.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtPhone.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtPassword.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        txtAlreadyHaveAccount.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvTermsLabel.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        chkTerms = (CheckBox) findViewById(R.id.chkTerms);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                //This will check if your click on button successively.
                if (SystemClock.elapsedRealtime() - mLastClickTime < Commons.THRESHOLD_TIME_POST_SCREEN) {
                    return;
                }
                if (!validate()) {
                    return;
                }

                if (!chkTerms.isChecked()) {
                    Toast.makeText(mContext, getString(R.string.TcnRequired), Toast.LENGTH_SHORT).show();
                    return;
                }

                //Code for further process Signup.
                //Navigating to home screen
                //Call API Request after check internet connection
                strEmail = edtEmail.getText().toString().trim();
                strName = edtName.getText().toString().trim();
                strPassword = edtPassword.getText().toString().trim();
                strPhone = edtPhone.getText().toString().trim();

                if (CheckNetwork.isInternetAvailable(mContext)) {
                    loader.show();
                    //Call API Request after check internet connection
                    new Communicator(mContext, APIUtils.CMD_SIGN_UP,
                            getSignUpRequestMap(APIUtils.CMD_SIGN_UP,
                                    strEmail, strName, strPassword, strPhone));
                } else {
                    Logger.i(TAG, "Not connected to Internet.");
                    Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.txtAlreadyHaveAccount:
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                this.finish();
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
        if (password.isEmpty()/* || password.length() < 4 || password.length() > 10*/) {
            edtPassword.setError(getString(R.string.SignUpPasswordRequired));
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        return valid;
    }


    public HashMap<String, String> getSignUpRequestMap(String method, String email, String name, String password, String phone) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.EMAIL, email);
        map.put(Commons.PHONE, phone);
        map.put(Commons.NAME, name);
        map.put(Commons.PASSWORD, password);
        map.put("deviceid", "xyz");
        return map;
    }

    @Override
    public void onSuccess(String name, Object object) {
        loader.dismiss();
        try {
            Logger.i(TAG, "Response: " + object);
            if (APIUtils.CMD_SIGN_UP.equalsIgnoreCase(name)) {
                if (ResponseParser.parseLoginResponse(object).getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    AppSPrefs.setAlreadyLoggedIn(true);
                    AccountModel model = ResponseParser.parseLoginResponse(object);
                    AppSPrefs.setString(Commons.ACCESS_TOKEN, model.getAccessToken());
                    AppSPrefs.setString(Commons.EMAIL, model.getUserId());
                    AppSPrefs.setString(Commons.USER_ID, model.getUserId());
                    AppSPrefs.setString(Commons.NAME, model.getName());
                    AppSPrefs.setString(Commons.PHONE, model.getPhoneNo());
                    AppSPrefs.setString(Commons.PHOTO, model.getPhoto());
                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(mContext, ResponseParser.parseLoginResponse(object).getResponseMsg(),
                            Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        loader.dismiss();
        Toast.makeText(mContext, "Signup response Success", Toast.LENGTH_LONG).show();
    }
}
