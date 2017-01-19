package com.sharpnode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;


public class ContactUsActivity extends AppCompatActivity implements APIRequestCallbacak, View.OnClickListener {

    private String TAG = getClass().getSimpleName();
    private ProgressDialog loader;
    private Toolbar mToolbar;
    private Context mContext;
    private EditText edtName, edtEmailID, edtPhone, edtMessage;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us_layout);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.ContactUsLabel));
        Utils.setTitleFontTypeface(mToolbar);

        loader = new ProgressDialog(this);
        initializeComponents();
    }

    /**
     * Initialize the UI Components.
     */
    public void initializeComponents() {
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        edtName = (EditText) findViewById(R.id.edtName);
        edtEmailID = (EditText) findViewById(R.id.edtEmailID);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtMessage = (EditText) findViewById(R.id.edtMessage);

        btnSubmit.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtEmailID.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtPhone.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtMessage.setTypeface(SNApplication.APP_FONT_TYPEFACE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                String name = edtName.getText().toString();
                String email = edtEmailID.getText().toString();
                String phone = edtPhone.getText().toString();
                String message = edtMessage.getText().toString();

                if(Utils.validateContactUs(name, email, phone, message)){
                    callContactUs(name, email, phone, message);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
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

    private void callContactUs(String name, String email, String phone, String message){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_USER_CONTACT,
                    getRequestMap(APIUtils.CMD_USER_CONTACT, name, email, phone, message));
        } else {
            finish();
            Logger.i(TAG, "Not connected to Internet.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * @param method
     *
     * @return
     */
    public HashMap<String, String> getRequestMap(String method, String name, String email, String phone, String message) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        map.put(Commons.NAME, name);
        map.put(Commons.EMAIL, email);
        map.put(Commons.PHONE, phone);
        map.put(Commons.MESSAGE, message);
        return map;
    }

    @Override
    public void onSuccess(String name, Object object) {
        Utils.dismissLoader();
        Logger.i(TAG, name+", onSuccess, Response: " + object);
        try {
            BaseModel model = ResponseParser.parseResponse(object);
            if(APIUtils.CMD_USER_CONTACT.equalsIgnoreCase(name)){
                if(Commons.CODE_200.equalsIgnoreCase(model.getResponseCode())){
                    finish();
                }
                Utils.okAlertDialog(mContext, model.getResponseMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        Utils.dismissLoader();
        Logger.i(TAG, name+", onFailure, Response: " + object);
    }
}
