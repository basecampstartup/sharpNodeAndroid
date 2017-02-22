package com.sharpnode.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.DeviceDashboardActivity;
import com.sharpnode.R;
import com.sharpnode.SNApplication;
import com.sharpnode.cloudcommunication.CloudUtils;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.ConfiguredDevices;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.setupdevice.MyDevicesActivity;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;
import com.sharpnode.widget.WidgetUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 11/22/2016.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> implements PopupMenu.OnMenuItemClickListener {

    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private ArrayList<ConfiguredDevices> devices;
    private String clickedDeviceId = "";
    private String clickedDeviceName = "";
    private ProgressDialog loader;
    private String lastIP="";

    public DeviceAdapter(Context mContext, ArrayList<ConfiguredDevices> devices) {
        this.mContext = mContext;
        this.devices = devices;
        loader = new ProgressDialog(mContext);
    }

    public void setData(ArrayList<ConfiguredDevices> devices) {
        this.devices = devices;
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_rename:
                showRenameDeviceDialog(clickedDeviceId, clickedDeviceName);
                return true;
            case R.id.item_remove:
                removeDeviceAlert(clickedDeviceId);
                //removeDevice(clickedDeviceId);
                return true;
            default:
                return false;
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvDeviceName, tvDeviceId, tvPresence, tvDeviceType;
        public ImageView btnRemove, ivPresence;
        public RelativeLayout rlParentLayout;

        public ViewHolder(View view) {
            super(view);

            rlParentLayout = (RelativeLayout) view.findViewById(R.id.rlParentLayout);
            tvDeviceName = (TextView) view.findViewById(R.id.tvDeviceName);
            tvDeviceId = (TextView) view.findViewById(R.id.tvDeviceId);
            tvPresence = (TextView) view.findViewById(R.id.tvPresence);
            tvDeviceType = (TextView) view.findViewById(R.id.tvDeviceType);
            btnRemove = (ImageView) view.findViewById(R.id.btnRemove);
            ivPresence = (ImageView) view.findViewById(R.id.ivPresence);

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
        ConfiguredDevices model = devices.get(position);
        holder.tvDeviceId.setText(model.getDeviceId());
        holder.tvDeviceName.setText(model.getDeviceName());
        holder.tvDeviceType.setText("Photon"); //Electrify-GQN2
        if(CloudUtils.deviceStatus.size() > 0 && model.getDeviceId()!=null
                && (CloudUtils.deviceStatus.get(model.getDeviceId().toLowerCase()))
                && CloudUtils.deviceStatus.get(model.getDeviceId().toLowerCase())){
            holder.tvPresence.setText(mContext.getString(R.string.PresenceOnline));
            holder.ivPresence.setImageResource(android.R.drawable.presence_online);

            if(AppSPrefs.getWidgetDeviceId().equalsIgnoreCase(model.getDeviceId())){
                WidgetUtils.isWidgetDeviceConnected = true;
            }
        } else {
            if(AppSPrefs.getWidgetDeviceId().equalsIgnoreCase(model.getDeviceId())){
                WidgetUtils.isWidgetDeviceConnected = false;
            }
            holder.tvPresence.setText(mContext.getString(R.string.PresenceOffline));
            holder.ivPresence.setImageResource(android.R.drawable.presence_invisible);
        }

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedDeviceName = devices.get(position).getDeviceName();
                clickedDeviceId = devices.get(position).getDeviceId();
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(DeviceAdapter.this);
                popupMenu.inflate(R.menu.menu_device_options);
                popupMenu.show();
            }
        });

        holder.rlParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedDeviceId = devices.get(position).getDeviceId();
                if(CloudUtils.deviceStatus.get(clickedDeviceId.toLowerCase())){
                    AppSPrefs.setString(Commons.CONFIGURED_DEVICE_ID, devices.get(position).getDeviceId());
                    AppSPrefs.setString(Commons.CONFIGURED_DEVICE_NAME, devices.get(position).getDeviceName());
                    //mContext.startActivity(new Intent(mContext, DeviceDashboardActivity.class));
                    getDeviceInfo(devices.get(position).getDeviceId());
                } else {
                    lastIP = devices.get(position).getLastIP();
                    String msg = "This device is offline. If you want operate offline, please select offline.";
                    Utils.offlineDeviceAlertDialog(mContext, "Device Offline", msg, lastIP);
                }
            }
        });
    }

    public void showRenameDeviceDialog(final String clickedDeviceId, final String value) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
