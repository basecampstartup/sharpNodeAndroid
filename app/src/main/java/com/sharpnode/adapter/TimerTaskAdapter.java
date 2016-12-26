package com.sharpnode.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.R;
import com.sharpnode.SNApplication;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.TimerModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 12/16/2016.
 */

public class TimerTaskAdapter extends BaseAdapter implements PopupMenu.OnMenuItemClickListener{
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private ArrayList<TimerModel> taskList;
    private LayoutInflater lInflater;
    private String clickedId;
    private ProgressDialog loader;

    public TimerTaskAdapter(Context mContext, ArrayList<TimerModel> taskList) {
        this.mContext = mContext;
        this.taskList = taskList;
        lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loader = new ProgressDialog(mContext);
    }

    public void setData(ArrayList<TimerModel> taskList){
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
        final TimerTaskAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.timer_listrow, parent, false);
            viewHolder = new TimerTaskAdapter.ViewHolder();
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.tvTaskName);
            viewHolder.taskName.setTypeface(SNApplication.APP_FONT_TYPEFACE, Typeface.BOLD);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.txtTime.setTypeface(SNApplication.APP_FONT_TYPEFACE, Typeface.NORMAL);
            viewHolder.txtMinTime = (TextView) convertView.findViewById(R.id.tvMinTime);
            viewHolder.txtMinTime.setTypeface(SNApplication.APP_FONT_TYPEFACE, Typeface.NORMAL);
            viewHolder.tvRepeat = (TextView) convertView.findViewById(R.id.tvRepeat);
            viewHolder.tvRepeat.setTypeface(SNApplication.APP_FONT_TYPEFACE, Typeface.NORMAL);
            viewHolder.ivOptions = (ImageView)convertView.findViewById(R.id.btnOptions);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TimerTaskAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.taskName.setText(taskList.get(position).getTaskName());
        viewHolder.txtTime.setText(taskList.get(position).getTime());
        String repeat = taskList.get(position).getRepeat();

        if (TextUtils.isEmpty(repeat) || "false".equalsIgnoreCase(repeat))
            viewHolder.tvRepeat.setText("Repeat: Off");
        else
            viewHolder.tvRepeat.setText("Repeat: On");

        viewHolder.txtMinTime.setText(taskList.get(position).getInterval()+" Min");

        viewHolder.ivOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedId = taskList.get(position).getTimerId();
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(TimerTaskAdapter.this);
                popupMenu.inflate(R.menu.menu_task_options);
                popupMenu.show();
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        public TextView taskName, tvRepeat, txtMinTime;
        public TextView txtTime;
        public ImageView ivOptions;
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.item_update:
//                return true;
            case R.id.item_remove:
                confirmationAlertDialog(mContext.getResources().getString(R.string.DeleteTimerTitle),
                        mContext.getResources().getString(R.string.DeleteTimerMessage), clickedId);
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
            new Communicator(mContext, null, APIUtils.CMD_TIMER_REMOVE,
                    getRemoveTimerRequestMap(APIUtils.CMD_TIMER_REMOVE, AppSPrefs.getDeviceId(), taskId));
        } else {
            Logger.i(TAG, "Not connected to Internet.");
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
        }
    }

    public HashMap<String, String> getRemoveTimerRequestMap(String method, String deviceId, String taskId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.CONFIGURED_DEVICE_ID, deviceId);
        map.put("timer_id", taskId);
        map.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        return map;
    }
}
