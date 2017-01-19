package com.sharpnode;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by admin on 11/25/2016.
 */

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener,APIRequestCallbacak{
    private Context mContext;
    private Toolbar mToolbar;
    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnSubmit;
    private ProgressDialog loader = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.ChangePassword));
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        Utils.setTitleFontTypeface(mToolbar);
        initializeComponents();
    }

    /**
     * Initialize the UI Components.
     */
    public void initializeComponents() {
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        edtOldPassword=(EditText)findViewById(R.id.edtOldPassword);
        edtNewPassword=(EditText)findViewById(R.id.edtNewPassword);
        edtConfirmPassword=(EditText)findViewById(R.id.edtConfirmPassword);

        btnSubmit.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtOldPassword.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtNewPassword.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtConfirmPassword.setTypeface(SNApplication.APP_FONT_TYPEFACE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSubmit:
                String oldPassword=edtOldPassword.getText().toString().trim();
                String newPassword=edtNewPassword.getText().toString().trim();
                String confPassword=edtConfirmPassword.getText().toString().trim();

                if (!validate()) {
                    return;
                }
                if(!newPassword.equalsIgnoreCase(confPassword))
                {
                    Toast.makeText(mContext,getResources().getString(R.string.NewPassworNotMatch),Toast.LENGTH_LONG).show();
                    return;
                }


                if (CheckNetwork.isInternetAvailable(mContext)) {
                    loader.show();
                    //Call API Request after check internet connection
                    new Communicator(mContext, null, APIUtils.CMD_UPDATE_PASSWORD,
                            getSetPasswordRequestMap(APIUtils.CMD_UPDATE_PASSWORD,oldPassword, newPassword));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                break;
        }
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
     * This method validate all the required fields.
     * @return
     */
    public boolean validate() {
        boolean valid = true;
        String oldPassword = edtOldPassword.getText().toString();
        String newPassword = edtNewPassword.getText().toString();
        String confirmNewPassword = edtConfirmPassword.getText().toString();

        if (oldPassword.isEmpty()) {
            edtOldPassword.setError(getString(R.string.OldPasswordRequired));
            valid = false;
        } else {
            edtOldPassword.setError(null);
        }
        if (newPassword.isEmpty()) {
            edtNewPassword.setError(getString(R.string.NewPasswordRequired));
            valid = false;
        } else {
            edtNewPassword.setError(null);
        }

        if (confirmNewPassword.isEmpty()) {
            edtConfirmPassword.setError(getString(R.string.ConfirmPasswordRequired));
            valid = false;
        } else {
            edtConfirmPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onSuccess(String name, Object object) {
        loader.dismiss();

        try {
            if (APIUtils.CMD_UPDATE_PASSWORD.equalsIgnoreCase(name)) {
                if (ResponseParser.parseUpdatePasswordResponse(object).getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    String message=ResponseParser.parseUpdatePasswordResponse(object).getResponseMsg();
                    Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(mContext, ResponseParser.parseLoginResponse(object).getResponseMsg(), Toast.LENGTH_LONG).show();
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
        Toast.makeText(mContext, "Password change response Failure", Toast.LENGTH_LONG).show();
    }

    public HashMap<String, String> getSetPasswordRequestMap(String method, String oldPassword, String newPassword) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.OLD_PASSWORD, oldPassword);
        map.put(Commons.NEW_PASSWORD, newPassword);
        map.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        return map;
    }
}
