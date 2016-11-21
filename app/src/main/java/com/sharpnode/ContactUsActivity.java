package com.sharpnode;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sharpnode.servercommunication.APIUtils;


public class ContactUsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Context mContext;
    private TextView tvText;
    private WebView webView;


    ProgressDialog pd ;

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
       /* pd = new ProgressDialog(ContactUsActivity.this);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new LoadWebClient());
        webView.getSettings().setJavaScriptEnabled(true);
       // spanText();
        webView.loadUrl(APIUtils.CONTACT_US_URL);*/

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

    public void spanText()
    {
        String var="here";
        final SpannableStringBuilder sb = new SpannableStringBuilder("your text here");

       // Span to make text bold
       //  final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

       // Set the text color for first 4 characters
       //sb.setSpan(fcs, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
       // make them also bold
       // sb.setSpan(bss, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
               // Toast.makeText(mContext,"click",Toast.LENGTH_LONG).show();
            }
        },0,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //vText.setMovementMethod(LinkMovementMethod.getInstance());
        tvText.setText(sb);
    }

    //This is for showing the loading until page load.
    public class LoadWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            pd.setMessage("loading");
            pd.show();

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
              if(pd!=null)pd.dismiss();
            //loading.setVisibility(View.GONE);
        }
    }

}
