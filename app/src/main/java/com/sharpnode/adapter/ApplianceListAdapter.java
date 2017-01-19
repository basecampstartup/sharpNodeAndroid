package com.sharpnode.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.DeviceDashboardActivity;
import com.sharpnode.R;
import com.sharpnode.RenameApplianceActivity;
import com.sharpnode.SNApplication;
import com.sharpnode.cloudcommunication.CloudCommunicator;
import com.sharpnode.cloudcommunication.CloudUtils;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.ApplianceModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 12/7/2016.
 */

public class ApplianceListAdapter extends RecyclerView.Adapter<ApplianceListAdapter.ViewHolder>{
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private ArrayList<ApplianceModel> appliances;
    private String clickedAppliance = "";
    private ProgressDialog loader;
    private Integer[] applianceIcons = {
            R.drawable.cfl,
            R.drawable.fan,
            R.drawable.lamp,
            R.drawable.tv,
            R.drawable.washing_machine,
            R.drawable.music
    };

    private Integer[] applianceIconsTeal = {
            R.drawable.cfl_teal,
            R.drawable.fan_teal,
            R.drawable.lamp_teal,
            R.drawable.tv_teal,
            R.drawable.washing_machine_teal,
            R.drawable.music_teal
    };

    public ApplianceListAdapter(Context mContext, ArrayList<ApplianceModel> appliances) {
        this.mContext = mContext;
        this.appliances = appliances;
        loader = new ProgressDialog(mContext);
    }

    public void setData(ArrayList<ApplianceModel> appliances) {
        this.appliances = appliances;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvApplianceName;
        public ImageView ivApplianceIcon, ivSwitchBtn;
        public RelativeLayout rlParentLayout;

        public ViewHolder(View view) {
            super(view);

            rlParentLayout = (RelativeLayout)view.findViewById(R.id.rlParentLayout);
            ivApplianceIcon = (ImageView) view.findViewById(R.id.ivApplianceIcon);
            tvApplianceName = (TextView) view.findViewById(R.id.tvApplianceName);
            ivSwitchBtn = (ImageView) view.findViewById(R.id.ivSwitchBtn);

            tvApplianceName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        }
    }

    @Override
    public ApplianceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appliance_listrow, parent, false);


        return new ApplianceListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ApplianceListAdapter.ViewHolder holder, final int position) {
        holder.tvApplianceName.setText(appliances.get(position).getName().toUpperCase());
        if(appliances.get(position).isStatus()){
            holder.ivApplianceIcon.setImageResource(applianceIconsTeal[position]);
            holder.ivSwitchBtn.setImageResource(R.drawable.on_btn);
        } else {
            holder.ivApplianceIcon.setImageResource(applianceIcons[position]);
            holder.ivSwitchBtn.setImageResource(R.drawable.off_btn);
        }

        holder.ivSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.multipleTapDelayLONG())
                    return;
                if(DeviceDashboardActivity.isConnected){
                    if(CheckNetwork.isInternetAvailable(mContext)){
                        String value = "";
                        if (appliances.get(position).isStatus()) {
                            holder.ivApplianceIcon.setImageResource(applianceIcons[position]);
                            holder.ivSwitchBtn.setImageResource(R.drawable.off_btn);
                            appliances.get(position).setStatus(false);
                            value = "l" + (position + 1) + ",HIGH";
                        } else {
                            holder.ivApplianceIcon.setImageResource(applianceIconsTeal[position]);
                            holder.ivSwitchBtn.setImageResource(R.drawable.on_btn);
                            appliances.get(position).setStatus(true);
                            value = "l" + (position + 1) + ",LOW";
                        }
                        Logger.i(TAG, "Switch: value=" + value);
                        notifyDataSetChanged();
                        makeSwitchOnOff(value);
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.check_for_internet_connectivity),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Utils.okAlertDialog(mContext, "Device is offline, please turn it ON.");
                }
            }
        });

        holder.rlParentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent renameApplianceIntent = new Intent(mContext, RenameApplianceActivity.class);
                renameApplianceIntent.putExtra("APPLIANCE_NAME", appliances.get(position).getName());
                renameApplianceIntent.putExtra("SWITCH_ID", appliances.get(position).getSwitchId());
                mContext.startActivity(renameApplianceIntent);
                ((Activity)mContext).overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return appliances.size();
    }

    private void makeSwitchOnOff(String value){
        if(DeviceDashboardActivity.isConnected){
            if(CheckNetwork.isInternetAvailable(mContext)){
                //Utils.showLoader(mContext, loader);
                //Call Cloud API Request after check internet connection
                new CloudCommunicator(mContext, null, CloudUtils.CLOUD_FUNCTION_LED,
                        getParams(AppSPrefs.getDeviceId(), value));
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.check_for_internet_connectivity),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.device_is_offline),
                    Toast.LENGTH_LONG).show();
        }
    }

    private HashMap<String, String> getParams(String deviceId, String switchOnOff){
        HashMap<String, String> params = new HashMap<>();
        params.put(Commons.CONFIGURED_DEVICE_ID, deviceId);
        params.put(CloudUtils.SWITCH_OPERATION_FOR_LED, switchOnOff);
        return params;
    }
}
