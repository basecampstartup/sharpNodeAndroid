package com.sharpnode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.cloudcommunication.CloudCommunicator;
import com.sharpnode.cloudcommunication.CloudUtils;
import com.sharpnode.commons.Commons;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by admin on 11/8/2016.
 */
public class AppliancesActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener,APIRequestCallbacak,PopupMenu.OnMenuItemClickListener{

    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private Toolbar mToolbar;
    ImageView ivFanSwitchBtn, ivCFLSwitchBtn, ivLampSwitchBtn, ivTVSwitchBtn, ivMusicSwitchBtn, ivWashingMachineSwitchBtn;
    ImageView ivFan, ivCFL, ivLamp, ivTV, ivMusic, ivWashingMachine;
    private String configuredDeviceId = "";
    private ProgressDialog loader;

    TextView tvFan, tvCFL, tvLamp, tvTV, tvMusic, tvWashingMachine;
    RelativeLayout rlFan, rlCFL, rlLamp, rlTV, rlMusic, rlWashingMachine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliances);

        mContext = this;

        //Initialize toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.LeftPanelAppliances));
        //Set Custom font to title.
        try {
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView titleText = (TextView) f.get(mToolbar);
            titleText.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }

        configuredDeviceId = AppSPrefs.getString(Commons.CONFIGURED_DEVICE_ID);
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        loader.setCancelable(false);
        initializeComponents();
        //call the code to set name of all appliaces here
    }

    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        ivFanSwitchBtn = (ImageView) findViewById(R.id.ivFanSwitchBtn);
        ivCFLSwitchBtn = (ImageView) findViewById(R.id.ivCFLSwitchBtn);
        ivLampSwitchBtn = (ImageView) findViewById(R.id.ivLampSwitchBtn);
        ivTVSwitchBtn = (ImageView) findViewById(R.id.ivTVSwitchBtn);
        ivMusicSwitchBtn = (ImageView) findViewById(R.id.ivMusicSwitchBtn);
        ivWashingMachineSwitchBtn = (ImageView) findViewById(R.id.ivWashingMachineSwitchBtn);

        rlFan = (RelativeLayout) findViewById(R.id.rlFan);
        rlCFL = (RelativeLayout) findViewById(R.id.rlCFL);
        rlLamp = (RelativeLayout) findViewById(R.id.rlLamp);
        rlTV = (RelativeLayout) findViewById(R.id.rlTV);
        rlMusic = (RelativeLayout) findViewById(R.id.rlMusic);
        rlWashingMachine = (RelativeLayout) findViewById(R.id.rlWashingMachine);

        ivFan = (ImageView) findViewById(R.id.ivFan);
        ivCFL = (ImageView) findViewById(R.id.ivCFL);
        ivLamp = (ImageView) findViewById(R.id.ivLamp);
        ivTV = (ImageView) findViewById(R.id.ivTV);
        ivMusic = (ImageView) findViewById(R.id.ivMusic);
        ivWashingMachine = (ImageView) findViewById(R.id.ivWashingMachine);

        ivFanSwitchBtn.setTag(false);
        ivCFLSwitchBtn.setTag(false);
        ivLampSwitchBtn.setTag(false);
        ivTVSwitchBtn.setTag(false);
        ivMusicSwitchBtn.setTag(false);

        ivWashingMachineSwitchBtn.setTag(false);
        ivFanSwitchBtn.setOnClickListener(this);
        ivCFLSwitchBtn.setOnClickListener(this);
        ivLampSwitchBtn.setOnClickListener(this);
        ivTVSwitchBtn.setOnClickListener(this);
        ivMusicSwitchBtn.setOnClickListener(this);
        ivWashingMachineSwitchBtn.setOnClickListener(this);
        tvFan=(TextView)findViewById(R.id.tvFan);
        tvFan.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvCFL=(TextView)findViewById(R.id.tvCFL);
        tvCFL.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvLamp=(TextView)findViewById(R.id.tvLamp);
        tvLamp.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvTV=(TextView)findViewById(R.id.tvTV);
        tvTV.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvMusic=(TextView)findViewById(R.id.tvMusic);
        tvMusic.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvWashingMachine=(TextView)findViewById(R.id.tvWashingMachine);
        tvWashingMachine.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        rlFan.setOnLongClickListener(this);
        rlCFL.setOnLongClickListener(this);
        rlLamp.setOnLongClickListener(this);
        rlTV.setOnLongClickListener(this);
        rlMusic.setOnLongClickListener(this);
        rlWashingMachine.setOnLongClickListener(this);


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
            //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        }catch (Exception e){}

        this.finish();
    }

    @Override
    public void onClick(View v) {
        if(Utils.preventMultipleClick())
            return;

        switch (v.getId()) {

            case R.id.ivCFLSwitchBtn:

                if ((boolean)ivCFLSwitchBtn.getTag()){
                    ivCFLSwitchBtn.setImageResource(R.drawable.off_btn);
                    ivCFLSwitchBtn.setTag(false);
                    ivCFL.setImageResource(R.drawable.cfl);
                } else {
                    ivCFLSwitchBtn.setImageResource(R.drawable.on_btn);
                    ivCFLSwitchBtn.setTag(true);
                    ivCFL.setImageResource(R.drawable.cfl_teal);
                }
                String value = ((boolean)ivCFLSwitchBtn.getTag())?"l1,HIGH":"l1,LOW";

                if(CheckNetwork.isInternetAvailable(mContext)){
                    showLoader();
                    //Call Cloud API Request after check internet connection
                    new CloudCommunicator(mContext, CloudUtils.CLOUD_FUNCTION_LED,
                            getParams(configuredDeviceId, value));
                } else {
                    Toast.makeText(mContext, getString(R.string.check_for_internet_connectivity),
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ivFanSwitchBtn:
                if ((boolean)ivFanSwitchBtn.getTag()){
                    ivFanSwitchBtn.setImageResource(R.drawable.off_btn);
                    ivFanSwitchBtn.setTag(false);
                    ivFan.setImageResource(R.drawable.fan);
                } else {
                    ivFanSwitchBtn.setImageResource(R.drawable.on_btn);
                    ivFanSwitchBtn.setTag(true);
                    ivFan.setImageResource(R.drawable.fan_teal);
                }
                break;
            case R.id.ivLampSwitchBtn:
                if ((boolean)ivLampSwitchBtn.getTag()){
                    ivLampSwitchBtn.setImageResource(R.drawable.off_btn);
                    ivLampSwitchBtn.setTag(false);
                    ivLamp.setImageResource(R.drawable.lamp);
                } else {
                    ivLampSwitchBtn.setImageResource(R.drawable.on_btn);
                    ivLampSwitchBtn.setTag(true);
                    ivLamp.setImageResource(R.drawable.lamp_teal);
                }
                break;
            case R.id.ivTVSwitchBtn:
                if ((boolean)ivTVSwitchBtn.getTag()){
                    ivTVSwitchBtn.setImageResource(R.drawable.off_btn);
                    ivTVSwitchBtn.setTag(false);
                    ivTV.setImageResource(R.drawable.tv);
                } else {
                    ivTVSwitchBtn.setImageResource(R.drawable.on_btn);
                    ivTVSwitchBtn.setTag(true);
                    ivTV.setImageResource(R.drawable.tv_teal);
                }

                break;
            case R.id.ivMusicSwitchBtn:
                if ((boolean)ivMusicSwitchBtn.getTag()){
                    ivMusicSwitchBtn.setImageResource(R.drawable.off_btn);
                    ivMusicSwitchBtn.setTag(false);
                    ivMusic.setImageResource(R.drawable.music);
                } else {
                    ivMusicSwitchBtn.setImageResource(R.drawable.on_btn);
                    ivMusicSwitchBtn.setTag(true);
                    ivMusic.setImageResource(R.drawable.music_teal);
                }
                break;
            case R.id.ivWashingMachineSwitchBtn:
                if ((boolean)ivWashingMachineSwitchBtn.getTag()){
                    ivWashingMachineSwitchBtn.setImageResource(R.drawable.off_btn);
                    ivWashingMachineSwitchBtn.setTag(false);
                    ivWashingMachine.setImageResource(R.drawable.washing_machine);
                } else {
                    ivWashingMachineSwitchBtn.setImageResource(R.drawable.on_btn);
                    ivWashingMachineSwitchBtn.setTag(true);
                    ivWashingMachine.setImageResource(R.drawable.washing_machine_teal);
                }
                break;
        }
    }
    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.rlFan:
                TextView tvFan=(TextView)v.findViewById(R.id.tvFan);
                String name=tvFan.getText().toString();
                Intent renameIntent=new Intent(AppliancesActivity.this,RenameApplianceActivity.class);
                renameIntent.putExtra("name",name);
                startActivity(renameIntent);

            /*    showRenameDialog(mContext,name,tvFan);
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(AppliancesActivity.this);
                popupMenu.inflate(R.menu.menu_device_options);
                popupMenu.show();*/
                break;
            case R.id.rlCFL:

                break;
            case R.id.rlLamp:

                break;
            case R.id.rlMusic:

                break;
            case R.id.rlWashingMachine:

                break;
            case R.id.rlTV:

                break;
        }
        return true;
    }
    private HashMap<String, String> getParams(String deviceId, String switchOnOff){

        HashMap<String, String> params = new HashMap<>();
        params.put(Commons.CONFIGURED_DEVICE_ID, deviceId);
        params.put(CloudUtils.SWITCH_OPERATION_FOR_LED, switchOnOff);
        return params;
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


    public void showRenameDialog(Context con,String oldName,final TextView tv)
    {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(con);
        View mView = layoutInflaterAndroid.inflate(R.layout.rename_dialog_box, null);
        final EditText edtName=(EditText)mView.findViewById(R.id.userInputDialog);
        edtName.setText(oldName);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(con);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // Call Update name for that Item API here..
                        tv.setText(edtName.getText().toString());
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

    }

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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_rename:
               /* TextView tvFan=(TextView)v.findViewById(R.id.tvFan);
                String name=tvFan.getText().toString();
                Toast.makeText(mContext, "Rename Clicked", Toast.LENGTH_SHORT).show();
                Intent renameIntent=new Intent(AppliancesActivity.this,RenameApplianceActivity.class);
                renameIntent.putExtra("name",name);*/
                return true;

            default:
                return false;
        }
    }
}
