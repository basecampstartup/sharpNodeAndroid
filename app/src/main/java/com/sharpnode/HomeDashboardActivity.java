package com.sharpnode;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.adapter.ApplianceListAdapter;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.cloudcommunication.CloudUtils;
import com.sharpnode.commons.Commons;
import com.sharpnode.context.ContextHelper;
import com.sharpnode.model.ApplianceModel;
import com.sharpnode.model.BaseModel;
import com.sharpnode.model.ConfiguredDevices;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * class: HomeActivity it is dashboard screen of application from where can access all features of
 * application.
 */
public class HomeDashboardActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak {
    private String TAG = getClass().getSimpleName();
    public static HomeDashboardActivity homeActivity = null;
    private Context mContext;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;  //toggle to open and close drawer
    private TextView tvUserName, tvUserRole, tvSettings, tvWidgetSetting, tvLiveCameraShortcut, tvControlShortcut, tvRemoteShortcut,
            tvUserManual, tvContactUs, tvLogout;
    private ImageView ivProfilePicture, ivLiveCameraShortcut;
    private LinearLayout llSettingsPanel, llWidgetSettingsPanel, llRemote,
            llControl, llCamera, llUserManualPanel, llLogoutPanel, llContactUsPanel;
    private Timer timer;
    private MyTimerTask myTimerTask;
    private RecyclerView rvAppliances;
    private LinearLayoutManager mLayoutManager;
    private String switches = "";

    private void prepareApplianceList(ArrayList<ApplianceModel> applianceList){
        WidgetUtils.setWidgetAppliances(applianceList);
        int[] ids = AppWidgetManager.getInstance(mContext).getAppWidgetIds(new ComponentName(mContext,
                SharpNodeAppWidget.class));
        //onUpdate(context, AppWidgetManager.getInstance(context), ids);
        Intent updateIntent = new Intent(mContext, SharpNodeAppWidget.class);
        updateIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        mContext.sendBroadcast(updateIntent);

//        rvAppliances = (RecyclerView) findViewById(R.id.rvAppliances);
//
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        rvAppliances.setHasFixedSize(true);
//
//        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        rvAppliances.setLayoutManager(mLayoutManager);
//
//        if(mAdapter==null){
//            // specify an adapter (see also next example)
//            mAdapter = new ApplianceListAdapter(mContext, applianceList);
//            rvAppliances.setAdapter(mAdapter);
//        } else {
//            mAdapter.setData(applianceList);
//            mAdapter.notifyDataSetChanged();
//        }
    }

    private ArrayList<ApplianceModel> getApplianceList(ArrayList<String> appliances){
        char c[] = switches.toCharArray();
        ApplianceModel model = null;
        ArrayList<ApplianceModel> list = new ArrayList<>();
        if(switches.length() == appliances.size()){
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
        }
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard);
        ContextHelper.setContext(this);
        homeActivity = this;
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        Utils.setTitleFontTypeface(mToolbar);
        mContext = this;
        //initialize drawer items
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
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

