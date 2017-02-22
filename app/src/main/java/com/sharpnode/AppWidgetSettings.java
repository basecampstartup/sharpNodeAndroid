package com.sharpnode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.adapter.ApplianceListDialogAdapter;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.cloudcommunication.CloudResponseParser;
import com.sharpnode.cloudcommunication.CloudUtils;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.BaseModel;
import com.sharpnode.model.ConfiguredDevices;
import com.sharpnode.model.DeviceModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.setupdevice.DeviceSetupActivity;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;
import com.sharpnode.widget.WidgetUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by admin on 1/18/2017.
 */

public class AppWidgetSettings extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak {

    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private ProgressDialog loader;
    private TextView tvOperationLabel, tvSelectDevice;
    private ArrayList<ConfiguredDevices> devices = new ArrayList();
    private ArrayList<String> deviceArray = new ArrayList<>();
    private ConfiguredDevices selectedDevice = null;
    private Button btnAdd;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_settings);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.AddWidget));
        Utils.setTitleFontTypeface(mToolbar);
        loader = new ProgressDialog(this);
        initializeComponents();
        getDevices();
    }

    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        btnAdd.setOnClickListener(this);

        tvOperationLabel = (TextView) findViewById(R.id.tvOperationLabel);
        tvOperationLabel.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        tvSelectDevice = (TextView) findViewById(R.id.tvSelectDevice);
        if(!TextUtils.isEmpty(AppSPrefs.getWidgetDevice())){
            tvSelectDevice.setText(AppSPrefs.getWidgetDevice());
        }
        tvSelectDevice.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvSelectDevice.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                if(getResources().getString(R.string.SelectDevice).equalsIgnoreCase(tvSelectDevice.getText().toString())){
                    Toast.makeText(mContext, "Device selection is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                addDeviceForWidget();
                break;
            case R.id.tvSelectDevice:
                showDeviceDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        this.finish();
    }

    public void showDeviceDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Choose Device");
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstAppliance);
        deviceArray = getDevicesArray(devices);
        // Change MyActivity.this and myListOfItems to your own values
        ApplianceListDialogAdapter applianceListDialogAdapter = new ApplianceListDialogAdapter(AppWidgetSettings.this, deviceArray);
        lv.setAdapter(applianceListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDevice = devices.get(position);
                tvSelectDevice.setText(deviceArray.get(position));
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private ArrayList<String> getDevicesArray(ArrayList<ConfiguredDevices> dList){
        ArrayList<String> list = new ArrayList<>();
        try{
            for (int i = 0; i < dList.size(); i++) {
                list.add(dList.get(i).getDeviceName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private void getDevices(){
        Utils.showLoader(mContext, loader);
        if (CheckNetwork.isInternetAvailable(mContext)) {
            //Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_GET_DEVICES,
                    getDeviceRequestMap(APIUtils.CMD_GET_DEVICES, AppSPrefs.getString(Commons.ACCESS_TOKEN)));
        } else {
            Logger.i(TAG, "Not connected to Internet.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * @param method
     * @param accessToken
     * @return
     */
    public HashMap<String, String> getDeviceRequestMap(String method, String accessToken) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.ACCESS_TOKEN, accessToken);
        return map;
    }

    private void addDeviceForWidget(){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_ADD_TO_FAV_DEVICE,
                    getRequestMap(APIUtils.CMD_ADD_TO_FAV_DEVICE, AppSPrefs.getString(Commons.ACCESS_TOKEN)));
        } else {
            Logger.i(TAG, "Not connected to Internet.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * @param method
     * @param accessToken
     * @return
     */
    public HashMap<String, String> getRequestMap(String method, String accessToken) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.CONFIGURED_DEVICE_ID, selectedDevice.getDeviceId());
        map.put(Commons.ACCESS_TOKEN, accessToken);
        return map;
    }

    @Override
    public void onSuccess(String name, Object object) {
        Utils.dismissLoader();

        try {
            if (APIUtils.CMD_GET_DEVICES.equalsIgnoreCase(name)) {
                Logger.i(TAG, "onSuccess"+" Name: "+name+" Response: " + object);
                ConfiguredDevices model = ResponseParser.parseGetDevicesResponse(object);
                if (model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    devices = model.getDevicesList();
                }
            } else if (APIUtils.CMD_ADD_TO_FAV_DEVICE.equalsIgnoreCase(name)) {
                Logger.i(TAG, "onSuccess"+" Name: "+name+" Response: " + object);
                BaseModel model = ResponseParser.parseResponse(object);
                if (model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    AppSPrefs.setWidgetDeviceId(selectedDevice.getDeviceId());
                    AppSPrefs.setWidgetDevice(selectedDevice.getDeviceName());
                    finish();
                }
                Toast.makeText(mContext, model.getResponseMsg(), Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        Utils.dismissLoader();
        Logger.i(TAG, "onFailure"+" Name: "+name+"Response: " + object);

        if(APIUtils.CMD_GET_DEVICES.equalsIgnoreCase(name)){
//            Toast.makeText(mContext, model.getResponseMsg(), Toast.LENGTH_LONG).show();
        } else if (APIUtils.CMD_ADD_TO_FAV_DEVICE.equalsIgnoreCase(name)) {
            BaseModel model = ResponseParser.parseResponse(object);
            Toast.makeText(mContext, model.getResponseMsg(), Toast.LENGTH_LONG).show();
        }
    }
}
