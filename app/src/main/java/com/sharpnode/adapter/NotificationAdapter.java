package com.sharpnode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sharpnode.R;
import com.sharpnode.model.NotificationModel;

import java.util.ArrayList;

/**
 * Created by admin on 12/2/2016.
 */

public class NotificationAdapter extends BaseAdapter {


    Context mContext;
    ArrayList<NotificationModel> noificationList;
    private LayoutInflater layoutInflater;

    public NotificationAdapter(Context mContext, ArrayList<NotificationModel> noificationList) {
        this.mContext = mContext;
        this.noificationList = noificationList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return noificationList.size();
    }

    @Override
    public Object getItem(int position) {
        return noificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.notification_list_item, null);
            holder = new NotificationAdapter.ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(holder);
        } else {
            holder = (NotificationAdapter.ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(noificationList.get(position).getTitle());
        holder.tvTime.setText(noificationList.get(position).getDateTime());
        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvTime;
    }
}
