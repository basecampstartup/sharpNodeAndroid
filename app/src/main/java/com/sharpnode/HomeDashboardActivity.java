package com.sharpnode;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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

import com.sharpnode.commons.Commons;
import com.sharpnode.setupdevice.MyDevicesActivity;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Utils;

import java.lang.reflect.Field;

/**
 * class: HomeActivity it is dashboard screen of application from where can access all features of
 * application.
 */
public class HomeDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    public static HomeDashboardActivity homeActivity = null;
    private Context mContext;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;  //toggle to open and close drawer
    private TextView tvUserName, tvUserRole, tvHome, tvSettings, tvLiveCameraShortcut, tvControlShortcut, tvRemoteShortcut,
            tvUserManual, tvContactUs, tvLogout, tvTemperature, tvHumidity, tvSecurityFeature;
    private ImageView ivProfilePicture, ivSecurityFeature, ivAppliancesShortcut, ivSchedulerShortcut, ivLiveCameraShortcut, ivTimerShortcut;
    private LinearLayout llHomePanel, llSettingsPanel, llRemote,
            llControl, llCamera, llUserManualPanel, llLogoutPanel, llContactUsPanel, llDeviceManualPanel;
    private RelativeLayout rlShortcutAppliance, rlShortcutScheduler, rlShortcutLiveCamera, rlShortcutTimer, rlSecurityFeature;
    private Animation animationEnlarge, animationShrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard);
        homeActivity = this;
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        closeDrawer();
        //switch case
        switch (view.getId()) {
            case R.id.llSettingsPanel:
                startActivity(new Intent(mContext, AccountSettingsActivity.class));
                break;
            case R.id.llCamera:
                startActivity(new Intent(mContext, LiveCameraActivity.class));
                //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.llControl:
                startActivity(new Intent(mContext, MyDevicesActivity.class));
                //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.llRemote:
                //startActivity(new Intent(mContext, SchedulerActivity.class));
                //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.llUserManualPanel:
                startActivity(new Intent(mContext, UserMannualActivity.class));
                break;
            case R.id.llContactUsPanel:
                startActivity(new Intent(mContext, ContactUsActivity.class));
                break;
            case R.id.llLogoutPanel:
                Utils.logoutFromApp(HomeDashboardActivity.this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            Utils.exitFromApp(HomeDashboardActivity.this);
        }
    }

    /**
     * Method for initialize component of screen.
     */
    private void initializeComponents() {
        //llHomePanel = (LinearLayout) findViewById(R.id.llHomePanel);
        llSettingsPanel = (LinearLayout) findViewById(R.id.llSettingsPanel);
        llUserManualPanel = (LinearLayout) findViewById(R.id.llUserManualPanel);
        llLogoutPanel = (LinearLayout) findViewById(R.id.llLogoutPanel);
        llContactUsPanel = (LinearLayout) findViewById(R.id.llContactUsPanel);

        llRemote = (LinearLayout) findViewById(R.id.llRemote);
        llCamera = (LinearLayout) findViewById(R.id.llCamera);
        llControl = (LinearLayout) findViewById(R.id.llControl);

        llSettingsPanel.setOnClickListener(this);
        llCamera.setOnClickListener(this);
        llControl.setOnClickListener(this);
        llRemote.setOnClickListener(this);
        llUserManualPanel.setOnClickListener(this);
        llLogoutPanel.setOnClickListener(this);
        llContactUsPanel.setOnClickListener(this);

        tvSettings = (TextView) findViewById(R.id.tvSettings);
        tvUserManual = (TextView) findViewById(R.id.tvUserManual);
        tvContactUs = (TextView) findViewById(R.id.tvContactUs);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        tvLiveCameraShortcut = (TextView) findViewById(R.id.tvLiveCameraShortcut);
        tvControlShortcut = (TextView) findViewById(R.id.tvControlShortcut);
        tvRemoteShortcut = (TextView) findViewById(R.id.tvRemoteShortcut);

        ivLiveCameraShortcut = (ImageView) findViewById(R.id.ivLiveCameraShortcut);

        tvSettings.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvLiveCameraShortcut.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvControlShortcut.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvRemoteShortcut.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvUserManual.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvContactUs.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvLogout.setTypeface(SNApplication.APP_FONT_TYPEFACE);
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

    /**
     * Method to close Close Drawer
     */
    private void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
