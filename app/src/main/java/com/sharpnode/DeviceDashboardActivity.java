package com.sharpnode;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.adapter.DeviceDashboardPagerAdaper;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.cloudcommunication.CloudCommunicator;
import com.sharpnode.cloudcommunication.CloudResponseParser;
import com.sharpnode.cloudcommunication.CloudUtils;
import com.sharpnode.commons.Commons;
import com.sharpnode.context.ContextHelper;
import com.sharpnode.model.DeviceInfoModel;
import com.sharpnode.model.DeviceModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.setupdevice.MyDevicesActivity;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * class: HomeActivity it is dashboard screen of application from where can access all features of
 * application.
 */
public class DeviceDashboardActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak{

    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;  //toggle to open and close drawer
    private TextView tvUserName, tvUserRole, tvHome, tvWidgetSetting ,tvMyDevices, tvInsights, tvIftttConfig,
            tvUserManual, tvContactUs, tvLogout;
    private ImageView ivProfilePicture;
    private LinearLayout llHomePanel, llWidgetSettingsPanel, llInsightsPanel, llIftttConfigPanel,
            llAppliancePanel, llDeviceManualPanel,
            llUserManualPanel, llLogoutPanel, llContactUsPanel;
    public static DeviceInfoModel deviceInfoModel=null;

    private ProgressDialog loader;
    public static boolean isConnected = false;
    private ViewPager mPager;
    private DeviceDashboardPagerAdaper mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_dashboard);
        ContextHelper.setContext(this);
        try{
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.app_name));
            Utils.setTitleFontTypeface(toolbar);
            mContext = this;

            //initialize drawer items
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                @Override
                public void onDrawerClosed(View drawerView) {
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    ivProfilePicture.setImageBitmap(Utils.getBitmapFromBase64(AppSPrefs.getString(Commons.PHOTO)));
                }
            };

            drawer.setDrawerListener(toggle);
            toggle.syncState();
            toggle.onConfigurationChanged(new Configuration());

            loader = new ProgressDialog(mContext);
            String deviceInfoData = getIntent().getStringExtra("DEVICE_INFO");
            deviceInfoModel = ResponseParser.parseDeviceInfoResponse(deviceInfoData);
            isConnected = CloudUtils.deviceStatus.get(deviceInfoModel.getDeviceId().toLowerCase());
            AppSPrefs.setWidgetDevice(deviceInfoModel.getDeviceName());
            // Instantiate a ViewPager and a PagerAdapter.
            mPager = (ViewPager) findViewById(R.id.pager);
            mPagerAdapter = new DeviceDashboardPagerAdaper(mContext, getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);

            initializeComponents();
            initHeaderComponents();
        }catch (Exception e){
            e.printStackTrace();
            finish();
        }

    }


//        @Override
//    protected void onResume() {
//        super.onResume();
//        try{
//            registerReceiver(deviceStatusChangedReceiver, new IntentFilter(CloudUtils.REFRESH_DEVICE_DASHBOARD));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        try{
//            unregisterReceiver(deviceStatusChangedReceiver);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onClick(View view) {
        closeDrawer();

        switch (view.getId()) {
            case R.id.llHomePanel:
                Intent goToHomeIntent = new Intent(getApplicationContext(), HomeDashboardActivity.class);
                startActivity(goToHomeIntent);
                finish();
                break;
            case R.id.llWidgetSettingsPanel:
                startActivity(new Intent(mContext, AppWidgetSettings.class));
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.llMyDevices:
                Intent intent = new Intent(HomeDashboardActivity.homeActivity, MyDevicesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                HomeDashboardActivity.homeActivity.startActivity(intent);
                finish();
                break;
            case R.id.llInsightsPanel:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(mContext, InsightsActivity.class));
                        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                    }
                }, 100);
                break;
            case R.id.llUserManualPanel:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(mContext, UserMannualActivity.class));
                        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                    }
                }, 100);
                break;
            case R.id.llContactUsPanel:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(mContext, ContactUsActivity.class));
                        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                    }
                }, 100);
                break;
            case R.id.llLogoutPanel:
                Utils.logoutFromApp(DeviceDashboardActivity.this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Method for initialize component of screen.
     */
    private void initializeComponents() {
        //Left panel_new components
        llHomePanel = (LinearLayout) findViewById(R.id.llHomePanel);
        llWidgetSettingsPanel = (LinearLayout) findViewById(R.id.llWidgetSettingsPanel);
        llInsightsPanel = (LinearLayout) findViewById(R.id.llInsightsPanel);
        llIftttConfigPanel = (LinearLayout) findViewById(R.id.llIftttConfigPanel);
        //llAppliancePanel = (LinearLayout) findViewById(R.id.llAppliancePanel);
        llDeviceManualPanel = (LinearLayout) findViewById(R.id.llMyDevices);
        llUserManualPanel = (LinearLayout) findViewById(R.id.llUserManualPanel);
        llLogoutPanel = (LinearLayout) findViewById(R.id.llLogoutPanel);
        llContactUsPanel = (LinearLayout) findViewById(R.id.llContactUsPanel);

        llHomePanel.setOnClickListener(this);
        llWidgetSettingsPanel.setOnClickListener(this);
        llInsightsPanel.setOnClickListener(this);
        llIftttConfigPanel.setOnClickListener(this);
        //llAppliancePanel.setOnClickListener(this);
        llDeviceManualPanel.setOnClickListener(this);
        llLogoutPanel.setOnClickListener(this);
        llContactUsPanel.setOnClickListener(this);
        llUserManualPanel.setOnClickListener(this);

        tvHome = (TextView) findViewById(R.id.tvHome);
        tvWidgetSetting = (TextView) findViewById(R.id.tvAppWidgetSetting);
        tvMyDevices = (TextView) findViewById(R.id.tvMyDevices);
        tvInsights = (TextView) findViewById(R.id.tvInsights);
        tvIftttConfig = (TextView) findViewById(R.id.tvIftttConfig);
        tvUserManual = (TextView) findViewById(R.id.tvUserManual);
        tvContactUs = (TextView) findViewById(R.id.tvContactUs);
        tvLogout = (TextView) findViewById(R.id.tvLogout);

        tvWidgetSetting.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvMyDevices.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvInsights.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvIftttConfig.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvUserManual.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvContactUs.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvLogout.setTypeface(SNApplication.APP_FONT_TYPEFACE);
    }

    /**
     * Method to initialize header options of side panel_new
     */
    private void initHeaderComponents() {
        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserRole = (TextView) findViewById(R.id.tvUserRole);

        tvUserName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvUserRole.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvUserRole.setText(AppSPrefs.getString(Commons.EMAIL));
        tvUserName.setText(AppSPrefs.getString(Commons.NAME));

        ivProfilePicture.setImageBitmap(Utils.getBitmapFromBase64(AppSPrefs.getString(Commons.PHOTO)));
    }

    /**
     * Method to close Close Drawer
     */
    private void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onSuccess(String name, Object object) {
        Utils.dismissLoader();
        //Logger.i(TAG, name+", onSuccess, Response: " + object);
        try {
            if(CloudUtils.CLOUD_FUNCTION_DEVICE_STATUS.equalsIgnoreCase(name)){
               DeviceModel model = CloudResponseParser.parseDeviceStatusResponse(object);
                if (model.getResponseCode() == Commons.CODE_200) {
                    CloudUtils.deviceStatus.put(model.getDevice_id().toLowerCase(), model.isDeviceStatus());
                    sendBroadcast(new Intent());
                }
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
}
