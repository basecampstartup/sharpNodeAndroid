package com.sharpnode;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharpnode.adapter.ApplianceListDialogAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class RenameApplianceActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edtApplianceLableValue;
    TextView tvSelectAppliance;
    Button btnUpdate;
    private Context mContext;
    private Toolbar mToolbar;
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
        //Set Custom font to title.
        try {
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView titleText = (TextView) f.get(mToolbar);
            titleText.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
        initializeComponents();
        String name=getIntent().getStringExtra("name");
        edtApplianceLableValue.setText(name);
    }

    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        edtApplianceLableValue = (EditText) findViewById(R.id.edtApplianceLableValue);
        tvSelectAppliance = (TextView) findViewById(R.id.tvSelectAppliance);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        tvSelectAppliance.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        btnUpdate.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpdate:
                //call API to update Appliance name and finish and updte on old screen.
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
            //overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        }catch (Exception e){}

        this.finish();
    }

    String selectedAppliance;
    String[] arrAppliances = {"Fan", "CFL", "Lamp", "TV", "Music", "Washing Machine"};
    ArrayList<String> appliancesList = new ArrayList<>();
    public void showApplianceDialog() {
        appliancesList = new ArrayList<String>(Arrays.asList(arrAppliances));
        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstAppliance);
        appliancesList = new ArrayList<String>(Arrays.asList(arrAppliances));
        // Change MyActivity.this and myListOfItems to your own values
        ApplianceListDialogAdapter applianceListDialogAdapter = new ApplianceListDialogAdapter(RenameApplianceActivity.this, appliancesList);
        lv.setAdapter(applianceListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAppliance = appliancesList.get(position);
                //    Toast.makeText(AddScheduleTaskActivity.this, "Item :" + selectedAppliance, Toast.LENGTH_SHORT).show();
                tvSelectAppliance.setText(selectedAppliance);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();


    }

}
