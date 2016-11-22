package com.sharpnode;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;


public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

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
        //Set Custom font to title.
        try {
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView titleText = (TextView) f.get(mToolbar);
            titleText.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {

        }

        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));

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
        this.finish();
    }
}
