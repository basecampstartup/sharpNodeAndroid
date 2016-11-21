//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 09/11/2016
//===============================================================================
package com.sharpnode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak{
    private final String TAG = getClass().getSimpleName();
    private Button btnSignUp;
    private Context mContext;

    EditText edtName,edtEmail,edtPhone,edtPassword;
    TextView txtAlreadyHaveAccount, tvTermsLabel;
    String strName,strEmail,strPhone,strPassword;
    private long mLastClickTime = 0;
    private ProgressDialog loader=null;
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
        loader.setMessage("Please wait...");
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
        edtName=(EditText)findViewById(R.id.edtName);
        edtEmail=(EditText)findViewById(R.id.edtEmailID);
        edtPhone=(EditText)findViewById(R.id.edtPhone);
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        tvTermsLabel = (TextView)findViewById(R.id.tvTermsLabel);
        txtAlreadyHaveAccount=(TextView)findViewById(R.id.txtAlreadyHaveAccount);
        txtAlreadyHaveAccount.setOnClickListener(this);

        ((TextView)findViewById(R.id.tvLable)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        btnSignUp.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtEmail.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtPhone.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtPassword.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        txtAlreadyHaveAccount.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvTermsLabel.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        chkTerms = (CheckBox)findViewById(R.id.chkTerms);
        /*chkTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    tvTermsLabel.setTextColor(getResources().getColor(R.color.colorAppWhite));
            }
        });*/
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

                if(!chkTerms.isChecked()) {
                    Toast.makeText(mContext, "Please accept Terms & Condition befor Sign-Up.", Toast.LENGTH_SHORT).show();
                    return;
                }

                loader.show();
                //Code for further process Signup.
                //Navigating to home screen
                //Call API Request after check internet connection
                strEmail=edtEmail.getText().toString().trim();
                strName=edtName.getText().toString().trim();
                strPassword=edtPassword.getText().toString().trim();
                strPhone=edtPhone.getText().toString().trim();

                new Communicator(mContext, APIUtils.CMD_SIGN_UP,
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
        if (password.isEmpty()/* || password.length() < 4 || password.length() > 10*/) {
            edtPassword.setError(getString(R.string.SignUpPasswordRequired));
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        return valid;
    }


    public HashMap<String, String> getSignUpRequestMap(String method, String email,String name,String password, String phone) {
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
        try{
            Logger.i(TAG, "Response: "+object);
            if (APIUtils.CMD_SIGN_UP.equalsIgnoreCase(name)) {
                if(parseLoginResponse(object).getResponseCode().equalsIgnoreCase(Commons.CODE_200)){
                    AppSPrefs.setAlreadyLoggedIn(true);
                    AppSPrefs.setString(Commons.ACCESS_TOKEN, parseLoginResponse(object).getAccessToken());
                    AppSPrefs.setString(Commons.USER_ID, parseLoginResponse(object).getUserId());
                    AppSPrefs.setString(Commons.NAME, parseLoginResponse(object).getName());
                    AppSPrefs.setString(Commons.PHONE, parseLoginResponse(object).getPhoneNo());
                    AppSPrefs.setString(Commons.PHOTO, parseLoginResponse(object).getPhoto());
                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
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
        loader.dismiss();
        Toast.makeText(mContext,"Signup response Success",Toast.LENGTH_LONG).show();
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
