package com.sharpnode;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;  //toggle to open and close drawer
    private TextView tvUserName, tvUserRole;
    private ImageView ivProfilePicture;
    private LinearLayout llHomePanel, llSettingsPanel, llInsightsPanel, llIftttConfigPanel,
            llAppliancePanel, llLiveCameraPanel, llUserManualPanel, llLogoutPanel;

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

        init();
        initHeaderItems();
    }

    @Override
    public void onClick(View view) {
        closeDrawer();

        switch (view.getId()){
            case R.id.llHomePanel:
                break;
            case R.id.llSettingsPanel:
                break;
            case R.id.llInsightsPanel:
                break;
            case R.id.llIftttConfigPanel:
                break;
            case R.id.llAppliancePanel:
                break;
            case R.id.llLiveCameraPanel:
                break;
            case R.id.llUserManualPanel:
                break;
            case R.id.llLogoutPanel:
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void init(){
        llHomePanel = (LinearLayout)findViewById(R.id.llHomePanel);
        llSettingsPanel = (LinearLayout)findViewById(R.id.llSettingsPanel);
        llInsightsPanel = (LinearLayout)findViewById(R.id.llInsightsPanel);
        llIftttConfigPanel = (LinearLayout)findViewById(R.id.llIftttConfigPanel);
        llAppliancePanel = (LinearLayout)findViewById(R.id.llAppliancePanel);
        llLiveCameraPanel = (LinearLayout)findViewById(R.id.llLiveCameraPanel);
        llUserManualPanel = (LinearLayout)findViewById(R.id.llUserManualPanel);
        llLogoutPanel = (LinearLayout)findViewById(R.id.llLogoutPanel);

        llHomePanel.setOnClickListener(this);
        llSettingsPanel.setOnClickListener(this);
        llInsightsPanel.setOnClickListener(this);
        llIftttConfigPanel.setOnClickListener(this);
        llAppliancePanel.setOnClickListener(this);
        llLiveCameraPanel.setOnClickListener(this);
        llUserManualPanel.setOnClickListener(this);
        llLogoutPanel.setOnClickListener(this);
    }
    /**
     * Method to initialize heaer options of side panel
     */
    private void initHeaderItems() {
        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserRole = (TextView) findViewById(R.id.tvUserRole);
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
