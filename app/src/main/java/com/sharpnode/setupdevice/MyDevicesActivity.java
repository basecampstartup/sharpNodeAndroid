package com.sharpnode.setupdevice;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.DeviceDashboardActivity;
import com.sharpnode.R;
import com.sharpnode.SNApplication;
import com.sharpnode.adapter.DeviceAdapter;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.cloudcommunication.CloudCommunicator;
import com.sharpnode.cloudcommunication.CloudResponseParser;
import com.sharpnode.cloudcommunication.CloudUtils;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.BaseModel;
import com.sharpnode.model.ConfiguredDevices;
import com.sharpnode.model.DeviceInfoModel;
import com.sharpnode.model.DeviceModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.Responses;
import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.SoftAPConfigRemover;

/**
 * Created by admin on 11/15/2016.
 */

public class MyDevicesActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak {

    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private ParticleDeviceSetupLibrary.DeviceSetupCompleteReceiver receiver = null;
    private Toolbar mToolbar;
    private Async.AsyncApiWorker<ParticleCloud, Responses.ClaimCodeResponse> claimCodeWorker;
    private RecyclerView rvDevices;
    private LinearLayoutManager mLayoutManager;
    private DeviceAdapter mAdapter;
    private ArrayList<ConfiguredDevices> devices = null;
    private ImageButton btnAddDevice;
    private TextView tvNoDeviceFound;
    private ProgressDialog loader;
    private Timer timer;
    private MyTimerTask myTimerTask;

    private void prepareDeviceList(){
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvDevices.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rvDevices.setLayoutManager(mLayoutManager);
        devices = new ArrayList<>();
        // specify an adapter (see also next example)
        mAdapter = new DeviceAdapter(this, devices);
        rvDevices.setAdapter(mAdapter);

        btnAddDevice = (ImageButton)findViewById(R.id.btnAddDevice);
        btnAddDevice.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_devices);

        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.LeftPanelDeviceManual));
        Utils.setTitleFontTypeface(mToolbar);
        tvNoDeviceFound = (TextView)findViewById(R.id.tvNoDeviceFound);
        tvNoDeviceFound.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        rvDevices = (RecyclerView) findViewById(R.id.rvDevices);
        rvDevices.setVisibility(View.GONE);
        tvNoDeviceFound.setVisibility(View.GONE);
        loader = new ProgressDialog(mContext);
        prepareDeviceList();
        getDevices();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddDevice:
                startActivity(new Intent(mContext, DeviceSetupActivity.class));
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                finish();
                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        this.finish();
    }

    private void getDevices(){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_GET_DEVICES,
                    getDeviceRequestMap(APIUtils.CMD_GET_DEVICES, AppSPrefs.getString(Commons.ACCESS_TOKEN)));
        } else {
            finish();
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

    private void getDeviceStatus(String deviceId){
        if(CheckNetwork.isInternetAvailable(mContext)){
            //Utils.showLoader(mContext, loader);
            //Call Cloud API Request after check internet connection
            new CloudCommunicator(mContext, null, CloudUtils.CLOUD_FUNCTION_DEVICE_STATUS, getStatusReqParams(deviceId));
        } else {
            Logger.i(TAG, getString(R.string.check_for_internet_connectivity));
        }
    }

    private HashMap<String, String> getStatusReqParams(String deviceId){
        HashMap<String, String> params = new HashMap<>();
        params.put(Commons.CONFIGURED_DEVICE_ID, deviceId);
        params.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        return params;
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
                    mAdapter.setData(devices);
                    mAdapter.notifyDataSetChanged();
                    if (model.getDevicesList().size() > 0)
                        AppSPrefs.setString(Commons.CONFIGURED_DEVICE_ID, model.getDevicesList().get(0).getDeviceId());
                    initTimer(1*1000);
                }
            } else if (APIUtils.CMD_DEVICE_INFO.equalsIgnoreCase(name)) {
                Logger.i(TAG, "onSuccess"+" Name: "+name+" Response: " + object);
                //Parsed only for check status is 200.
                BaseModel model = ResponseParser.parseResponse(object);
                if (model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    Intent deviceInfoIntent = new Intent(mContext, DeviceDashboardActivity.class);
                    //Sending response JSON data on called Activity for later use.
                    deviceInfoIntent.putExtra("DEVICE_INFO", object.toString());
                    startActivity(deviceInfoIntent);
                    //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                }
            } else if (APIUtils.CMD_REMOVE_DEVICE.equalsIgnoreCase(name)) {
                getDevices();
            } else if (APIUtils.CMD_RENAME_DEVICE.equalsIgnoreCase(name)) {
                getDevices();
            } else if(CloudUtils.CLOUD_FUNCTION_DEVICE_STATUS.equalsIgnoreCase(name)){
                DeviceModel model = CloudResponseParser.parseDeviceStatusResponse(object);
                CloudUtils.deviceStatus.put(model.getDevice_id().toLowerCase(), model.isDeviceStatus());
                if(mAdapter!=null){
                    mAdapter.notifyDataSetChanged();
                }
                initTimer(Utils.delay45Seconds);
            }

            if (mAdapter != null && mAdapter.getItemCount() == 0) {
                rvDevices.setVisibility(View.GONE);
                tvNoDeviceFound.setVisibility(View.VISIBLE);
            } else {
                rvDevices.setVisibility(View.VISIBLE);
                tvNoDeviceFound.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        Utils.dismissLoader();
        Logger.i(TAG, "onFailure"+" Name: "+name+"Response: " + object);

        if(CloudUtils.CLOUD_FUNCTION_DEVICE_STATUS.equalsIgnoreCase(name)){
//            DeviceModel model = CloudResponseParser.parseDeviceStatusResponse(object);
//            CloudUtils.deviceStatus.put(model.getDevice_id().toLowerCase(), model.isDeviceStatus());
//            if(mAdapter!=null){
//                mAdapter.notifyDataSetChanged();
//            }
            initTimer(Utils.delay45Seconds);
        }
    }

    private void initTimer(int delayTime) {
        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, delayTime);
    }

    private int counter = 0;

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (counter < devices.size()) {
                        String deviceId = devices.get(counter).getDeviceId();
                        counter++;
                        Logger.i(TAG, "Timer Running! Device Id: " + deviceId);
                        //update UI here.
                        getDeviceStatus(deviceId);
                    } else {
                        counter = 0;
                    }
                }
            });
        }
    }

    private void cancelTimer(){
        if(timer!=null){
            timer.cancel();
        }

        if(myTimerTask!=null){
            myTimerTask.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}
