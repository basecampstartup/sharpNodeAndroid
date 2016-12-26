package com.sharpnode;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sharpnode.utils.Utils;

/**
 * Created by admin on 12/16/2016.
 */

public class WebviewOfflineActivity extends AppCompatActivity{

    private Context mContext;
    private Toolbar mToolbar;
    private WebView webView;
    private String lastIP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_webview);
        mContext=this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        Utils.setTitleFontTypeface(mToolbar);

        lastIP = getIntent().getStringExtra("LAST_IP");
        webView = (WebView)findViewById(R.id.wvOffline);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://"+lastIP);
        webView.setWebViewClient(new WebViewController());
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

    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
