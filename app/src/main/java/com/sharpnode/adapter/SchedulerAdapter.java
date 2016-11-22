package com.sharpnode.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sharpnode.R;
import com.sharpnode.SNApplication;
import com.sharpnode.model.SchedulerModel;

import java.util.ArrayList;

/**
 * Created by admin on 11/9/2016.
 */

public class SchedulerAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SchedulerModel> schedulerModels;
    private LayoutInflater lInflater;

    public SchedulerAdapter(Context mContext, ArrayList<SchedulerModel> schedulerModels) {
        this.mContext = mContext;
        this.schedulerModels = schedulerModels;
        lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return schedulerModels.size();
    }

    @Override
    public Object getItem(int position) {
        return schedulerModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.schedular_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.tvTaskName);
            viewHolder.taskName.setTypeface(SNApplication.APP_FONT_TYPEFACE, Typeface.BOLD);
            viewHolder.turnOnOffStatus = (TextView) convertView.findViewById(R.id.tvTurnOnOff);
            viewHolder.turnOnOffStatus.setTypeface(SNApplication.APP_FONT_TYPEFACE, Typeface.BOLD);
            viewHolder.weekDays = (TextView) convertView.findViewById(R.id.tvTurnOnOffDays);
            viewHolder.weekDays.setTypeface(SNApplication.APP_FONT_TYPEFACE, Typeface.NORMAL);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.txtTime.setTypeface(SNApplication.APP_FONT_TYPEFACE, Typeface.NORMAL);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.taskName.setText(schedulerModels.get(position).getScheduleTaskName());
        if (schedulerModels.get(position).isTurnOnOff()) {
            viewHolder.turnOnOffStatus.setText("Turn ON");
        } else {
            viewHolder.turnOnOffStatus.setText("Turn OFF");
        }
        viewHolder.txtTime.setText(schedulerModels.get(position).getTime());
        if (schedulerModels.get(position).getWeekDays().equalsIgnoreCase("")) {
            viewHolder.weekDays.setText("Every Day");
        } else {
            viewHolder.weekDays.setText(schedulerModels.get(position).getWeekDays());
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView taskName;
        public TextView turnOnOffStatus;
        public TextView weekDays;
        public TextView txtTime;
    }
}