        initializeComponents();
        initHeaderComponents();
        initTimer(Utils.delay05Seconds);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                startActivity(new Intent(mContext, NotificationActivity.class));
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(final View view) {
        closeDrawer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //switch case
                switch (view.getId()) {
                    case R.id.llSettingsPanel:
                        startActivity(new Intent(mContext, AccountSettingsActivity.class));
                        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                        break;
                    case R.id.llWidgetSettingsPanel:
                        startActivity(new Intent(mContext, AppWidgetSettings.class));
                        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                        break;
                    case R.id.llCamera:
                        startActivity(new Intent(mContext, LiveCameraActivity.class));
                        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                        break;
                    case R.id.llControl:
                        startActivity(new Intent(mContext, MyDevicesActivity.class));
                        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                        break;
                    case R.id.llRemote:
                        //startActivity(new Intent(mContext, SchedulerActivity.class));
                        //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                        break;
                    case R.id.llUserManualPanel:
                        startActivity(new Intent(mContext, UserMannualActivity.class));
                        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                        break;
                    case R.id.llContactUsPanel:
                        startActivity(new Intent(mContext, ContactUsActivity.class));
                        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                        break;
                    case R.id.llLogoutPanel:
                        Utils.logoutFromApp(HomeDashboardActivity.this);
                        break;
                }
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            //Utils.exitFromApp(HomeDashboardActivity.this);
        }
    }

    /**
     * Method for initialize component of screen.
     */
    private void initializeComponents() {
        //llHomePanel = (LinearLayout) findViewById(R.id.llHomePanel);
        llSettingsPanel = (LinearLayout) findViewById(R.id.llSettingsPanel);
        llWidgetSettingsPanel = (LinearLayout) findViewById(R.id.llWidgetSettingsPanel);
        llUserManualPanel = (LinearLayout) findViewById(R.id.llUserManualPanel);
        llLogoutPanel = (LinearLayout) findViewById(R.id.llLogoutPanel);
        llContactUsPanel = (LinearLayout) findViewById(R.id.llContactUsPanel);

        llRemote = (LinearLayout) findViewById(R.id.llRemote);
        llCamera = (LinearLayout) findViewById(R.id.llCamera);
        llControl = (LinearLayout) findViewById(R.id.llControl);

        llSettingsPanel.setOnClickListener(this);
        llWidgetSettingsPanel.setOnClickListener(this);
        llCamera.setOnClickListener(this);
        llControl.setOnClickListener(this);
        llRemote.setOnClickListener(this);
        llUserManualPanel.setOnClickListener(this);
        llLogoutPanel.setOnClickListener(this);
        llContactUsPanel.setOnClickListener(this);

        tvSettings = (TextView) findViewById(R.id.tvSettings);
        tvWidgetSetting = (TextView) findViewById(R.id.tvWidgetSetting);
        tvUserManual = (TextView) findViewById(R.id.tvUserManual);
        tvContactUs = (TextView) findViewById(R.id.tvContactUs);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        tvLiveCameraShortcut = (TextView) findViewById(R.id.tvLiveCameraShortcut);
        tvControlShortcut = (TextView) findViewById(R.id.tvControlShortcut);
        tvRemoteShortcut = (TextView) findViewById(R.id.tvRemoteShortcut);

        ivLiveCameraShortcut = (ImageView) findViewById(R.id.ivLiveCameraShortcut);

        tvSettings.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvWidgetSetting.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvLiveCameraShortcut.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvControlShortcut.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvRemoteShortcut.setTypeface(SNApplication.APP_FONT_TYPEFACE);
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
        tvUserRole.setSelected(true);
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
    protected void onResume() {
        super.onResume();
        initHeaderComponents();
    }

    private void getWidgetDevice(){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            //Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_FAV_DEVICE,
                    getRequestMap(APIUtils.CMD_FAV_DEVICE, AppSPrefs.getString(Commons.ACCESS_TOKEN)));
        } else {
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
        map.put(Commons.ACCESS_TOKEN, accessToken);
        return map;
    }

    @Override
    public void onSuccess(String name, Object object) {
        try {
            if (APIUtils.CMD_FAV_DEVICE.equalsIgnoreCase(name)) {
                Logger.i(TAG, "onSuccess"+" Name: "+name+" Response: " + object);
                DeviceInfoModel model = ResponseParser.parseGetFavDevicesResponse(object);
                if (model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    switches = model.getSwitches();
                    Utils.arrAppliances = (String[]) model.getApplianceList().toArray(new String[model.getApplianceList().size()]);
                    prepareApplianceList(getApplianceList(model.getApplianceList()));
                }
                initTimer(Utils.delay30Seconds);
            } else if(CloudUtils.CLOUD_FUNCTION_LED.equalsIgnoreCase(name)){
                int[] ids = AppWidgetManager.getInstance(mContext).getAppWidgetIds(new ComponentName(mContext,
                        SharpNodeAppWidget.class));
                //onUpdate(context, AppWidgetManager.getInstance(context), ids);
                Intent updateIntent = new Intent(mContext, SharpNodeAppWidget.class);
                updateIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
                mContext.sendBroadcast(updateIntent);
            }

        } catch (Exception e) {
            e.printStackTrace();
            initTimer(Utils.delay30Seconds);
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        Logger.i(TAG, "onFailure"+" Name: "+name+" Response: " + object);

        if(APIUtils.CMD_FAV_DEVICE.equalsIgnoreCase(name)){
            initTimer(Utils.delay30Seconds);
        }
    }

    private void initTimer(int delayTime) {
        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, delayTime);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            getWidgetDevice();
            /*runOnUiThread(new Runnable() {

                @Override
                public void run() {
                }
            });*/
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
