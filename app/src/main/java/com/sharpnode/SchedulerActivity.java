package com.sharpnode;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class SchedulerActivity extends AppCompatActivity implements View.OnClickListener{
    /*private ActionBar actionBar;*/
    Context mContext;
    private Toolbar mToolbar;
    LinearLayout containerAddNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        mContext=this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.SchedulerList));
        containerAddNew=(LinearLayout)findViewById(R.id.containerAddNew);
        containerAddNew.setOnClickListener(this);
        /*actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.SchedulerList));
        actionBar.setDisplayHomeAsUpEnabled(true);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.containerAddNew:
                //Toast.makeText(mContext,"Add New Task",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        this.finish();
    }
}
