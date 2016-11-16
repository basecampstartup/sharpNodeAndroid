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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * class: HomeActivity it is dashboard screen of application from where can access all features of
 * application.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;  //toggle to open and close drawer
    private TextView tvUserName, tvUserRole, tvHome, tvSettings, tvInsights, tvIftttConfig, tvAppliances,
            tvUserManual, tvContactUs, tvLogout;
    private ImageView ivProfilePicture,imgSecurity;
    private LinearLayout llHomePanel, llSettingsPanel, llInsightsPanel, llIftttConfigPanel,
            llAppliancePanel, llLiveCameraPanel, llUserManualPanel, llLogoutPanel;
    private LinearLayout llShortcutAppliance, llShortcutScheduler, llShortcutSettings, llShortcutTimer,llContactUsPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundResource(android.R.color.transparent);
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
    public void onClick(View view) {
        closeDrawer();

        switch (view.getId()){
            case R.id.llHomePanel:
                break;
            case R.id.llSettingsPanel:
                startActivity(new Intent(mContext, AccountSettingsActivity.class));
                break;
            case R.id.llInsightsPanel:
                startActivity(new Intent(mContext, InsightsActivity.class));
                break;
            case R.id.llIftttConfigPanel:
                startActivity(new Intent(mContext, IFTTTConfigActivity.class));
                break;
            case R.id.llAppliancePanel:
                startActivity(new Intent(mContext, AppliancesActivity.class));
                break;
            /*case R.id.llLiveCameraPanel:
                startActivity(new Intent(mContext, LiveCameraActivity.class));
                break;*/
            case R.id.llUserManualPanel:
                startActivity(new Intent(mContext, UserMannualActivity.class));
                break;
            case R.id.llShortcutAppliance:
                startActivity(new Intent(mContext, AppliancesActivity.class));
                break;
            case R.id.llShortcutScheduler:
                startActivity(new Intent(mContext, SchedulerActivity.class));
                break;
            case R.id.llShortcutSettings:
                startActivity(new Intent(mContext, AccountSettingsActivity.class));
                break;
            case R.id.llShortcutTimer:
                startActivity(new Intent(mContext, TimerListActivity.class));
                break;
            case R.id.llLogoutPanel:
                finish();
                break;
            case R.id.llContactUsPanel:
                startActivity(new Intent(mContext, ContactUsActivity.class));
                break;
            case R.id.ivSecurity:
                Toast.makeText(mContext,"Security click",Toast.LENGTH_LONG).show();
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
    private void initializeComponents(){
        llHomePanel = (LinearLayout)findViewById(R.id.llHomePanel);
        llSettingsPanel = (LinearLayout)findViewById(R.id.llSettingsPanel);
        llInsightsPanel = (LinearLayout)findViewById(R.id.llInsightsPanel);
        llIftttConfigPanel = (LinearLayout)findViewById(R.id.llIftttConfigPanel);
        llAppliancePanel = (LinearLayout)findViewById(R.id.llAppliancePanel);
       // llLiveCameraPanel = (LinearLayout)findViewById(R.id.llLiveCameraPanel);
        llUserManualPanel = (LinearLayout)findViewById(R.id.llUserManualPanel);
        llLogoutPanel = (LinearLayout)findViewById(R.id.llLogoutPanel);
        llContactUsPanel = (LinearLayout)findViewById(R.id.llContactUsPanel);
        llShortcutAppliance = (LinearLayout)findViewById(R.id.llShortcutAppliance);
        llShortcutScheduler = (LinearLayout)findViewById(R.id.llShortcutScheduler);
        llShortcutSettings = (LinearLayout)findViewById(R.id.llShortcutSettings);
        llShortcutTimer = (LinearLayout)findViewById(R.id.llShortcutTimer);
        imgSecurity=(ImageView)findViewById(R.id.ivSecurity);
        llShortcutAppliance.setOnClickListener(this);
        llShortcutScheduler.setOnClickListener(this);
        llShortcutSettings.setOnClickListener(this);
        llShortcutTimer.setOnClickListener(this);

        llHomePanel.setOnClickListener(this);
        llSettingsPanel.setOnClickListener(this);
        llInsightsPanel.setOnClickListener(this);
        llIftttConfigPanel.setOnClickListener(this);
        llAppliancePanel.setOnClickListener(this);
       // llLiveCameraPanel.setOnClickListener(this);
        llUserManualPanel.setOnClickListener(this);
        llLogoutPanel.setOnClickListener(this);
        imgSecurity.setOnClickListener(this);
        llContactUsPanel.setOnClickListener(this);

        tvHome = (TextView)findViewById(R.id.tvHome);
        tvSettings = (TextView)findViewById(R.id.tvSettings);
        tvInsights = (TextView)findViewById(R.id.tvInsights);
        tvIftttConfig = (TextView)findViewById(R.id.tvIftttConfig);
        tvAppliances = (TextView)findViewById(R.id.tvAppliances);
        tvUserManual = (TextView)findViewById(R.id.tvUserManual);
        tvContactUs = (TextView)findViewById(R.id.tvContactUs);
        tvLogout = (TextView)findViewById(R.id.tvLogout);

        tvHome.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvSettings.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvInsights.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvIftttConfig.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvAppliances.setTypeface(SNApplication.APP_FONT_TYPEFACE);
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
