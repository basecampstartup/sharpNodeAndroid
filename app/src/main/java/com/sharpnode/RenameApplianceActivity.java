package com.sharpnode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.adapter.ApplianceListDialogAdapter;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.BaseModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RenameApplianceActivity extends AppCompatActivity implements APIRequestCallbacak, View.OnClickListener {

    private String TAG = getClass().getSimpleName();
    private EditText edtApplianceLabelValue;
    private TextView tvSelectAppliance, tvApplianceDetailLbl, tvApplianceType;
    private Button btnUpdate;
    private String selectedAppliance;
    private String action = Utils.arrAppliancesKey[0];
    private ArrayList<String> appliancesList = new ArrayList<>();
    private Context mContext;
    private Toolbar mToolbar;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_appliance);
        mContext = this;
        //Initialize toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.RenameAppliance));
        Utils.setTitleFontTypeface(mToolbar);
        loader = new ProgressDialog(mContext);
        initializeComponents();

        String name = getIntent().getStringExtra("APPLIANCE_NAME");
        edtApplianceLabelValue.setText(name);
    }

    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        tvApplianceDetailLbl = (TextView)findViewById(R.id.tvApplianceDetailLbl);
        tvApplianceType = (TextView)findViewById(R.id.tvApplianceType);

        tvSelectAppliance = (TextView) findViewById(R.id.tvSelectAppliance);
        tvSelectAppliance.setOnClickListener(this);

        edtApplianceLabelValue = (EditText) findViewById(R.id.edtApplianceLabelValue);
        edtApplianceLabelValue.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        tvApplianceDetailLbl.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvApplianceType.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvSelectAppliance.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        btnUpdate.setTypeface(SNApplication.APP_FONT_TYPEFACE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                String name = edtApplianceLabelValue.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(mContext, "Appliance name is required", Toast.LENGTH_LONG).show();
                    return;
                }
                String appliance = tvSelectAppliance.getText().toString();
                if(TextUtils.isEmpty(appliance) || getString(R.string.SelectAppliance).equalsIgnoreCase(appliance)){
                    Toast.makeText(mContext, "Appliance type selection is required", Toast.LENGTH_LONG).show();
                    return;
                }
                callRenameAppliance(name, action);
                break;
            case R.id.tvSelectAppliance:
                showApplianceDialog();
            default:
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        } catch (Exception e) {
        }

        this.finish();
    }

    public void showApplianceDialog() {
        final Dialog dialog = new Dialog(this);
        //dialog.setTitle("Select Appliance Type");
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstAppliance);
        final ArrayList<String> appliancesList = new ArrayList<String>(Arrays.asList(Utils.arrAppliances));

        ApplianceListDialogAdapter applianceListDialogAdapter =
                new ApplianceListDialogAdapter(RenameApplianceActivity.this, appliancesList);
        lv.setAdapter(applianceListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                action = Utils.arrAppliancesKey[position];
                selectedAppliance = appliancesList.get(position);
                tvSelectAppliance.setText(selectedAppliance);
                dialog.dismiss();
            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.show();
    }

    private void callRenameAppliance(String switchName, String switchId){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_RENAME_APPLIANCE,
                    getRequestMap(APIUtils.CMD_RENAME_APPLIANCE, switchName, switchId));
        } else {
            finish();
            Logger.i(TAG, "Not connected to Internet.");
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param method
     *
     * @return
     */
    public HashMap<String, String> getRequestMap(String method, String switchName, String switchId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        map.put(Commons.CONFIGURED_DEVICE_ID, AppSPrefs.getDeviceId());
        map.put(Commons.SWITCH_ID, switchId);
        map.put(Commons.SWITCH_NAME, switchName);
        return map;
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

    @Override
    public void onSuccess(String name, Object object) {
        Utils.dismissLoader();
        Logger.i(TAG, name+", onSuccess, Response: " + object);
        try {
            BaseModel model = ResponseParser.parseResponse(object);
            if(APIUtils.CMD_RENAME_APPLIANCE.equalsIgnoreCase(name)){
                if(Commons.CODE_200.equalsIgnoreCase(model.getResponseCode())){
                    //finish();
                    getDeviceInfo(AppSPrefs.getDeviceId());
                }
                Toast.makeText(mContext, model.getResponseMsg(), Toast.LENGTH_LONG).show();
            } else if (APIUtils.CMD_DEVICE_INFO.equalsIgnoreCase(name)) {
                Logger.i(TAG, "onSuccess"+" Name: "+name+" Response: " + object);
                //Parsed only for check status is 200.
                BaseModel model1 = ResponseParser.parseResponse(object);
                if (model1.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                    Intent deviceInfoIntent = new Intent(mContext, DeviceDashboardActivity.class);
                    //Sending response JSON data on called Activity for later use.
                    deviceInfoIntent.putExtra("DEVICE_INFO", object.toString());
                    startActivity(deviceInfoIntent);
                    //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
                    finish();
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
