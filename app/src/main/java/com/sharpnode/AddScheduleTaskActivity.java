package com.sharpnode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sharpnode.adapter.ApplianceListDialogAdapter;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.cloudcommunication.CloudUtils;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.BaseModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.servercommunication.ResponseParser;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.TimeStyleEnum;
import com.sharpnode.utils.Utils;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class AddScheduleTaskActivity extends AppCompatActivity implements APIRequestCallbacak, View.OnClickListener, TimePickerDialog.OnTimeSetListener  {

    private String TAG = getClass().getSimpleName();
    private ProgressDialog loader;
    Context mContext;
    private Toolbar mToolbar;
    ArrayList<String> appliancesList = new ArrayList<>();
    String selectedAppliance;
    String selectedRepeat="Everyday";
    TextView txtSelectAppliance, tvSelectAppliance, tvRepeatOnValue, tvSelectTimeLbl,tvSelectedTime;
    private RadioGroup radGroupOnOff;
    private RadioButton radOn,radOff;
    private TimePicker endDatePicker;
    private boolean systemPeriod;
    private int h = 0, m = 0;
    private int d = 0, mon = 0, y = 0;
    private TextView chkSu,chkMo,chkTu,chkWe,chkTh,chkFr,chkSat;
    private ArrayList weekDays = new ArrayList();
    Button btnSchedule;
    EditText edtTaskName;
    String taskID = "", action=Utils.arrAppliancesKey[0];
    private boolean fromFlag;
    private Date selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule_task);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.SchedulerAddNewTask));
        Utils.setTitleFontTypeface(mToolbar);
        loader = new ProgressDialog(this);
        initializeComponents();
        setCurrentDataTimeFields();

        fromFlag = getIntent().hasExtra("KEY");
        if(fromFlag && getIntent().getStringExtra("KEY").equalsIgnoreCase("UPDATE")){
            setValues(getIntent());
        }
    }

    private void setValues(Intent intent){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(intent.getStringExtra("NEXT_DATE")));

        action = intent.getStringExtra("ACTION");
        taskID = intent.getStringExtra("TASK_ID");
        edtTaskName.setText(intent.getStringExtra("TASK_NAME"));
        tvRepeatOnValue.setText(intent.getStringExtra("REPEAT"));
        tvSelectedTime.setTag(calendar.getTime());
        tvSelectedTime.setText(intent.getStringExtra("TIME"));
        selectedRepeat = intent.getStringExtra("REPEAT");
        btnSchedule.setText(getString(R.string.Update));

        if("on".equalsIgnoreCase(intent.getStringExtra("STATUS"))){
            radOn.setChecked(true);
        } else {
            radOff.setChecked(true);
        }

        if("switch-one".equalsIgnoreCase(action)){
            action = Utils.arrAppliancesKey[0];
            txtSelectAppliance.setText(Utils.arrAppliances[0]);
        }else if("switch-two".equalsIgnoreCase(action)){
            action = Utils.arrAppliancesKey[1];
            txtSelectAppliance.setText(Utils.arrAppliances[1]);
        }else if("switch-three".equalsIgnoreCase(action)){
            action = Utils.arrAppliancesKey[2];
            txtSelectAppliance.setText(Utils.arrAppliances[2]);
        }else if("switch-four".equalsIgnoreCase(action)){
            action = Utils.arrAppliancesKey[3];
            txtSelectAppliance.setText(Utils.arrAppliances[3]);
        }else if("switch-five".equalsIgnoreCase(action)){
            action = Utils.arrAppliancesKey[4];
            txtSelectAppliance.setText(Utils.arrAppliances[4]);
        }else if("switch-six".equalsIgnoreCase(action)){
            action = Utils.arrAppliancesKey[5];
            txtSelectAppliance.setText(Utils.arrAppliances[5]);
        }else {
            action = Utils.arrAppliancesKey[0];
            txtSelectAppliance.setText(Utils.arrAppliances[0]);
        }
        getSupportActionBar().setTitle(getString(R.string.SchedulerUpdateTask));
    }

    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        tvRepeatOnValue = (TextView) findViewById(R.id.tvRepeatOnValue);
        tvRepeatOnValue.setOnClickListener(this);
        tvRepeatOnValue.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        txtSelectAppliance = (TextView) findViewById(R.id.tvSelectAppliance);
        txtSelectAppliance.setOnClickListener(this);
        ((TextView) findViewById(R.id.tvSelectAppliance)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView) findViewById(R.id.tvSelectApplianceLbl)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView) findViewById(R.id.tvOperationLabel)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView) findViewById(R.id.tvScheduleDayTime)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        edtTaskName = (EditText) findViewById(R.id.edtTaskName);
        edtTaskName.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvSelectTimeLbl = (TextView) findViewById(R.id.tvSelectTimeLbl);
        tvSelectTimeLbl.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        tvSelectedTime = (TextView) findViewById(R.id.tvSelectedTime);
        tvSelectedTime.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        btnSchedule=(Button)findViewById(R.id.btnSchedule);
        btnSchedule.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        radGroupOnOff = (RadioGroup) findViewById(R.id.radGroupOnOff);
        radOn = (RadioButton) findViewById(R.id.radOn);
        radOn.setTypeface(SNApplication.APP_FONT_TYPEFACE);
        radOff = (RadioButton) findViewById(R.id.radOff);
        radOff.setTypeface(SNApplication.APP_FONT_TYPEFACE);

        btnSchedule.setOnClickListener(this);
        tvSelectTimeLbl.setOnClickListener(this);
        tvSelectedTime.setOnClickListener(this);
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

    public void showApplianceDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Choose Appliance");
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstAppliance);
        appliancesList = new ArrayList<String>(Arrays.asList(Utils.arrAppliances));
        // Change MyActivity.this and myListOfItems to your own values
        ApplianceListDialogAdapter applianceListDialogAdapter = new ApplianceListDialogAdapter(AddScheduleTaskActivity.this, appliancesList);
        lv.setAdapter(applianceListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAppliance = appliancesList.get(position);
                action = Utils.arrAppliancesKey[position];
                txtSelectAppliance.setText(selectedAppliance);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();


    }

    public void showRepeatOnDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Repeat on");
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstAppliance);
        final ArrayList<String> list = new ArrayList<String>(Arrays.asList(Utils.arrRepeat));
        // Change MyActivity.this and myListOfItems to your own values
        ApplianceListDialogAdapter applianceListDialogAdapter = new ApplianceListDialogAdapter(AddScheduleTaskActivity.this, list);
        lv.setAdapter(applianceListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedRepeat = list.get(position);
                tvRepeatOnValue.setText(selectedRepeat);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSchedule:
                //SimpleDateFormat timeSDF = new SimpleDateFormat("hh:mm a");
                //String nextTime =timeSDF.format(tvSelectedTime.getTag());

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateTime =sdf.format(tvSelectedTime.getTag());

                String taskName = edtTaskName.getText().toString();
                if(TextUtils.isEmpty(taskName)){
                    Toast.makeText(mContext, "Task name is required", Toast.LENGTH_LONG).show();
                    return;
                } else if(txtSelectAppliance.getText().toString().equalsIgnoreCase(getString(R.string.SelectAppliance))){
                    Toast.makeText(mContext, "Appliance selection is required", Toast.LENGTH_LONG).show();
                    return;
                }
                if(fromFlag){
                    callUpdateTask(dateTime, taskID);
                } else {
                    callAddTask(dateTime);
                }
                break;
            case R.id.tvSelectAppliance:
                showApplianceDialog();
                break;
            case R.id.tvRepeatOnValue:
                showRepeatOnDialog();
                break;
            case R.id.tvSelectedTime:
                openTimePicker();
                break;
            case R.id.tvSelectTimeLbl:
                openTimePicker();
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
        Calendar nowLocal = Calendar.getInstance();
        int h = nowLocal.get( Calendar.HOUR_OF_DAY );
        int m = nowLocal.get( Calendar.MINUTE );
        if (hourOfDay > h && minute > m) {
            StringBuffer sb = new StringBuffer();
            sb.append(hourOfDay);
            sb.append(":");
            sb.append(minute);
            String time = sb.toString();
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
                final Date dateObj = sdf.parse(time);
                tvSelectedTime.setTag(nowLocal.getTime());
                tvSelectedTime.setText(Utils.formatTime(this, dateObj, TimeStyleEnum.StyleType.SHORT));
            } catch (final ParseException e) {
            }
        } else {
            Utils.okAlertDialog(mContext, "Scheduler Date / Time must be greater than current time.");
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
            TimePickerDialog tpd = TimePickerDialog.newInstance(AddScheduleTaskActivity.this, h, m, android.text.format.DateFormat.is24HourFormat(this));
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
        Date dateTime = getDateByTime(y, mon, d, h, m);
        tvSelectedTime.setText(Utils.formatTime(this, dateTime, TimeStyleEnum.StyleType.SHORT));//.replace(CAP_AM, SMALL_AM).replace(CAP_PM, SMALL_PM));

        //By default selected date/time will be 1+.
        Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.DATE, 1);
        Date plusOne = cal.getTime();
        tvSelectedTime.setTag(plusOne);

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

    /**
     * To set time in Picker which was selected
     * id= 1 : start time
     * id= 0 : end time
     */
    private void setTimePickerTime(int id, int hourOfDay, int minute) {
            h = hourOfDay;
            m = minute;
    }


    public void onRadioButtonClicked(){

    }

    private void callAddTask(String startDate){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_SCHEDULER_ADD,
                    getRequestMap(APIUtils.CMD_SCHEDULER_ADD, startDate, "0"));
        } else {
            finish();
            Logger.i(TAG, "Not connected to Internet.");
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
        }
    }

    private void callUpdateTask(String startDate, String taskID){
        if (CheckNetwork.isInternetAvailable(mContext)) {
            Utils.showLoader(mContext, loader);
            //Call API Request after check internet connection
            new Communicator(mContext, null, APIUtils.CMD_SCHEDULER_UPDATE,
                    getRequestMap(APIUtils.CMD_SCHEDULER_UPDATE, startDate, taskID));
        } else {
            finish();
            Logger.i(TAG, "Not connected to Internet.");
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param method
     * @param startDate
     * @return
     */
    public HashMap<String, String> getRequestMap(String method, String startDate, String taskID) {
        String date = startDate.substring(0, startDate.lastIndexOf(":"));
        HashMap<String, String> map = new HashMap<>();
        map.put(Commons.COMMAND, method);
        map.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));
        map.put(Commons.CONFIGURED_DEVICE_ID, AppSPrefs.getDeviceId());

        if (fromFlag)
            map.put("task_id", taskID);

        map.put("name", edtTaskName.getText().toString());
        map.put("task", (radOn.isChecked()) ? "ON" : "OFF");
        map.put("action", action);
        map.put("repeat", selectedRepeat);
        map.put("startd", date+":00");

        return map;
    }

    @Override
    public void onSuccess(String name, Object object) {
        Utils.dismissLoader();
        Logger.i(TAG, name+", onSuccess, Response: " + object);
        try {
            BaseModel model = ResponseParser.parseResponse(object.toString());
            if(model.getResponseCode().equalsIgnoreCase(Commons.CODE_200)) {
                if (APIUtils.CMD_SCHEDULER_ADD.equalsIgnoreCase(name)) {
                    Intent intent = new Intent();
                    intent.putExtra("RESULT", "TASK_ADDED");
                    setResult(1, intent);
                    finish();
                } else if (APIUtils.CMD_SCHEDULER_UPDATE.equalsIgnoreCase(name)) {
                    Intent intent = new Intent();
                    intent.putExtra("RESULT", "TASK_UPDATED");
                    setResult(1, intent);
                    finish();
                }
            } else {
                Toast.makeText(mContext, model.getResponseMsg(), Toast.LENGTH_LONG).show();
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