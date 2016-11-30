package com.sharpnode.setupdevice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.sharpnode.R;
import com.sharpnode.SNApplication;
import com.sharpnode.adapter.DeviceAdapter;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.ConfiguredDevices;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.Responses;
import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.SoftAPConfigRemover;

/**
 * Created by admin on 11/15/2016.
 */

public class MyDevicesActivity extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak {

    public final static String EXTRA_SETUP_LAUNCHED_TIME = "io.particle.devicesetup.sharpnode.SETUP_LAUNCHED_TIME";
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private Button btnSetupDevice;
    private ParticleDeviceSetupLibrary.DeviceSetupCompleteReceiver receiver = null;
    private Toolbar mToolbar;
    private Async.AsyncApiWorker<ParticleCloud, Responses.ClaimCodeResponse> claimCodeWorker;
    private ParticleCloud sparkCloud;
    private SoftAPConfigRemover softAPConfigRemover;
    private RecyclerView rvDevices;
    private LinearLayoutManager mLayoutManager;
    private DeviceAdapter mAdapter;
    private ArrayList<ConfiguredDevices> devices = null;
    private ScrollView svSetupInstruction;
    private ProgressDialog loader = null;
    private LinearLayout llDevices;
    private ImageButton btnAddDevice;

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
        //Set Custom font to title.
        try {
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView titleText = (TextView) f.get(mToolbar);
            titleText.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        llDevices = (LinearLayout)findViewById(R.id.llDevices);
        rvDevices = (RecyclerView) findViewById(R.id.rvDevices);

        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        loader.setCancelable(false);

        prepareDeviceList();
        getDevices();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddDevice:
                startActivity(new Intent(mContext, DeviceSetupActivity.class));
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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getDevices(){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            loader.show();
            //Call API Request after check internet connection
            new Communicator(mContext, APIUtils.CMD_GET_DEVICES,
                    getDeviceRequestMap(APIUtils.CMD_GET_DEVICES, AppSPrefs.getString(Commons.ACCESS_TOKEN)));
        } else {
            finish();
            Logger.i(TAG, "Not connected to Internet.");
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
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

    @Override
    public void onSuccess(String name, Object object) {
        loader.dismiss();
        try {
            Logger.i(TAG, "Name: "+name+"Response: " + object);
            if (APIUtils.CMD_GET_DEVICES.equalsIgnoreCase(name)) {
                ConfiguredDevices model = ResponseParser.parseGetDevicesResponse(object);
                if (model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    mAdapter.setData(model.getDevicesList());
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, model.getResponseMsg(),
                            Toast.LENGTH_LONG).show();
                }
            } else if (APIUtils.CMD_REMOVE_DEVICE.equalsIgnoreCase(name)) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        loader.dismiss();
        Toast.makeText(mContext, "Login response Failure", Toast.LENGTH_LONG).show();
    }
}
