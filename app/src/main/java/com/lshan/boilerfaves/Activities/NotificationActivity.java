package com.lshan.boilerfaves.Activities;

import android.content.Context;
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

        String toPrint = prefs.getString("breakfast", null);
        if(toPrint == null) {
            breakfastTime.setText("00:00");
        } else {
            breakfastTime.setText(toPrint);
        }

        toPrint = prefs.getString("lunch", null);
        if(toPrint == null) {
            lunchTime.setText("00:00");
        } else {
            lunchTime.setText(toPrint);
        }

        toPrint = prefs.getString("dinner", null);
        if(toPrint == null) {
            dinnerTime.setText("00:00");
        } else {
            dinnerTime.setText(toPrint);
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */



        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(NotificationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String amorpm = "AM";
                        if(hour > 12){
                            hour-=12;
                            amorpm = "PM";
                        }
                        String sMinute = "" + minute;
                        if(minute < 10) {
                            sMinute = "0" + minute;
                        }
                        System.out.println("Hour: " + hour);
                        System.out.println("Minute: " + minute);
                        storeTimeFromTimePicker("breakfast", hour, minute);
                        breakfastTime.setText(hour + ":" + sMinute + " " + amorpm);
                    }
                }, hour, minute, false).show();
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(NotificationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String amorpm = "AM";
                        if(hour > 12){
                            hour -= 12;
                            amorpm = "PM";
                        }
                        String sMinute = "" + minute;
                        if(minute < 10) {
                            sMinute = "0" + minute;
                        }
                        System.out.println("Hour: " + hour);
                        System.out.println("Minute: " + minute);
                        //lunchTime.setText(hour + ":" + minute);
                        storeTimeFromTimePicker("lunch", hour, minute);
                        lunchTime.setText(hour + ":" + sMinute + " " + amorpm);
                    }
                }, hour, minute, false).show();
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(NotificationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String amorpm = "AM";
                        if(hour > 12){
                            hour -= 12;
                            amorpm = "PM";
                        }
                        String sMinute = "" + minute;
                        if(minute < 10) {
                            sMinute = "0" + minute;
                        }
                        System.out.println("Hour: " + hour);
                        System.out.println("Minute: " + minute);
                        //dinnerTime.setText(hour + ":" + minute);
                        storeTimeFromTimePicker("dinner", hour, minute);
                        dinnerTime.setText(hour + ":" + sMinute + " " + amorpm);
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
    }

    @OnCheckedChanged(R.id.lunchSwitch)
    public void lunchClicked(SwitchCompat buttonView, boolean isChecked) {
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
    }

    @OnCheckedChanged(R.id.dinnerSwitch)
    public void dinnerClicked(SwitchCompat buttonView, boolean isChecked) {
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
            case BREAKFAST: breakfastSwitch.toggle();
                break;
            case LUNCH: lunchSwitch.toggle();
                break;
            case DINNER: dinnerSwitch.toggle();
        }

        mealTime = NOTSELECTED;
    }

    public void storeTimeFromTimePicker(String meal, int hour, int minute){
        String Shour = Integer.toString(hour);
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
    }
}
