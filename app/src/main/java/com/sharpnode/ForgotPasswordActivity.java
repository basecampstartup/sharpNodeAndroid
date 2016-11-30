package com.sharpnode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharpnode.utils.Utils;

import java.lang.reflect.Field;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    RelativeLayout rlRoot;
    private Toolbar mToolbar;
    private EditText edtEmailID;
    private Button btnSend;
    private TextView tvLabelOne, tvLabelTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forget_password);
        rlRoot=(RelativeLayout)findViewById(R.id.rlRoot);
        Utils.setFont(rlRoot,SNApplication.APP_FONT_TYPEFACE);

        //Initialize toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.ForgotPassword));
        //Set Custom font to title.
        try {
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView titleText = (TextView) f.get(mToolbar);
            titleText.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }

        btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        edtEmailID = (EditText)findViewById(R.id.edtEmailID);

        tvLabelOne = (TextView)findViewById(R.id.tvLabelOne);
        tvLabelTwo = (TextView) findViewById(R.id.tvLabelTwo);

        btnSend.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtEmailID.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvLabelOne.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvLabelTwo.setTypeface(SNApplication.APP_FONT_TYPEFACE);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSend:
                break;
        }
    }
}
