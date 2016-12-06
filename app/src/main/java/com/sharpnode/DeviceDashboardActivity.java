package com.sharpnode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.setupdevice.MyDevicesActivity;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * class: HomeActivity it is dashboard screen of application from where can access all features of
 * application.
 */
public class DeviceDashboardActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak{

    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;  //toggle to open and close drawer
    private TextView tvUserName, tvUserRole, tvHome, tvMyDevices, tvInsights, tvIftttConfig, tvAppliances,
            tvUserManual, tvContactUs, tvLogout, tvTemperature, tvHumidity, tvSecurityFeature;
    private ImageView ivProfilePicture, ivSecurityFeature;
    private LinearLayout llHomePanel, llSettingsPanel, llInsightsPanel, llIftttConfigPanel,
            llAppliancePanel, llDeviceManualPanel, llShortcutAppliance, llShortcutScheduler, llShortcutTimer,
            llUserManualPanel, llLogoutPanel, llContactUsPanel;
    private RelativeLayout rlSecurityFeature;
    private Animation animationEnlarge, animationShrink;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_dashboard);

        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        loader.setCancelable(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        //Set Custom font to title.
        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView titleText = (TextView) f.get(toolbar);
            titleText.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {

        }
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
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.onConfigurationChanged(new Configuration());

        initializeComponents();
        initHeaderComponents();
        getTempAndHumidity();
    }

    @Override
    public void onClick(View view) {
        closeDrawer();
        //switch case
        switch (view.getId()) {
            case R.id.llHomePanel:
                Intent goToHomeIntent = new Intent(getApplicationContext(), HomeDashboardActivity.class);
                startActivity(goToHomeIntent);
                finish();
                break;
            case R.id.llMyDevices:
                Intent intent = new Intent(HomeDashboardActivity.homeActivity, MyDevicesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                HomeDashboardActivity.homeActivity.startActivity(intent);
                finish();
                break;
            case R.id.llInsightsPanel:
                startActivity(new Intent(mContext, InsightsActivity.class));
                break;
            case R.id.llAppliancePanel:
                startActivity(new Intent(mContext, AppliancesActivity.class));
                break;
            case R.id.llShortcutAppliance:
                startActivity(new Intent(mContext, AppliancesActivity.class));
                //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.llShortcutScheduler:
                startActivity(new Intent(mContext, SchedulerActivity.class));
                //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.llShortcutTimer:
                startActivity(new Intent(mContext, TimerListActivity.class));
                //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.rlSecurityFeature:
                enLargeShrinkAnimation();
                break;
            case R.id.llUserManualPanel:
                startActivity(new Intent(mContext, UserMannualActivity.class));
                break;
            case R.id.llContactUsPanel:
                startActivity(new Intent(mContext, ContactUsActivity.class));
                break;
            case R.id.llLogoutPanel:
                Utils.logoutFromApp(DeviceDashboardActivity.this);
                break;
        }
    }

    private void enLargeShrinkAnimation() {
        animationEnlarge = AnimationUtils.loadAnimation(this, R.anim.enlarge);
        animationShrink = AnimationUtils.loadAnimation(this, R.anim.shrink);
        animationEnlarge.setAnimationListener(animationEnlargeListener);
        animationShrink.setAnimationListener(animationShrinkListener);
        ivSecurityFeature.startAnimation(animationEnlarge);
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
        llHomePanel = (LinearLayout) findViewById(R.id.llHomePanel);
        llInsightsPanel = (LinearLayout) findViewById(R.id.llInsightsPanel);
        llIftttConfigPanel = (LinearLayout) findViewById(R.id.llIftttConfigPanel);
        llAppliancePanel = (LinearLayout) findViewById(R.id.llAppliancePanel);
        llDeviceManualPanel = (LinearLayout) findViewById(R.id.llMyDevices);
        llUserManualPanel = (LinearLayout) findViewById(R.id.llUserManualPanel);
        llLogoutPanel = (LinearLayout) findViewById(R.id.llLogoutPanel);
        llContactUsPanel = (LinearLayout) findViewById(R.id.llContactUsPanel);

        rlSecurityFeature = (RelativeLayout) findViewById(R.id.rlSecurityFeature);
        llShortcutAppliance = (LinearLayout) findViewById(R.id.llShortcutAppliance);
        llShortcutScheduler = (LinearLayout) findViewById(R.id.llShortcutScheduler);
        llShortcutTimer = (LinearLayout) findViewById(R.id.llShortcutTimer);

        rlSecurityFeature.setOnClickListener(this);
        llShortcutAppliance.setOnClickListener(this);
        llShortcutScheduler.setOnClickListener(this);
        llShortcutTimer.setOnClickListener(this);

        llHomePanel.setOnClickListener(this);
        llInsightsPanel.setOnClickListener(this);
        llIftttConfigPanel.setOnClickListener(this);
        llAppliancePanel.setOnClickListener(this);
        llDeviceManualPanel.setOnClickListener(this);
        llLogoutPanel.setOnClickListener(this);
        llContactUsPanel.setOnClickListener(this);

        ivSecurityFeature = (ImageView) findViewById(R.id.ivSecurityFeature);
        ivSecurityFeature.setImageResource(R.drawable.ic_security_off);
        ivSecurityFeature.setTag(false);

        tvHome = (TextView) findViewById(R.id.tvHome);
        tvMyDevices = (TextView) findViewById(R.id.tvMyDevices);
        tvInsights = (TextView) findViewById(R.id.tvInsights);
        tvIftttConfig = (TextView) findViewById(R.id.tvIftttConfig);
        tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        tvUserManual = (TextView) findViewById(R.id.tvUserManual);
        tvContactUs = (TextView) findViewById(R.id.tvContactUs);

        ((TextView) findViewById(R.id.tvSecurityFeature)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvTemperature.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvHumidity.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        tvHome.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvMyDevices.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvInsights.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvIftttConfig.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvUserManual.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvContactUs.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvLogout.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        ((TextView)findViewById(R.id.tvAppliances)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView)findViewById(R.id.tvScheduler)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView)findViewById(R.id.tvTimer)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
    }

    /**
     * Method to initialize header options of side panel
     */
    private void initHeaderComponents() {
        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserRole = (TextView) findViewById(R.id.tvUserRole);

        tvUserName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvUserRole.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        tvUserName.setText(AppSPrefs.getString(Commons.NAME));
    }

    private void getTempAndHumidity(){
        if(CheckNetwork.isInternetAvailable(mContext)){
            showLoader();
            //Call Cloud API Request after check internet connection
            new CloudCommunicator(mContext, CloudUtils.CLOUD_TEMP_HUMIDITY_PREFIX,
                    getParams(AppSPrefs.getDeviceId(), CloudUtils.CLOUD_TEMP_HUMIDITY_PREFIX));
        } else {
            Toast.makeText(mContext, getString(R.string.check_for_internet_connectivity),
                    Toast.LENGTH_LONG).show();
        }
    }

    private HashMap<String, String> getParams(String deviceId, String prefix){
        HashMap<String, String> params = new HashMap<>();
        params.put(Commons.CONFIGURED_DEVICE_ID, deviceId);
        params.put(Commons.TEMP_HUMIDITY_PREFIX, prefix);
        params.put(Commons.CLOUD_EVENTS_TAG, CloudUtils.CLOUD_EVENTS);
        return params;
    }

    /**
     * Method to close Close Drawer
     */
    private void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    Animation.AnimationListener animationEnlargeListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {
            ivSecurityFeature.startAnimation(animationShrink);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }
    };
    Animation.AnimationListener animationShrinkListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            if ((boolean) ivSecurityFeature.getTag()) {
                ivSecurityFeature.setImageResource(R.drawable.ic_security_off);
                ivSecurityFeature.setTag(false);
            } else {
                ivSecurityFeature.setImageResource(R.drawable.ic_security_on);
                ivSecurityFeature.setTag(true);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }
    };

    @Override
    public void onSuccess(String name, Object object) {
        dismissLoader();
        Logger.i(TAG, name+", onSuccess, Response: " + object);
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        dismissLoader();
        Logger.i(TAG, name+", onFailure, Response: " + object);
    }

    private void showLoader(){
        try {
            if (loader != null && !loader.isShowing())
                loader.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissLoader(){
        try {
            if (loader != null)
                loader.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
