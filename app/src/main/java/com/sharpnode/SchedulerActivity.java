package com.sharpnode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sharpnode.adapter.SchedulerAdapter;
import com.sharpnode.model.SchedulerModel;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SchedulerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    /*private ActionBar actionBar;*/
    Context mContext;
    private Toolbar mToolbar;
    LinearLayout containerAddNew;
    ArrayList<SchedulerModel> schedulerModels = new ArrayList<>();
    ListView schedularList;
    SchedulerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.SchedulerList));
        //Set Custom font to title.
        try {
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView titleText = (TextView) f.get(mToolbar);
            titleText.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
        containerAddNew = (LinearLayout) findViewById(R.id.containerAddNew);
        containerAddNew.setOnClickListener(this);
        schedularList = (ListView) findViewById(R.id.schedularList);
        schedularList.setOnItemLongClickListener(this);
        setScheduleListDummyData();
        adapter = new SchedulerAdapter(mContext, schedulerModels);
        schedularList.setAdapter(adapter);
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
        switch (v.getId()) {
            case R.id.containerAddNew:
                //Toast.makeText(mContext,"Add New Task",Toast.LENGTH_LONG).show();
                startActivity(new Intent(SchedulerActivity.this, AddScheduleTaskActivity.class));

                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        this.finish();
    }

    public void setScheduleListDummyData() {
        SchedulerModel model1 = new SchedulerModel();
        model1.setScheduleTaskName("AC at Night turn off");
        model1.setEveryDay(true);
        model1.setTurnOnOff(false);
        model1.setTime("3:00 am");
        model1.setWeekDays("");
        schedulerModels.add(model1);

        SchedulerModel model2 = new SchedulerModel();
        model2.setScheduleTaskName("Night Lamp turn on");
        model2.setEveryDay(true);
        model2.setTurnOnOff(true);
        model2.setTime("12:00 am");
        model2.setWeekDays("");
        schedulerModels.add(model2);

        SchedulerModel model3 = new SchedulerModel();
        model3.setScheduleTaskName("TV turn off when sleep");
        model3.setEveryDay(true);
        model3.setTurnOnOff(false);
        model3.setTime("12:00 am");
        model3.setWeekDays("");
        schedulerModels.add(model3);

        SchedulerModel model4 = new SchedulerModel();
        model4.setScheduleTaskName("Music Player turn on");
        model4.setEveryDay(false);
        model4.setWeekDays(" M, W, F");
        model4.setTurnOnOff(true);
        model4.setTime("7:00 am");
        schedulerModels.add(model4);

        SchedulerModel model5 = new SchedulerModel();
        model5.setScheduleTaskName("TV turn on");
        model5.setEveryDay(false);
        model5.setTurnOnOff(true);
        model5.setWeekDays("T, T, S");
        model5.setTime("3:00 am");
        schedulerModels.add(model5);

        SchedulerModel model6 = new SchedulerModel();
        model6.setScheduleTaskName("Night Lamp turn on");
        model6.setEveryDay(true);
        model6.setTurnOnOff(true);
        model6.setTime("12:00 am");
        model6.setWeekDays("");
        schedulerModels.add(model6);
    }





    /**
     * dialog to confirming user for Make admin and Remove admin
     *
     * @param title
     * @param message
     * @param position
     */
    public void confirmationAlertDialog(String title, String message, final int position) {
        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan("avenir-roman.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                SchedulerActivity.this);

        // set title
        alertDialogBuilder.setTitle(s);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.CommonYes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Add condition for self login admin functionality after change its own permission it should be logout.
                        dialog.cancel();
                        //check internet connection

                    }
                });
        // set dialog message
        alertDialogBuilder.setCancelable(false)
                .setNegativeButton(getResources().getString(R.string.CommonNo), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
        // change color of delete text
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(SchedulerActivity.this.getResources().getColor(R.color.colorPrimaryDark));
        //change for font style of button
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(SNApplication.APP_FONT_TYPEFACE);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        confirmationAlertDialog(getResources().getString(R.string.DeleteScheduleTitle),getResources().getString(R.string.DeleteScheduleMessage),position);
        return false;
    }
}