//        View view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.rename_device_dialog, null);
//        dialogBuilder.setView(view);
//        final EditText edtDeviceName = (EditText) view.findViewById(R.id.edtDeviceName);
//        edtDeviceName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
//        edtDeviceName.setText(value);
//        Button btnDone = (Button) view.findViewById(R.id.btnDone);
//        btnDone.setTypeface(SNApplication.APP_FONT_TYPEFACE);
//        final AlertDialog dialog = dialogBuilder.create();
//        btnDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String value = edtDeviceName.getText().toString();
//                if(!TextUtils.isEmpty(value)) {
//                    dialog.dismiss();
//                    renameDevice(clickedDeviceId, value);
//                }
//            }
//        });
//        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
//        btnCancel.setTypeface(SNApplication.APP_FONT_TYPEFACE);
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder((MyDevicesActivity) mContext/*, R.style.MyAlertDialogStyle*/);
        View view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.rename_device_dialog, null);
        builder.setView(view);
        final EditText edtDeviceName = (EditText) view.findViewById(R.id.edtDeviceName);
        edtDeviceName.setTextColor(mContext.getResources().getColor(R.color.color_gray));
        edtDeviceName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtDeviceName.setText(value);
        builder.setCancelable(false)
                .setPositiveButton(mContext.getString(R.string.Done), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String value = edtDeviceName.getText().toString();
                        if (!TextUtils.isEmpty(value)) {
                            dialog.dismiss();
                            renameDevice(clickedDeviceId, value);
                        }
                    }
                })

                .setNegativeButton(mContext.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setLayout(480, 480);
        //alertDialog.setTitle("Rename Device");
        alertDialog.show();
    }

    private void removeDeviceAlert(final String clickedDeviceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure?, You wanted to remove device.");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                        removeDevice(clickedDeviceId);
                    }
                });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void removeDevice(String deviceId) {
        //Call API Request after check internet connection
        new Communicator(mContext, null, APIUtils.CMD_REMOVE_DEVICE,
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

    private void getDeviceInfo(String clickedDeviceId) {
        if (CheckNetwork.isInternetAvailable(mContext)) {
            Utils.showLoader(mContext, loader);
            //Call Cloud API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_DEVICE_INFO,
                    loadDeviceInfoRequestMap(APIUtils.CMD_DEVICE_INFO,
                            clickedDeviceId, AppSPrefs.getString(Commons.ACCESS_TOKEN)));
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.check_for_internet_connectivity),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param method
     * @param accessToken
     * @return
     */
    public HashMap<String, String> loadDeviceInfoRequestMap(String method, String deviceId, String accessToken) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.CONFIGURED_DEVICE_ID, deviceId);
        map.put(Commons.ACCESS_TOKEN, accessToken);
        return map;
    }

    private void renameDevice(String deviceId, String deviceName) {
        if (CheckNetwork.isInternetAvailable(mContext)) {
            Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_RENAME_DEVICE,
                    renameDeviceRequestMap(APIUtils.CMD_RENAME_DEVICE, deviceId, deviceName));
        } else {
            Logger.i(TAG, "Not connected to Internet.");
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param method
     * @param deviceId
     * @param newName
     * @return
     */
    public HashMap<String, String> renameDeviceRequestMap(String method, String deviceId, String newName) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.CONFIGURED_DEVICE_ID, deviceId);
        map.put(Commons.DEVICE_NEW_NAME, newName);
        map.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        return map;
    }
}
