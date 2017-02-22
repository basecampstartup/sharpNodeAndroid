package com.sharpnode;

import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.adapter.ApplianceListAdapter;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.cloudcommunication.CloudCommunicator;
import com.sharpnode.cloudcommunication.CloudUtils;
import com.sharpnode.commons.Commons;
import com.sharpnode.context.ContextHelper;
import com.sharpnode.model.ApplianceModel;
import com.sharpnode.model.BaseModel;
import com.sharpnode.model.DeviceInfoModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.setupdevice.MyDevicesActivity;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;
import com.sharpnode.widget.SharpNodeAppWidget;
import com.sharpnode.widget.WidgetUtils;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 11/8/2016.
 */
public class AppliancesActivity extends AppCompatActivity implements APIRequestCallbacak{

    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private Toolbar mToolbar;
    ArrayList<String> appliancesName=null;
    //private ArrayList<ApplianceModel> applianceList = null;
    private String switches = "";
    private RecyclerView rvAppliances;
    private ApplianceListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ProgressDialog loader;
    private Timer timer;
//    private MyTimerTask myTimerTask;

    private void prepareApplianceList(ArrayList<ApplianceModel> applianceList){
//        WidgetUtils.setWidgetAppliances(applianceList);
//        int[] ids = AppWidgetManager.getInstance(mContext).getAppWidgetIds(new ComponentName(mContext,
//                SharpNodeAppWidget.class));
//        //onUpdate(context, AppWidgetManager.getInstance(context), ids);
//        Intent updateIntent = new Intent(mContext, SharpNodeAppWidget.class);
//        updateIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
//        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
//        mContext.sendBroadcast(updateIntent);

        rvAppliances = (RecyclerView) findViewById(R.id.rvAppliances);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvAppliances.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rvAppliances.setLayoutManager(mLayoutManager);

        if(mAdapter==null){
            // specify an adapter (see also next example)
            mAdapter = new ApplianceListAdapter(mContext, applianceList);
            rvAppliances.setAdapter(mAdapter);
        } else {
            mAdapter.setData(applianceList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliances);
        ContextHelper.setContext(this);
        mContext = this;

        //Initialize toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.LeftPanelAppliances));
        Utils.setTitleFontTypeface(mToolbar);
        loader = new ProgressDialog(this);
        switches = getIntent().getStringExtra("SWITCH");
        appliancesName = getIntent().getStringArrayListExtra("APPLIANCE");
        Utils.arrAppliances = (String[]) appliancesName.toArray(new String[appliancesName.size()]);
        //applianceList = getApplianceList(appliancesName);
        prepareApplianceList(getApplianceList(appliancesName));
        getApplianceStatus();
//        initTimer(1*1000);
    }

    private ArrayList<ApplianceModel> getApplianceList(ArrayList<String> appliances){
        char c[] = switches.toCharArray();
        ApplianceModel model = null;
        ArrayList<ApplianceModel> list = new ArrayList<>();
        for (int i = 0; i < appliances.size(); i++) {
            model = new ApplianceModel();
            model.setName(appliances.get(i));
            model.setSwitchId(Utils.arrAppliancesKey[i]);
            model.setSwitchIndex(String.valueOf(c[i]));
            if(String.valueOf(c[i]).equalsIgnoreCase("2")){
                model.setStatus(false);  // Appliance is OFF.
            } else {
                model.setStatus(true); // Appliance is ON.
            }
            list.add(model);
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appliance_refresh, menu);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.item_refresh:
                //getDeviceInfo(AppSPrefs.getDeviceId());
                getApplianceStatus();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        }catch (Exception e){}

        this.finish();
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

    private void getApplianceStatus(){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new CloudCommunicator(mContext, null, CloudUtils.GET_APPLIANCE_STATUS,
                    getRequestMap(CloudUtils.GET_APPLIANCE_STATUS));
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
     * @return
     */
    public HashMap<String, String> getRequestMap(String method) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        map.put(Commons.CONFIGURED_DEVICE_ID, AppSPrefs.getDeviceId());
        return map;
    }

    @Override
    public void onSuccess(String name, Object object) {
        Utils.dismissLoader();
        Logger.i(TAG, name+", onSuccess, Response: " + object);
        try {
            JSONObject jsonObject = null;
            JSONObject coreInfo = null;
            if(CloudUtils.GET_APPLIANCE_STATUS.equalsIgnoreCase(name)){
                jsonObject = new JSONObject(object.toString());
                switches = jsonObject.optString("result");
                coreInfo = jsonObject.optJSONObject("coreInfo");
                String deviceId= coreInfo.optString("DeviceID");
                prepareApplianceList(getApplianceList(appliancesName));
                //initTimer(Utils.delay30Seconds);
            } else if(CloudUtils.CLOUD_FUNCTION_LED.equalsIgnoreCase(name)){ // Switch ON / OFF
                int[] ids = AppWidgetManager.getInstance(mContext).getAppWidgetIds(new ComponentName(mContext,
                        SharpNodeAppWidget.class));
                //onUpdate(context, AppWidgetManager.getInstance(context), ids);
                Intent updateIntent = new Intent(mContext, SharpNodeAppWidget.class);
                updateIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
                mContext.sendBroadcast(updateIntent);

                if (CheckNetwork.isInternetAvailable(mContext)) {
                    //Call API Request after check internet connection
                    new CloudCommunicator(mContext, null, CloudUtils.GET_APPLIANCE_STATUS,
                            getRequestMap(CloudUtils.GET_APPLIANCE_STATUS));
                }
            } else if (APIUtils.CMD_DEVICE_INFO.equalsIgnoreCase(name)) {
                Logger.i(TAG, "onSuccess"+" Name: "+name+" Response: " + object);
                //Parsed only for check status is 200.
                DeviceInfoModel model = ResponseParser.parseDeviceInfoResponse(object);
                switches = model.getSwitches();
                appliancesName = model.getApplianceList();
                Utils.arrAppliances = (String[]) appliancesName.toArray(new String[appliancesName.size()]);
                //applianceList = getApplianceList(appliancesName);
                prepareApplianceList(getApplianceList(appliancesName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        Utils.dismissLoader();
        Logger.i(TAG, name+", onFailure, Response: " + object);
    }

//    private void initTimer(int delayTime) {
//        timer = new Timer();
//        myTimerTask = new MyTimerTask();
//        timer.schedule(myTimerTask, delayTime);
//    }
//
//    class MyTimerTask extends TimerTask {
//
//        @Override
//        public void run() {
//            runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    Logger.i(TAG, "Timer Running! ");
//                    getApplianceStatus();
//                }
//            });
//        }
//    }
//
//    private void cancelTimer(){
//        if(timer!=null){
//            timer.cancel();
//        }
//
//        if(myTimerTask!=null){
//            myTimerTask.cancel();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        cancelTimer();
//    }
}
