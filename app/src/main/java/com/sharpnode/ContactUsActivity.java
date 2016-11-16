package com.sharpnode;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ContactUsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Context mContext;
    private TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        tvText=(TextView)findViewById(R.id.tvText);
        spanText();
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
}