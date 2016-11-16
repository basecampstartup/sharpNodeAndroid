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
        ivFanSwitchBtn.setOnClickListener(this);
        ivCFLSwitchBtn.setOnClickListener(this);
        ivLampSwitchBtn.setOnClickListener(this);
        ivTVSwitchBtn.setOnClickListener(this);
        ivMusicSwitchBtn.setOnClickListener(this);
        ivWashingMachineSwitchBtn.setOnClickListener(this);


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
        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivFanSwitchBtn:
                if ((boolean)ivFanSwitchBtn.getTag()){
                    ivFanSwitchBtn.setImageResource(R.drawable.on_btn);
                    ivFanSwitchBtn.setTag(false);
                } else {
                    ivFanSwitchBtn.setImageResource(R.drawable.off_btn);
                    ivFanSwitchBtn.setTag(true);
                }
                Toast.makeText(mContext,"click",Toast.LENGTH_LONG).show();
                break;
            case R.id.ivCFLSwitchBtn:
                Toast.makeText(mContext,"click",Toast.LENGTH_LONG).show();
                ivCFLSwitchBtn.playSoundEffect(SoundEffectConstants.CLICK);
                break;
            case R.id.ivLampSwitchBtn:
                Toast.makeText(mContext,"click",Toast.LENGTH_LONG).show();
                ivLampSwitchBtn.playSoundEffect(SoundEffectConstants.CLICK);
                break;
            case R.id.ivTVSwitchBtn:
                Toast.makeText(mContext,"click",Toast.LENGTH_LONG).show();
                ivTVSwitchBtn.playSoundEffect(SoundEffectConstants.CLICK);
                break;
            case R.id.ivMusicSwitchBtn:
                Toast.makeText(mContext,"click",Toast.LENGTH_LONG).show();
                ivMusicSwitchBtn.playSoundEffect(SoundEffectConstants.CLICK);
                break;
            case R.id.ivWashingMachineSwitchBtn:
                Toast.makeText(mContext,"click",Toast.LENGTH_LONG).show();
                ivWashingMachineSwitchBtn.playSoundEffect(SoundEffectConstants.CLICK);
                break;
        }

    }
}
