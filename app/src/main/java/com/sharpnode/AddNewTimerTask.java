package com.sharpnode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sharpnode.adapter.ApplianceListDialogAdapter;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.commons.Commons;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.TimeStyleEnum;
import com.sharpnode.utils.Utils;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class AddNewTimerTask extends AppCompatActivity implements View.OnClickListener, APIRequestCallbacak, TimePickerDialog.OnTimeSetListener  {

    private String TAG = getClass().getSimpleName();
    private ProgressDialog loader;
    Context mContext;
    private Toolbar mToolbar;
    Button btnAdd;
    EditText edtTaskName;
    private TextView tvScheduleDayTime, tvRepeat, tvSelectAppliance;
    private EditText tvSelectedTime;
    private RadioGroup radGroupOnOff;
    private RadioButton radOn,radOff;
    private TimePicker endDatePicker;
    private boolean systemPeriod;
    private int h = 0, m = 0;
    private int d = 0, mon = 0, y = 0;
    private ArrayList<String> optionsList;

    private String selectedOption;
    private CheckBox cbRepeat;
    private String action=Utils.arrAppliancesKey[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timer_task);

        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.TimerAddNewTask));
        Utils.setTitleFontTypeface(mToolbar);
        loader = new ProgressDialog(this);
        initializeComponents();
        //setCurrentDataTimeFields();

        Utils.arrAppliances = (String[]) DeviceDashboardActivity.deviceInfoModel.getApplianceList().toArray(new String[DeviceDashboardActivity.deviceInfoModel.getApplianceList().size()]);
        Log.i("DEVICE", "Add Timer: "+Utils.arrAppliances[0]);
    }

    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        tvSelectAppliance = (TextView) findViewById(R.id.tvSelectAppliance);
        tvSelectAppliance.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvSelectAppliance.setOnClickListener(this);

        tvSelectedTime = (EditText) findViewById(R.id.tvSelectedTime);
        tvSelectedTime.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvSelectedTime.setOnClickListener(this);

        cbRepeat = (CheckBox) findViewById(R.id.cbRepeat);
        cbRepeat.setChecked(true);
        tvRepeat = (TextView) findViewById(R.id.tvRepeat);
        tvRepeat.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvRepeat.setOnClickListener(this);
        tvScheduleDayTime = (TextView) findViewById(R.id.tvScheduleDayTime);
        tvScheduleDayTime.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvScheduleDayTime.setOnClickListener(this);

        ((TextView) findViewById(R.id.tvOperationLabel)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView) findViewById(R.id.tvScheduleDayTime)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtTaskName = (EditText) findViewById(R.id.edtTaskName);
        edtTaskName.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnAdd.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        radGroupOnOff = (RadioGroup) findViewById(R.id.radGroupOnOff);
        radOn = (RadioButton) findViewById(R.id.radOn);
        radOn.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        radOff = (RadioButton) findViewById(R.id.radOff);
        radOff.setTypeface(SNApplication.APP_FONT_TYPEFACE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAdd:
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date expiryDate = calendar.getTime();

                String radiovalue = ((RadioButton) findViewById(radGroupOnOff.getCheckedRadioButtonId())).getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateTime =sdf.format(expiryDate);
                String taskName = edtTaskName.getText().toString();
                if(TextUtils.isEmpty(taskName)){
                    Toast.makeText(mContext, "Task name is required", Toast.LENGTH_LONG).show();
                    return;
                }
                String appliance = tvSelectAppliance.getText().toString();
                if(TextUtils.isEmpty(appliance) || getString(R.string.SelectAppliance).equalsIgnoreCase(appliance)){
                    Toast.makeText(mContext, "Appliance selection is required", Toast.LENGTH_LONG).show();
                    return;
                }
                String interval = tvSelectedTime.getText().toString();
                if(TextUtils.isEmpty(interval)){
                    Toast.makeText(mContext, "Interval is required", Toast.LENGTH_LONG).show();
                    return;
                }
                int intervalValue = Integer.parseInt(interval);
                if (intervalValue <= 0 || intervalValue > 60) {
                    Toast.makeText(mContext, "Interval must be between 1-60", Toast.LENGTH_LONG).show();
                    return;
                }
                callAddTask(dateTime, cbRepeat.isChecked());
                break;
            case R.id.tvSelectAppliance:
                showApplianceDialog();
                break;
            case R.id.tvSelectedTime:
                //showIntervalDialog();//openTimePicker();
                break;
//            case R.id.tvScheduleDayTime:
//                showIntervalDialog();//openTimePicker();
//                break;
            case R.id.tvRepeat:
                if(cbRepeat.isChecked())
                    cbRepeat.setChecked(false);
                else
                    cbRepeat.setChecked(true);

                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_side_in, R.anim.right_side_out);
        this.finish();
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

        StringBuffer sb = new StringBuffer();
        sb.append(hourOfDay);
        sb.append(":");
        sb.append(minute);
        String time = sb.toString();
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
            final Date dateObj = sdf.parse(time);
            tvSelectedTime.setText(Utils.formatTime(this, dateObj, TimeStyleEnum.StyleType.SHORT));
        } catch (final ParseException e) {
        }
    }

    @Override
    public void onTimeCancel() {

    }

    /**
     * method to open time picker
     */
    private void openTimePicker() {
        systemPeriod = false;

        // open time picker for start time
        TimePickerDialog tpd = TimePickerDialog.newInstance(AddNewTimerTask.this, h, m, android.text.format.DateFormat.is24HourFormat(this));
        tpd.show(getFragmentManager(), "Timepickerdialog");

    }


    /**
     * To set current date and time in date-time text fields
     */
    private void setCurrentDataTimeFields() {

        Calendar nowLocal = Calendar.getInstance();
        nowLocal.setTimeZone(TimeZone.getDefault());
        h = nowLocal.get(Calendar.HOUR_OF_DAY);
        m = nowLocal.get(Calendar.MINUTE);
        Logger.d("Calendar11", "At setCurrentDataTimeFields: Time Value Start:"+ h + ":" + m);
        Date dateTime = getDateByTime(y, mon, d, h, m);
        tvSelectedTime.setText(Utils.formatTime(this, dateTime, TimeStyleEnum.StyleType.SHORT));//.replace(CAP_AM, SMALL_AM).replace(CAP_PM, SMALL_PM));

    }
    /**
     * To get the date as per all the parameters
     *
     * @param y   : year
     * @param mon :month
     * @param d   :day
     * @param h   :hour
     * @param m   :minute
     * @return Date
     */
    private Date getDateByTime(int y, int mon, int d, int h, int m) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, mon);
        calendar.set(Calendar.DAY_OF_MONTH, d);
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        return calendar.getTime();
    }

    public void showApplianceDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Select any option");
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstAppliance);
        Utils.arrAppliances = (String[]) DeviceDashboardActivity.deviceInfoModel.getApplianceList().toArray(new String[DeviceDashboardActivity.deviceInfoModel.getApplianceList().size()]);
        final ArrayList<String> list = new ArrayList<String>(Arrays.asList(Utils.arrAppliances));
        list.add("Security Feature");
        // Change MyActivity.this and myListOfItems to your own values
        ApplianceListDialogAdapter applianceListDialogAdapter = new ApplianceListDialogAdapter(mContext, list);
        lv.setAdapter(applianceListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                action = Utils.arrAppliancesKey[position];
                tvSelectAppliance.setText(list.get(position));
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public void showIntervalDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Select Interval");
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstAppliance);
        final ArrayList<String> list = new ArrayList<String>(Arrays.asList(Utils.arrInterval));
        // Change MyActivity.this and myListOfItems to your own values
        ApplianceListDialogAdapter applianceListDialogAdapter = new ApplianceListDialogAdapter(mContext, list);
        lv.setAdapter(applianceListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvSelectedTime.setText(list.get(position));
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private void callAddTask(String startDate, boolean repeat){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_TIMER_ADD,
                    getRequestMap(APIUtils.CMD_TIMER_ADD, startDate, repeat));
        } else {
            finish();
            Logger.i(TAG, "Not connected to Internet.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * @param method
     * @param startDate
     * @return
     */
    public HashMap<String, String> getRequestMap(String method, String startDate, boolean repeat) {
        String date = startDate.substring(0, startDate.lastIndexOf(":"));
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        map.put(Commons.CONFIGURED_DEVICE_ID, AppSPrefs.getDeviceId());

        map.put("name", edtTaskName.getText().toString());
        map.put("task", (radOn.isChecked()) ? "ON" : "OFF");
        map.put("action", action);
        map.put("repeat", ""+repeat);
        map.put("interval", tvSelectedTime.getText().toString().replace("-Min", "").trim()/*date+":00"*/);

        return map;
    }

    @Override
    public void onSuccess(String name, Object object) {
        Utils.dismissLoader();
        Logger.i(TAG, name+", onSuccess, Response: " + object);
        try {
            if(APIUtils.CMD_TIMER_ADD.equalsIgnoreCase(name)){
                Intent intent = new Intent();
                intent.putExtra("RESULT", "TASK_ADDED");
                setResult(1, intent);
                finish();
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
