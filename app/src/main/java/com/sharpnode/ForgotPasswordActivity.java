package com.sharpnode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sharpnode.utils.Utils;

public class ForgotPasswordActivity extends AppCompatActivity {
    RelativeLayout rlRoot;
    LinearLayout layout_fields_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forget_password);
        rlRoot=(RelativeLayout)findViewById(R.id.rlRoot);
        Utils.setFont(rlRoot,SNApplication.APP_FONT_TYPEFACE);
    }
}
