package com.sharpnode;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.sharpnode.adapter.NotificationAdapter;
import com.sharpnode.model.NotificationModel;
import com.sharpnode.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by admin on 11/25/2016.
 */

public class NotificationActivity extends AppCompatActivity{
    private Context mContext;
    private Toolbar mToolbar;
    NotificationAdapter adapter;
    ListView notificationList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        mContext=this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.Notifications));
        Utils.setTitleFontTypeface(mToolbar);
        notificationList=(ListView)findViewById(R.id.notificationList);
        setScheduleListDummyData();
        adapter = new NotificationAdapter(mContext, NotificationModels);
        notificationList.setAdapter(adapter);
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
    ArrayList<NotificationModel> NotificationModels = new ArrayList<>();
    public void setScheduleListDummyData() {
        NotificationModel model1 = new NotificationModel();
        model1.setTitle("AC is  turn off just now");
        model1.setDateTime("just Now");
        NotificationModels.add(model1);

        NotificationModel model2 = new NotificationModel();
        model2.setTitle("AC is  turn off just now");
        model2.setDateTime("just Now");
        NotificationModels.add(model2);

        NotificationModel model3 = new NotificationModel();
        model3.setTitle("AC is  turn off just now");
        model3.setDateTime("just Now");
        NotificationModels.add(model3);

        NotificationModel model4 = new NotificationModel();
        model4.setTitle("AC is  turn off just now");
        model4.setDateTime("just Now");
        NotificationModels.add(model4);

        NotificationModel model5 = new NotificationModel();
        model5.setTitle("Your Insight Now Available");
        model5.setDateTime("just Now");
        NotificationModels.add(model5);

        NotificationModel model6 = new NotificationModel();
        model6.setTitle("TV Timer is stop");
        model6.setDateTime("15 minutes ago");
        NotificationModels.add(model6);
    }

}
