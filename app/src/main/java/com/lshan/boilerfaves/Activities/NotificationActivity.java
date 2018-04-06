package com.lshan.boilerfaves.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.EventLog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import java.util.Calendar;

import com.lshan.boilerfaves.R;
import com.lshan.boilerfaves.Receivers.MasterAlarmReceiver;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;

import java.sql.SQLOutput;
import java.util.Date;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class NotificationActivity extends AppCompatActivity {



    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.breakfastSwitch)
    SwitchCompat breakfastSwitch;

    @BindView(R.id.lunchSwitch)
    SwitchCompat lunchSwitch;

    @BindView(R.id.dinnerSwitch)
    SwitchCompat dinnerSwitch;

    @BindView(R.id.timeParent)
    RelativeLayout timeParent;

    @BindView(R.id.listParent)
    RelativeLayout listParent;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.cancel)
    Button cancel;

    @BindView(R.id.breakfast)
    TextView breakfast;

    @BindView(R.id.lunch)
    TextView lunch;

    @BindView(R.id.dinner)
    TextView dinner;

    @BindView(R.id.timePicker)
    TimePicker timePicker;

    @BindView(R.id.breakfast_time)
    TextView breakfastTime;

    @BindView(R.id.lunch_time)
    TextView lunchTime;

    @BindView(R.id.dinner_time)
    TextView dinnerTime;

    public int mealTime;
    public static final int NOTSELECTED = 0;
    public static final int BREAKFAST = 1;
    public static final int LUNCH = 2;
    public static final int DINNER = 3;
    Calendar calendar = Calendar.getInstance();

    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            String time = String.valueOf(hour) + ":" + String.valueOf(minute);
            System.out.println(time);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mealTime = NOTSELECTED;

        getSupportActionBar().setTitle("Notifications");
        timeParent.setVisibility(View.GONE);

        final ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(toolbar);

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        SharedPreferences prefs = SharedPrefsHelper.getSharedPrefs(getApplicationContext());

        int[] breakfastHandM = SharedPrefsHelper.getMealTime(this, SharedPrefsHelper.BREAKFAST);
        String toPrint = twentyFourHourTo12(breakfastHandM[0], breakfastHandM[1]);
        if(toPrint == null) {
            breakfastTime.setText("00:00");
        } else {
            breakfastTime.setText(toPrint);
        }

        int[] lunchHandM = SharedPrefsHelper.getMealTime(this, SharedPrefsHelper.LUNCH);
        toPrint = twentyFourHourTo12(lunchHandM[0], lunchHandM[1]);
        if(toPrint == null) {
            lunchTime.setText("00:00");
        } else {
            lunchTime.setText(toPrint);
        }

        int[] dinnerHandM = SharedPrefsHelper.getMealTime(this, SharedPrefsHelper.DINNER);
        toPrint = twentyFourHourTo12(dinnerHandM[0], dinnerHandM[1]);
        if(toPrint == null) {
            dinnerTime.setText("00:00");
        } else {
            dinnerTime.setText(toPrint);
        }

        breakfastSwitch.setChecked(prefs.getBoolean("sendBreakfastNotif", true));
        lunchSwitch.setChecked(prefs.getBoolean("sendLunchNotif", true));
        dinnerSwitch.setChecked(prefs.getBoolean("sendDinnerNotif", true));

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */



        breakfastTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(NotificationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        storeTimeFromTimePicker("breakfast", hour, minute);
                        String toDisplay = twentyFourHourTo12(hour, minute);
                        breakfastTime.setText(toDisplay);
                        runNotifAlarm();
                    }
                }, hour, minute, false).show();
            }
        });

        lunchTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(NotificationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        storeTimeFromTimePicker("lunch", hour, minute);
                        String toDisplay = twentyFourHourTo12(hour, minute);
                        lunchTime.setText(toDisplay);
                        runNotifAlarm();
                    }
                }, hour, minute, false).show();
            }
        });

        dinnerTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(NotificationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        storeTimeFromTimePicker("dinner", hour, minute);
                        String toDisplay = twentyFourHourTo12(hour, minute);
                        dinnerTime.setText(toDisplay);
                        runNotifAlarm();
                    }
                }, hour, minute, false).show();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                // app icon in action bar clicked; go home
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnCheckedChanged(R.id.breakfastSwitch)
    public void breakfeastClicked(SwitchCompat buttonView, boolean isChecked) {
        SharedPrefsHelper.getSharedPrefs(this).edit().putBoolean("sendBreakfastNotif", isChecked).apply();
        System.out.println(isChecked);

        if (isChecked) {
            mealTime = BREAKFAST;
            /*
            timeParent.setVisibility(View.VISIBLE);
            listParent.setVisibility(View.GONE);
            */
        } else {
            mealTime = NOTSELECTED;
            //clear any notification time for this mealTime
        }

        runNotifAlarm();
    }

    @OnCheckedChanged(R.id.lunchSwitch)
    public void lunchClicked(SwitchCompat buttonView, boolean isChecked) {
        SharedPrefsHelper.getSharedPrefs(this).edit().putBoolean("sendLunchNotif", isChecked).apply();
        if (isChecked) {
            mealTime = LUNCH;
            /*
            timeParent.setVisibility(View.VISIBLE);
            listParent.setVisibility(View.GONE);
            */
        } else {
            mealTime = NOTSELECTED;
            //clear any notification time for this mealTime
        }

        runNotifAlarm();
    }

    @OnCheckedChanged(R.id.dinnerSwitch)
    public void dinnerClicked(SwitchCompat buttonView, boolean isChecked) {
        SharedPrefsHelper.getSharedPrefs(this).edit().putBoolean("sendDinnerNotif", isChecked).apply();
        if (isChecked) {
            mealTime = DINNER;
            /*
            timeParent.setVisibility(View.VISIBLE);
            listParent.setVisibility(View.GONE);
            */
        } else {
            mealTime = NOTSELECTED;
            //clear any notification time for this mealTime
        }

        runNotifAlarm();
    }

    /*
    @OnClick(R.id.submit)
    public void submitClicked(Button button){
        mealTime = NOTSELECTED;


        timeParent.setVisibility(View.GONE);
        listParent.setVisibility(View.VISIBLE);
        //figure out time on time picker and associate it with given mealtime
        //don't forget to get am or pm

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        String SHour = mealTime + "Hour";
        String MHour = mealTime + "Minute";
        editor.putInt(SHour, hour);
        editor.putInt(MHour, minute);
        editor.apply();

        System.out.println("time: " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());

    }
    */

    @OnClick(R.id.cancel)
    public void cancelClicked(Button button){

        timeParent.setVisibility(View.GONE);
        listParent.setVisibility(View.VISIBLE);

        switch(mealTime){
            case BREAKFAST:
                breakfastSwitch.toggle();


                break;
            case LUNCH: lunchSwitch.toggle();
                break;
            case DINNER: dinnerSwitch.toggle();
        }

        mealTime = NOTSELECTED;
    }

    public void storeTimeFromTimePicker(String meal, int hour, int minute){

        SharedPreferences preferences = SharedPrefsHelper.getSharedPrefs(this);
        preferences.edit().putInt(meal + "Hour", hour).apply();
        preferences.edit().putInt(meal + "Minute", minute).apply();

        /*String Shour = Integer.toString(hour);
        String Sminute = Integer.toString(minute);
        if(hour < 10){
            Shour = "0" + Shour;
        }
        if(minute < 10){
            Sminute = "0" + Sminute;
        }
        String time = Shour + ":" + Sminute;
        SharedPreferences prefs = SharedPrefsHelper.getSharedPrefs(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(meal, time);
        editor.apply();
        */
    }

    private void runNotifAlarm(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MasterAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
    }

    private String twentyFourHourTo12(int hour, int minute){
        String amorpm = "AM";
        String sMin = "" + minute;
        if(hour > 12) {
            hour -= 12;
            amorpm = "PM";
        }
        String sHour = "" + hour;
        if(minute < 10){
            sMin = "0" + sMin;
        }
        return (sHour + ":" + sMin + " " + amorpm);
    }
}
