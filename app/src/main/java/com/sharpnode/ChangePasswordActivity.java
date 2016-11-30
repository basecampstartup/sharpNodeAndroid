package com.sharpnode;

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

import com.sharpnode.commons.Commons;
import com.sharpnode.sprefs.AppSPrefs;

import java.lang.reflect.Field;

/**
 * Created by admin on 11/25/2016.
 */

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private Context mContext;
    private Toolbar mToolbar;
    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnSubmit;

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

        //Set Custom font to title.
        try {
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView titleText = (TextView) f.get(mToolbar);
            titleText.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {

        }
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
        //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        this.finish();
    }
}
