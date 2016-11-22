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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharpnode.commons.Commons;
import com.sharpnode.setupdevice.DeviceSetupActivity;
import com.sharpnode.sprefs.AppSPrefs;

/**
 * class: HomeActivity it is dashboard screen of application from where can access all features of
 * application.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;  //toggle to open and close drawer
    private TextView tvUserName, tvUserRole, tvHome, tvSettings, tvInsights, tvIftttConfig, tvAppliances,
            tvUserManual, tvContactUs, tvLogout, tvTemperature, tvHumidity, tvSecurityFeature;
    private ImageView ivProfilePicture, ivSecurityFeature, ivAppliancesShortcut, ivSchedulerShortcut, ivLiveCameraShortcut, ivTimerShortcut;
    private LinearLayout llHomePanel, llSettingsPanel, llInsightsPanel, llIftttConfigPanel,
            llAppliancePanel, llLiveCameraPanel, llUserManualPanel, llLogoutPanel, llContactUsPanel, llDeviceManualPanel;
    private RelativeLayout rlShortcutAppliance, rlShortcutScheduler, rlShortcutLiveCamera, rlShortcutTimer, rlSecurityFeature;
    private Animation animationEnlarge, animationShrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        //switch case
        switch (view.getId()) {
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
            case R.id.llUserManualPanel:
                startActivity(new Intent(mContext, UserMannualActivity.class));
                break;
            case R.id.rlShortcutAppliance:
                startActivity(new Intent(mContext, AppliancesActivity.class));
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.rlShortcutScheduler:
                startActivity(new Intent(mContext, SchedulerActivity.class));
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.rlShortcutLiveCamera:
                startActivity(new Intent(mContext, LiveCameraActivity.class));
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.rlShortcutTimer:
                startActivity(new Intent(mContext, TimerListActivity.class));
                overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                break;
            case R.id.llContactUsPanel:
                startActivity(new Intent(mContext, ContactUsActivity.class));
                break;
            case R.id.rlSecurityFeature:
                enLargeShrinkAnimation();
                break;
            case R.id.llDeviceManualPanel:
                startActivity(new Intent(mContext, DeviceSetupActivity.class));
                break;
            case R.id.llLogoutPanel:
                AppSPrefs.clearAppSPrefs();
                Intent intent = new Intent(mContext, LandingPageActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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
        llSettingsPanel = (LinearLayout) findViewById(R.id.llSettingsPanel);
        llInsightsPanel = (LinearLayout) findViewById(R.id.llInsightsPanel);
        llIftttConfigPanel = (LinearLayout) findViewById(R.id.llIftttConfigPanel);
        llAppliancePanel = (LinearLayout) findViewById(R.id.llAppliancePanel);
        llUserManualPanel = (LinearLayout) findViewById(R.id.llUserManualPanel);
        llLogoutPanel = (LinearLayout) findViewById(R.id.llLogoutPanel);
        llContactUsPanel = (LinearLayout) findViewById(R.id.llContactUsPanel);
        llDeviceManualPanel = (LinearLayout) findViewById(R.id.llDeviceManualPanel);

        rlSecurityFeature = (RelativeLayout) findViewById(R.id.rlSecurityFeature);
        rlShortcutAppliance = (RelativeLayout) findViewById(R.id.rlShortcutAppliance);
        rlShortcutScheduler = (RelativeLayout) findViewById(R.id.rlShortcutScheduler);
        rlShortcutLiveCamera = (RelativeLayout) findViewById(R.id.rlShortcutLiveCamera);
        rlShortcutTimer = (RelativeLayout) findViewById(R.id.rlShortcutTimer);

        rlSecurityFeature.setOnClickListener(this);
        rlShortcutAppliance.setOnClickListener(this);
        rlShortcutScheduler.setOnClickListener(this);
        rlShortcutLiveCamera.setOnClickListener(this);
        rlShortcutTimer.setOnClickListener(this);

        llHomePanel.setOnClickListener(this);
        llSettingsPanel.setOnClickListener(this);
        llInsightsPanel.setOnClickListener(this);
        llIftttConfigPanel.setOnClickListener(this);
        llAppliancePanel.setOnClickListener(this);
        llUserManualPanel.setOnClickListener(this);
        llLogoutPanel.setOnClickListener(this);
        llContactUsPanel.setOnClickListener(this);
        llDeviceManualPanel.setOnClickListener(this);

        ivSecurityFeature = (ImageView) findViewById(R.id.ivSecurityFeature);
        ivSecurityFeature.setImageResource(R.drawable.ic_security_off);
        ivSecurityFeature.setTag(false);

        tvHome = (TextView) findViewById(R.id.tvHome);
        tvSettings = (TextView) findViewById(R.id.tvSettings);
        tvInsights = (TextView) findViewById(R.id.tvInsights);
        tvIftttConfig = (TextView) findViewById(R.id.tvIftttConfig);
        tvAppliances = (TextView) findViewById(R.id.tvAppliances);
        tvUserManual = (TextView) findViewById(R.id.tvUserManual);
        tvContactUs = (TextView) findViewById(R.id.tvContactUs);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        tvHumidity = (TextView) findViewById(R.id.tvHumidity);

        ivAppliancesShortcut = (ImageView) findViewById(R.id.ivAppliancesShortcut);
        ivSchedulerShortcut = (ImageView) findViewById(R.id.ivSchedulerShortcut);
        ivLiveCameraShortcut = (ImageView) findViewById(R.id.ivLiveCameraShortcut);
        ivTimerShortcut = (ImageView) findViewById(R.id.ivTimerShortcut);

        ((TextView) findViewById(R.id.tvSecurityFeature)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvTemperature.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvHumidity.setTypeface(SNApplication.APP_FONT_TYPEFACE);

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
}
