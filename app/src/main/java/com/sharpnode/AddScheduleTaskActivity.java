package com.sharpnode;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.Locale;
import java.util.TimeZone;

public class AddScheduleTaskActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener  {

    Context mContext;
    private Toolbar mToolbar;
    String[] arrAppliances = {"Fan", "CFL", "Lamp", "TV", "Music", "Washing Machine"};
    ArrayList<String> appliancesList = new ArrayList<>();
    String selectedAppliance;
    TextView txtSelectAppliance,tvSelectTimeLbl,tvSelectedTime;
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
        initializeComponents();
        setCurrentDataTimeFields();


    }

    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        txtSelectAppliance = (TextView) findViewById(R.id.tvSelectAppliance);
        txtSelectAppliance.setOnClickListener(this);
        ((TextView) findViewById(R.id.tvSelectAppliance)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView) findViewById(R.id.tvSelectApplianceLbl)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
        ((TextView) findViewById(R.id.tvEveryWeekLbl)).setTypeface(SNApplication.APP_FONT_TYPEFACE);
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
        chkSu = (TextView)findViewById(R.id.chkSu);
        chkMo = (TextView)findViewById(R.id.chkMo);
        chkTu = (TextView)findViewById(R.id.chkTu);
        chkWe = (TextView)findViewById(R.id.chkWe);
        chkTh = (TextView)findViewById(R.id.chkTh);
        chkFr = (TextView)findViewById(R.id.chkFr);
        chkSat = (TextView)findViewById(R.id.chkSat);

        chkSu.setTag(false);
        chkMo.setTag(false);
        chkTu.setTag(false);
        chkWe.setTag(false);
        chkTh.setTag(false);
        chkFr.setTag(false);
        chkSat.setTag(false);

        chkSu.setOnClickListener(this);
        chkMo.setOnClickListener(this);
        chkTu.setOnClickListener(this);
        chkWe.setOnClickListener(this);
        chkTh.setOnClickListener(this);
        chkFr.setOnClickListener(this);
        chkSat.setOnClickListener(this);
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
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstAppliance);
        appliancesList = new ArrayList<String>(Arrays.asList(arrAppliances));
        // Change MyActivity.this and myListOfItems to your own values
        ApplianceListDialogAdapter applianceListDialogAdapter = new ApplianceListDialogAdapter(AddScheduleTaskActivity.this, appliancesList);
        lv.setAdapter(applianceListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAppliance = appliancesList.get(position);
            //    Toast.makeText(AddScheduleTaskActivity.this, "Item :" + selectedAppliance, Toast.LENGTH_SHORT).show();
                txtSelectAppliance.setText(selectedAppliance);
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
                String radiovalue = ((RadioButton) findViewById(radGroupOnOff.getCheckedRadioButtonId())).getText().toString();
                break;
            case R.id.tvSelectAppliance:
                showApplianceDialog();
                break;
            case R.id.tvSelectedTime:
                openTimePicker();
                break;
            case R.id.chkSu:
                chkSu.setBackgroundResource(android.R.color.transparent);
                if((boolean)chkSu.getTag()){
                    //set white bg.
                    chkSu.setTag(false);
                    weekDays.remove("Sunday");
                }else{
                    chkSu.setBackgroundResource(R.drawable.circle);
                    weekDays.add("Sunday");
                    //set teal circle bg.
                    chkSu.setTag(true);
                }
                break;
            case R.id.chkMo:
                chkMo.setBackgroundResource(android.R.color.transparent);
                if((boolean)chkMo.getTag()){
                    //set white bg.
                    chkMo.setTag(false);
                    weekDays.remove("Monday");
                }else{
                    chkMo.setBackgroundResource(R.drawable.circle);
                    weekDays.add("Monday");
                    //set teal circle bg.
                    chkMo.setTag(true);
                }
                break;
            case R.id.chkTu:
                chkTu.setBackgroundResource(android.R.color.transparent);
                if((boolean)chkTu.getTag()){
                    //set white bg.
                    chkTu.setTag(false);
                    weekDays.remove("Tuesday");
                }else{
                    chkTu.setBackgroundResource(R.drawable.circle);
                    weekDays.add("Tuesday");
                    //set teal circle bg.
                    chkTu.setTag(true);
                }
                break;
            case R.id.chkWe:
                chkWe.setBackgroundResource(android.R.color.transparent);
                if((boolean)chkWe.getTag()){
                    //set white bg.
                    chkWe.setTag(false);
                    weekDays.remove("Wednesday");
                }else{
                    chkWe.setBackgroundResource(R.drawable.circle);
                    weekDays.add("Wednesday");
                    //set teal circle bg.
                    chkWe.setTag(true);
                }
                break;
            case R.id.chkTh:
                chkTh.setBackgroundResource(android.R.color.transparent);
                if((boolean)chkTh.getTag()){
                    //set white bg.
                    chkTh.setTag(false);
                    weekDays.remove("Thursday");
                }else{
                    chkTh.setBackgroundResource(R.drawable.circle);
                    weekDays.add("Thursday");
                    //set teal circle bg.
                    chkTh.setTag(true);
                }
                break;
            case R.id.chkFr:
                chkFr.setBackgroundResource(android.R.color.transparent);
                if((boolean)chkFr.getTag()){
                    //set white bg.
                    chkFr.setTag(false);
                    weekDays.remove("Friday");
                }else{
                    chkFr.setBackgroundResource(R.drawable.circle);
                    weekDays.add("Friday");
                    //set teal circle bg.
                    chkFr.setTag(true);
                }
                break;
            case R.id.chkSat:
                chkSat.setBackgroundResource(android.R.color.transparent);
                if((boolean)chkSat.getTag()){
                    //set white bg.
                    chkSat.setTag(false);
                    weekDays.remove("Saturday");
                }else{
                    chkSat.setBackgroundResource(R.drawable.circle);
                    weekDays.add("Saturday");
                    //set teal circle bg.
                    chkSat.setTag(true);
                }
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
}