package com.sharpnode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sharpnode.R;

import java.util.List;

/**
 * adapter class for show list of picture option's to change profile picture.
 */
public class DialogAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<String> pictureOptionList;
    private Context mContext;

    public DialogAdapter(Context context, List<String> pictureOptionList) {
        layoutInflater = LayoutInflater.from(context);
        this.pictureOptionList = pictureOptionList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return pictureOptionList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.simple_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(pictureOptionList.get(position));
        if (viewHolder.textView.getText() != null && viewHolder.textView.getText().toString().equalsIgnoreCase("cancel")) {
            viewHolder.textView.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.color_black));
        }
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
