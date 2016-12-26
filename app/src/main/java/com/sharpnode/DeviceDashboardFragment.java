package com.sharpnode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.cloudcommunication.CloudCommunicator;
import com.sharpnode.cloudcommunication.CloudUtils;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.BaseModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 12/8/2016.
 */

public class DeviceDashboardFragment extends Fragment implements View.OnClickListener , APIRequestCallbacak{
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private TextView tvTemperature, tvHumidity, tvSecurityFeature, tvActiveDeviceLbl, tvActiveDeviceName;
    private ImageView ivSecurityFeature;
    private LinearLayout llShortcutAppliance, llShortcutScheduler, llShortcutTimer;
    private Animation animationEnlarge, animationShrink;
    private ProgressDialog loader;
    private boolean security = false;
    //private RefreshDeviceDashboard refreshDeviceDashboard;
    private Timer timer;
    private MyTimerTask myTimerTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_dashboard_pager_item, container, false);
        mContext = getActivity();
        loader = new ProgressDialog(mContext);
        initializeComponents(view);
        initTimer(Utils.delay05Seconds);
        return view;
    }

    @Override
    public void onClick(View view) {
        //switch case
        switch (view.getId()) {
            case R.id.llShortcutAppliance:
                Intent applianceIntent = new Intent(mContext, AppliancesActivity.class);
                applianceIntent.putExtra("SWITCH", DeviceDashboardActivity.deviceInfoModel.getSwitches());
                applianceIntent.putExtra("APPLIANCE", DeviceDashboardActivity.deviceInfoModel.getApplianceList());
                startActivity(applianceIntent);
                getActivity().overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.llShortcutScheduler:
                startActivity(new Intent(mContext, SchedulerActivity.class));
                getActivity().overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.llShortcutTimer:
                startActivity(new Intent(mContext, TimerListActivity.class));
                getActivity().overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.ivSecurityFeature:
                if(Utils.multipleTapDelayLONG())
                    return;

                if(DeviceDashboardActivity.isConnected){
                    if(CheckNetwork.isInternetAvailable(mContext)){
                        enLargeShrinkAnimation();
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.check_for_internet_connectivity),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Utils.okAlertDialog(mContext, "Device is offline, please turn it ON.");
                }
                break;
        }
    }

    /**
     * Method for initialize component of screen.
     */
    private void initializeComponents(View view) {
        tvActiveDeviceLbl = (TextView) view.findViewById(R.id.tvActiveDeviceLbl);
        tvActiveDeviceName = (TextView) view.findViewById(R.id.tvActiveDeviceName);
        tvActiveDeviceLbl.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvActiveDeviceName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvActiveDeviceName.setText(AppSPrefs.getString(Commons.CONFIGURED_DEVICE_NAME));

        //rlSecurityFeature = (ImageView) view.findViewById(R.id.rlSecurityFeature);
        llShortcutAppliance = (LinearLayout) view.findViewById(R.id.llShortcutAppliance);
        llShortcutScheduler = (LinearLayout) view.findViewById(R.id.llShortcutScheduler);
        llShortcutTimer = (LinearLayout) view.findViewById(R.id.llShortcutTimer);


        llShortcutAppliance.setOnClickListener(this);
        llShortcutScheduler.setOnClickListener(this);
        llShortcutTimer.setOnClickListener(this);

        ivSecurityFeature = (ImageView) view.findViewById(R.id.ivSecurityFeature);
        ivSecurityFeature.setOnClickListener(this);
        ivSecurityFeature.setImageResource(R.drawable.ic_security_off);
        ivSecurityFeature.setTag(false);

        tvTemperature = (TextView) view.findViewById(R.id.tvTemperature);
        tvHumidity = (TextView) view.findViewById(R.id.tvHumidity);
        tvSecurityFeature = (TextView) view.findViewById(R.id.tvSecurityFeature);
        tvTemperature.setText("Temp | "+ AppSPrefs.getTemperature() +" °C");
        tvHumidity.setText("Humidity "+AppSPrefs.getHumidity()+"%");

        tvSecurityFeature.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvTemperature.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvHumidity.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        ((TextView) view.findViewById(R.id.tvAppliances)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView) view.findViewById(R.id.tvScheduler)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView) view.findViewById(R.id.tvTimer)).setTypeface(SNApplication.APP_FONT_TYPEFACE);

        security = DeviceDashboardActivity.deviceInfoModel.isSecurity();
        ivSecurityFeature.setImageResource((security) ? (R.drawable.ic_security_on) : (R.drawable.ic_security_off));
    }

    private void callApiToggleSecurity(String value){
        //Utils.showLoader(mContext, loader);
        //Call Cloud API Request after check internet connection
        new Communicator(mContext, this, APIUtils.CMD_SECURITY_ON_OFF,
                getParams(AppSPrefs.getDeviceId(), APIUtils.CMD_SECURITY_ON_OFF, value));
    }

    private HashMap<String, String> getParams(String deviceId, String command, String switchOnOff){
        HashMap<String, String> params = new HashMap<>();
        params.put(Commons.COMMAND, command);
        params.put(Commons.CONFIGURED_DEVICE_ID, deviceId);
        params.put(Commons.PIR_STATUS, switchOnOff);
        params.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        return params;
    }

    private void getTempAndHumidity(){
        if(CheckNetwork.isInternetAvailable(mContext)){
            //Call Cloud API Request after check internet connection
            new CloudCommunicator(mContext, this, CloudUtils.CLOUD_TEMP_HUMIDITY_PREFIX,
                    getTempParams(AppSPrefs.getDeviceId(), CloudUtils.CLOUD_TEMP_HUMIDITY_PREFIX));
        } else {
            Toast.makeText(mContext, getString(R.string.check_for_internet_connectivity),
                    Toast.LENGTH_LONG).show();
        }
    }

    private HashMap<String, String> getTempParams(String deviceId, String prefix){
        HashMap<String, String> params = new HashMap<>();
        params.put(Commons.CONFIGURED_DEVICE_ID, deviceId);
        params.put(Commons.TEMP_HUMIDITY_PREFIX, prefix);
        params.put(Commons.CLOUD_EVENTS_TAG, CloudUtils.CLOUD_EVENTS);
        return params;
    }

    private String toggleSecuritySwitchUI(boolean value){
        String cmd = "";
        if (value) {
            ivSecurityFeature.setImageResource(R.drawable.ic_security_off);
            security = false;
            cmd = "off";//"l7,HIGH";
        } else {
            ivSecurityFeature.setImageResource(R.drawable.ic_security_on);
            security = true;
            cmd = "on";//"l7,LOW";
        }
        return cmd;
    }

    /**
     *
     */
    private void enLargeShrinkAnimation() {
        animationEnlarge = AnimationUtils.loadAnimation(mContext, R.anim.enlarge);
        animationShrink = AnimationUtils.loadAnimation(mContext, R.anim.shrink);
        animationEnlarge.setAnimationListener(animationEnlargeListener);
        animationShrink.setAnimationListener(animationShrinkListener);
        ivSecurityFeature.startAnimation(animationEnlarge);
    }

    /**
     *
     */
    Animation.AnimationListener animationEnlargeListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {
            ivSecurityFeature.startAnimation(animationShrink);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

    /**
     *
     */
    Animation.AnimationListener animationShrinkListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            String cmd = toggleSecuritySwitchUI(security);
            callApiToggleSecurity(cmd);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

    @Override
    public void onSuccess(String name, Object object) {
        Logger.i(TAG, "onSuccess"+" Name: "+name+"Response: " + object);
        try{
            if(CloudUtils.CLOUD_TEMP_HUMIDITY_PREFIX.equalsIgnoreCase(name)){
                JSONObject data = new JSONObject(object.toString());
                String status = data.optString("status");
                final String temperature = data.optString("temperature");
                final String humidity = data.optString("humidity");
                String ip = data.optString("ip");

                if(mContext!=null){
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!TextUtils.isEmpty(temperature)) {
                                int temp = (int)Double.parseDouble(temperature);
                                tvTemperature.setText("Temp | "+ temp +" °C");
                                AppSPrefs.setTemperature(""+temp);
                            } else {
                                tvTemperature.setText("Temp | "+ AppSPrefs.getTemperature() +" °C");
                            }

                            if(!TextUtils.isEmpty(humidity)) {
                                int hum = (int)Double.parseDouble(humidity);
                                tvHumidity.setText("Humidity "+hum+"%");
                                AppSPrefs.setHumidity(""+hum);
                            } else {
                                tvHumidity.setText("Humidity "+AppSPrefs.getHumidity()+"%");
                            }
                        }
                    });
                }
            } else {
                BaseModel model = (BaseModel) ResponseParser.parseResponse(object);
                if(model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    DeviceDashboardActivity.deviceInfoModel.setSecurity(security);
                }else{
                    Toast.makeText(mContext, model.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        Logger.i(TAG, "onFailure"+" Name: "+name+"Response: " + object);
    }

//    private class RefreshDeviceDashboard extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Logger.i(TAG, "Device Status Changed");
//            if (intent != null && intent.hasExtra("UPDATE_TEMPERATURE")) {
//                String temperature = intent.getStringExtra(Commons.TEMPERATURE);
//                String humidity = intent.getStringExtra(Commons.HUMIDITY);
//                AppSPrefs.setTemperature(temperature);
//                AppSPrefs.setHumidity(humidity);
//            }
//        }
//    }

    private void initTimer(int delayTime) {
        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, delayTime);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Logger.i(TAG, "Timer Running! Device Id: "+AppSPrefs.getDeviceId());
                    //update UI here.
                    getTempAndHumidity();
                }
            });

            initTimer(Utils.delay45Seconds);
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
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancelTimer();
    }
}
