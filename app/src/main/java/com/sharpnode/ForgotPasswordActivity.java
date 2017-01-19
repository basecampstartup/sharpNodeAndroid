package com.sharpnode;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.BaseModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak{
    RelativeLayout rlRoot;
    private Toolbar mToolbar;
    private EditText edtEmailID, edtNewPassword, edtSecretKey;
    private Button btnSend;
    private TextView tvLabelOne, tvLabelTwo;
    private ProgressDialog loader = null;
    private Context mContext;
    private LinearLayout llayout1, llayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forget_password);

        mContext = this;

        rlRoot=(RelativeLayout)findViewById(R.id.rlRoot);
        Utils.setFont(rlRoot,SNApplication.APP_FONT_TYPEFACE);

        //Initialize toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.ForgotPassword));
        Utils.setTitleFontTypeface(mToolbar);

        llayout1 = (LinearLayout)findViewById(R.id.llayout1);
        llayout1.setVisibility(View.VISIBLE);
        llayout2 = (LinearLayout)findViewById(R.id.llayout2);
        llayout2.setVisibility(View.GONE);

        btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        edtEmailID = (EditText)findViewById(R.id.edtEmailID);
        edtEmailID.setText(AppSPrefs.getString(Commons.EMAIL));
        edtSecretKey = (EditText)findViewById(R.id.edtSecretKey);
        edtSecretKey.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtNewPassword = (EditText)findViewById(R.id.edtNewPassword);
        edtNewPassword.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        tvLabelOne = (TextView)findViewById(R.id.tvLabelOne);
        tvLabelTwo = (TextView) findViewById(R.id.tvLabelTwo);

        btnSend.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtEmailID.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvLabelOne.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvLabelTwo.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));

        edtEmailID.setText(AppSPrefs.getString(Commons.EMAIL));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSend:
                if(llayout1.isShown()){
                    String emailId = edtEmailID.getText().toString();
                    if(TextUtils.isEmpty(emailId))
                        return;

                    if (CheckNetwork.isInternetAvailable(mContext)) {
                        loader.show();
                        //Call API Request after check internet connection
                        new Communicator(mContext, null, APIUtils.CMD_RESET_PASSWORD,
                                getResetPasswordRequestMap(APIUtils.CMD_RESET_PASSWORD, emailId));
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else if(llayout2.isShown()){
                    String secretKey = edtSecretKey.getText().toString();
                    String newPassword = edtNewPassword.getText().toString();
                    String emailId = edtEmailID.getText().toString();

                    if(TextUtils.isEmpty(secretKey) || TextUtils.isEmpty(newPassword))
                        return;

                    if (CheckNetwork.isInternetAvailable(mContext)) {
                        loader.show();
                        //Call API Request after check internet connection
                        new Communicator(mContext, null, APIUtils.CMD_SET_NEW_PASSWORD,
                                getSetNewPasswordRequestMap(APIUtils.CMD_SET_NEW_PASSWORD, secretKey, newPassword, emailId));
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                break;
        }
    }

    /**
     * This method validate all the required fields.
     * @return
     */
    public boolean validate() {
        boolean valid = true;
        String newPassword = edtNewPassword.getText().toString();
        String key = edtSecretKey.getText().toString();


        return valid;
    }

    @Override
    public void onSuccess(String name, Object object) {
        loader.dismiss();

        try {
            if (APIUtils.CMD_RESET_PASSWORD.equalsIgnoreCase(name)) {
                BaseModel model = ResponseParser.parseResponse(object);
                if (model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    llayout1.setVisibility(View.GONE);
                    llayout2.setVisibility(View.VISIBLE);
                    btnSend.setText(getString(R.string.Done));
                    Utils.okAlertDialog(ForgotPasswordActivity.this, model.getResponseMsg());
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, model.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            } else if (APIUtils.CMD_SET_NEW_PASSWORD.equalsIgnoreCase(name)) {
                BaseModel model = ResponseParser.parseResponse(object);
                if (model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    finish();
                }
                Toast.makeText(ForgotPasswordActivity.this, model.getResponseMsg(), Toast.LENGTH_LONG).show();
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

    public HashMap<String, String> getSetNewPasswordRequestMap(String method, String secretKey, String newPassword, String emailId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.SECRET_KEY, secretKey);
        map.put(Commons.USER_ID, emailId);
        map.put(Commons.PASSWORD, newPassword);
        return map;
    }

    public HashMap<String, String> getResetPasswordRequestMap(String method, String emailId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.USER_ID, emailId);
        map.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        return map;
    }
}
