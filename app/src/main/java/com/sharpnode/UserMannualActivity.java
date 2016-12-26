package com.sharpnode;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.utils.Utils;

import java.lang.reflect.Field;

public class UserMannualActivity extends AppCompatActivity {

    private ProgressDialog loader;
    private Toolbar mToolbar;
    private Context mContext;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mannual);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.LeftPanelUserManuals));
        Utils.setTitleFontTypeface(mToolbar);
        loader = new ProgressDialog(UserMannualActivity.this);
        loader.setCancelable(false);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new UserMannualActivity.LoadWebClient());
        webView.getSettings().setJavaScriptEnabled(true);
        // spanText();
        webView.loadUrl(APIUtils.USER_MANNUAL_URL);
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

    //This is for showing the loading until page load.
    public class LoadWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            try{
                loader.setMessage(getString(R.string.MessagePleaseWait));
                loader.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            // loading.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            if (loader != null) loader.dismiss();
        }
    }
}
