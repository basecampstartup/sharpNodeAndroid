package com.sharpnode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.commons.Commons;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;

/**
 * Created by admin on 11/8/2016.
 */
public class AppliancesActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private ActionBar actionBar;
    private Toolbar mToolbar;
    ImageView ivFanSwitchBtn, ivCFLSwitchBtn, ivLampSwitchBtn, ivTVSwitchBtn, ivMusicSwitchBtn, ivWashingMachineSwitchBtn;
    ImageView ivFan, ivCFL, ivLamp, ivTV, ivMusic, ivWashingMachine;
    TextView tvFan, tvCFL, tvLamp, tvTV,tvMusic,tvWashingMachine;
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
        initializeComponents();

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
        }catch (Exception e){}

        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                ivFanSwitchBtn.playSoundEffect(SoundEffectConstants.CLICK);
                break;
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
                ivCFLSwitchBtn.playSoundEffect(SoundEffectConstants.CLICK);
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
                ivLampSwitchBtn.playSoundEffect(SoundEffectConstants.CLICK);
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
                ivTVSwitchBtn.playSoundEffect(SoundEffectConstants.CLICK);
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
                ivMusicSwitchBtn.playSoundEffect(SoundEffectConstants.CLICK);
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
                ivWashingMachineSwitchBtn.playSoundEffect(SoundEffectConstants.CLICK);
                break;
        }

    }
}
