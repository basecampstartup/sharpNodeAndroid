package com.sharpnode;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.sharpnode.adapter.SchedulerAdapter;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.ConfiguredDevices;
import com.sharpnode.model.SchedulerModel;
import com.sharpnode.model.TaskModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class SchedulerActivity extends AppCompatActivity implements APIRequestCallbacak, View.OnClickListener{
    private String TAG = getClass().getSimpleName();
    Context mContext;
    private Toolbar mToolbar;
    LinearLayout containerAddNew;
    ArrayList<TaskModel> taskList = new ArrayList<>();
    ListView schedulerList;
    SchedulerAdapter adapter;
    private ProgressDialog loader;

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
        Utils.setTitleFontTypeface(mToolbar);
        containerAddNew = (LinearLayout) findViewById(R.id.containerAddNew);
        containerAddNew.setOnClickListener(this);
        schedulerList = (ListView) findViewById(R.id.schedularList);
        /*schedulerList.setOnItemLongClickListener(this);
        schedulerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/

        adapter = new SchedulerAdapter(mContext, taskList);
        schedulerList.setAdapter(adapter);
        loader = new ProgressDialog(mContext);
        getScheduler();
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
                startActivityForResult(new Intent(SchedulerActivity.this, AddScheduleTaskActivity.class), 1);
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
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



//    /**
//     * dialog to confirming user for Make admin and Remove admin
//     *
//     * @param title
//     * @param message
//     * @param taskId
//     */
//    public void confirmationAlertDialog(String title, String message, final String taskId) {
//        SpannableString s = new SpannableString(title);
//        s.setSpan(new TypefaceSpan("avenir-roman.otf"), 0, s.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                SchedulerActivity.this);
//
//        // set title
//        alertDialogBuilder.setTitle(s);
//
//        // set dialog message
//        alertDialogBuilder
//                .setMessage(message)
//                .setCancelable(false)
//                .setPositiveButton(getResources().getString(R.string.CommonYes), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //Add condition for self login admin functionality after change its own permission it should be logout.
//                        dialog.cancel();
//                        removeScheduler(taskId);
//                    }
//                });
//        // set dialog message
//        alertDialogBuilder.setCancelable(false)
//                .setNegativeButton(getResources().getString(R.string.CommonNo), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//
//                    }
//                });
//        final AlertDialog alertDialog = alertDialogBuilder.create();
//        // show it
//        alertDialog.show();
//        // change color of delete text
//        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(SchedulerActivity.this.getResources().getColor(R.color.colorPrimaryDark));
//        //change for font style of button
//        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(SNApplication.APP_FONT_TYPEFACE);
//        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(SNApplication.APP_FONT_TYPEFACE);
//    }

//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        TaskModel model = (TaskModel)parent.getAdapter().getItem(position);
//        confirmationAlertDialog(getResources().getString(R.string.DeleteScheduleTitle),
//                getResources().getString(R.string.DeleteScheduleMessage), model.getTaskId());
//        return false;
//    }

    private void getScheduler(){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_SCHEDULER_LIST,
                    getSchedulerRequestMap(APIUtils.CMD_SCHEDULER_LIST, AppSPrefs.getDeviceId(), AppSPrefs.getString(Commons.ACCESS_TOKEN)));
        } else {
            finish();
            Logger.i(TAG, "Not connected to Internet.");
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param method
     * @param accessToken
     * @return
     */
    public HashMap<String, String> getSchedulerRequestMap(String method, String deviceId, String accessToken) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.CONFIGURED_DEVICE_ID, deviceId);
        map.put(Commons.ACCESS_TOKEN, accessToken);
        return map;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null){
            switch (resultCode){
                case 1:
                    getScheduler();
                    break;
            }
        }
    }

    @Override
    public void onSuccess(String name, Object object) {
        Utils.dismissLoader();
        Logger.i(TAG, "onSuccess"+" Name: "+name+"Response: " + object);
        try {
            if (APIUtils.CMD_SCHEDULER_LIST.equalsIgnoreCase(name)) {
                TaskModel model = ResponseParser.parseGetTaskResponse(object);
                if (model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)
                        || "No task scheduled in this device".equalsIgnoreCase(model.getResponseMsg())) {
                    adapter.setData(model.getTaskList());
                    adapter.notifyDataSetChanged();
                } else {
                  Toast.makeText(mContext, model.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            } else if (APIUtils.CMD_SCHEDULER_ADD.equalsIgnoreCase(name)) {
                getScheduler();
            } else if (APIUtils.CMD_SCHEDULER_DELETE.equalsIgnoreCase(name)) {
                getScheduler();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        Utils.dismissLoader();
        Logger.i(TAG, "onFailure"+" Name: "+name+"Response: " + object);
    }


}
