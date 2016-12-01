package com.sharpnode.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.DeviceDashboardActivity;
import com.sharpnode.R;
import com.sharpnode.SNApplication;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.ConfiguredDevices;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.sprefs.AppSPrefs;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 11/22/2016.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> implements PopupMenu.OnMenuItemClickListener {

    private Context mContext;
    private ArrayList<ConfiguredDevices> devices;
    private String clickedDeviceId="";

    public DeviceAdapter(Context mContext, ArrayList<ConfiguredDevices> devices) {
        this.mContext = mContext;
        this.devices = devices;
    }

    public void setData(ArrayList<ConfiguredDevices> devices) {
        this.devices = devices;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvDeviceName, tvDeviceId, tvPresence, tvDeviceType;
        public ImageView btnRemove;
        public RelativeLayout rlParentLayout;

        public ViewHolder(View view) {
            super(view);

            rlParentLayout = (RelativeLayout)view.findViewById(R.id.rlParentLayout);
            tvDeviceName = (TextView) view.findViewById(R.id.tvDeviceName);
            tvDeviceId = (TextView) view.findViewById(R.id.tvDeviceId);
            tvPresence = (TextView) view.findViewById(R.id.tvPresence);
            tvDeviceType = (TextView) view.findViewById(R.id.tvDeviceType);
            btnRemove = (ImageView) view.findViewById(R.id.btnRemove);

            tvDeviceName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
            tvDeviceId.setTypeface(SNApplication.APP_FONT_TYPEFACE);
            tvPresence.setTypeface(SNApplication.APP_FONT_TYPEFACE);
            tvDeviceType.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        }

    }

    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.configured_devices_listrow, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceAdapter.ViewHolder holder, final int position) {
        holder.tvDeviceId.setText(devices.get(position).getDeviceId());
        holder.tvDeviceName.setText(devices.get(position).getDeviceName());
        holder.tvDeviceType.setText("Electrify-GQN2");

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(DeviceAdapter.this);
                popupMenu.inflate(R.menu.menu_device_options);
                popupMenu.show();
                clickedDeviceId = devices.get(position).getDeviceId();
            }
        });


        holder.rlParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSPrefs.setString(Commons.CONFIGURED_DEVICE_ID, clickedDeviceId);
                mContext.startActivity(new Intent(mContext, DeviceDashboardActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_rename:
                Toast.makeText(mContext, "Rename Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_remove:
                removeDevice(clickedDeviceId);
                Toast.makeText(mContext, "Remove Clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    private void removeDevice(String deviceId){
        //Call API Request after check internet connection
        new Communicator(mContext, APIUtils.CMD_REMOVE_DEVICE,
                removeDeviceRequestMap(APIUtils.CMD_REMOVE_DEVICE,
                        deviceId,
                        AppSPrefs.getString(Commons.ACCESS_TOKEN)));
    }

    /**
     * @param method
     * @param accessToken
     * @return
     */
    public HashMap<String, String> removeDeviceRequestMap(String method, String deviceId, String accessToken) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.CONFIGURED_DEVICE_ID, deviceId);
        map.put(Commons.ACCESS_TOKEN, accessToken);
        return map;
    }
}
