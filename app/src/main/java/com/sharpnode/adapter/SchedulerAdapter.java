package com.sharpnode.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.AddScheduleTaskActivity;
import com.sharpnode.R;
import com.sharpnode.SNApplication;
import com.sharpnode.SchedulerActivity;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.SchedulerModel;
import com.sharpnode.model.TaskModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 11/9/2016.
 */

public class SchedulerAdapter extends BaseAdapter implements PopupMenu.OnMenuItemClickListener{
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    public ArrayList<TaskModel> taskList;
    private LayoutInflater lInflater;
    private ProgressDialog loader;
    private String clickedTaskId;

    public SchedulerAdapter(Context mContext, ArrayList<TaskModel> taskList) {
        this.mContext = mContext;
        this.taskList = taskList;
        lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loader = new ProgressDialog(mContext);
    }

    public void setData(ArrayList<TaskModel> taskList){
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.schedular_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.rlParent = (RelativeLayout)convertView.findViewById(R.id.rlParent);
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.tvTaskName);
            viewHolder.taskName.setTypeface(SNApplication.APP_FONT_TYPEFACE, Typeface.BOLD);
            viewHolder.turnOnOffStatus = (TextView) convertView.findViewById(R.id.tvTurnOnOff);
            viewHolder.turnOnOffStatus.setTypeface(SNApplication.APP_FONT_TYPEFACE, Typeface.BOLD);
            viewHolder.tvRepeat = (TextView) convertView.findViewById(R.id.tvTurnOnOffDays);
            viewHolder.tvRepeat.setTypeface(SNApplication.APP_FONT_TYPEFACE, Typeface.NORMAL);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.txtTime.setTypeface(SNApplication.APP_FONT_TYPEFACE, Typeface.NORMAL);
            viewHolder.ivOptions = (ImageView)convertView.findViewById(R.id.btnOptions);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.taskName.setText(taskList.get(position).getTaskName());
        viewHolder.tvRepeat.setText(taskList.get(position).getRepeat());
        viewHolder.txtTime.setText(taskList.get(position).getTime());

        viewHolder.rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddScheduleTaskActivity.class);
                intent.putExtra("KEY", "UPDATE");
                intent.putExtra("TASK_NAME", taskList.get(position).getTaskName());
                intent.putExtra("TASK_ID", taskList.get(position).getTaskId());
                intent.putExtra("TIME", taskList.get(position).getTime());
                intent.putExtra("REPEAT", taskList.get(position).getRepeat());
                intent.putExtra("ACTION", taskList.get(position).getAction());
                intent.putExtra("STATUS", taskList.get(position).getTaskStatus());
                intent.putExtra("NEXT_DATE", taskList.get(position).getNextDate());
                ((Activity)mContext).startActivityForResult(intent, 1);
                ((Activity)mContext).overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
            }
        });

        viewHolder.ivOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedTaskId = taskList.get(position).getTaskId();
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(SchedulerAdapter.this);
                popupMenu.inflate(R.menu.menu_task_options);
                popupMenu.show();
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        public ImageView ivOptions;
        public TextView taskName;
        public TextView turnOnOffStatus;
        public TextView tvRepeat;
        public TextView txtTime;
        public RelativeLayout rlParent;
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.item_update:
//                return true;
            case R.id.item_remove:
                confirmationAlertDialog(mContext.getResources().getString(R.string.DeleteScheduleTitle),
                        mContext.getResources().getString(R.string.DeleteScheduleMessage), clickedTaskId);
                return true;
            default:
                return false;
        }
    }

    /**
     * dialog to confirming user for Make admin and Remove admin
     *
     * @param title
     * @param message
     * @param taskId
     */
    public void confirmationAlertDialog(String title, String message, final String taskId) {
        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan("avenir-roman.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set title
        alertDialogBuilder.setTitle(s);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(R.string.CommonYes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Add condition for self login admin functionality after change its own permission it should be logout.
                        dialog.cancel();
                        removeScheduler(taskId);
                    }
                });
        // set dialog message
        alertDialogBuilder.setCancelable(false)
                .setNegativeButton(mContext.getResources().getString(R.string.CommonNo), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
        // change color of delete text
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        //change for font style of button
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(SNApplication.APP_FONT_TYPEFACE);
    }

    private void removeScheduler(String taskId){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_SCHEDULER_DELETE,
                    getRemoveSchedulerRequestMap(APIUtils.CMD_SCHEDULER_DELETE, AppSPrefs.getDeviceId(), taskId));
        } else {
            Logger.i(TAG, "Not connected to Internet.");
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
        }
    }

    public HashMap<String, String> getRemoveSchedulerRequestMap(String method, String deviceId, String taskId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.CONFIGURED_DEVICE_ID, deviceId);
        map.put("task_id", taskId);
        map.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        return map;
    }
}
