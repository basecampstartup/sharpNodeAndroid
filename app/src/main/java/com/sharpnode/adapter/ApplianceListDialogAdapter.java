package com.sharpnode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sharpnode.R;

import java.util.ArrayList;

/**
 * Created by admin on 11/17/2016.
 */

public class ApplianceListDialogAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> appliancesList;
    private LayoutInflater layoutInflater;

    public ApplianceListDialogAdapter(Context mContext, ArrayList<String> appliancesList) {
        this.mContext = mContext;
        this.appliancesList = appliancesList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return appliancesList.size();
    }

    @Override
    public Object getItem(int position) {
        return appliancesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
            holder = new ViewHolder();
            holder.txtApplianceName = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtApplianceName.setText(appliancesList.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView txtApplianceName;
    }
}
