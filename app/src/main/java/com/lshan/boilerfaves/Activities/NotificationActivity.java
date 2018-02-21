package com.lshan.boilerfaves.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.EventLog;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.lshan.boilerfaves.R;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;

import java.sql.SQLOutput;

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

    @BindView(R.id.timePicker)
    TimePicker timePicker;


    public int mealTime;
    public static final int NOTSELECTED = 0;
    public static final int BREAKFAST = 1;
    public static final int LUNCH = 2;
    public static final int DINNER = 3;


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

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

    }

    @OnCheckedChanged(R.id.breakfastSwitch)
    public void breakfeastClicked(SwitchCompat buttonView, boolean isChecked) {
        if (isChecked) {
            mealTime = BREAKFAST;
            timeParent.setVisibility(View.VISIBLE);
            listParent.setVisibility(View.GONE);
        } else {
            mealTime = NOTSELECTED;
            //clear any notification time for this mealTime
        }
    }

    @OnCheckedChanged(R.id.lunchSwitch)
    public void lunchClicked(SwitchCompat buttonView, boolean isChecked) {
        if (isChecked) {
            mealTime = LUNCH;
            timeParent.setVisibility(View.VISIBLE);
            listParent.setVisibility(View.GONE);
        } else {
            mealTime = NOTSELECTED;
            //clear any notification time for this mealTime
        }
    }

    @OnCheckedChanged(R.id.dinnerSwitch)
    public void dinnerClicked(SwitchCompat buttonView, boolean isChecked) {
        if (isChecked) {
            mealTime = DINNER;
            timeParent.setVisibility(View.VISIBLE);
            listParent.setVisibility(View.GONE);
        } else {
            mealTime = NOTSELECTED;
            //clear any notification time for this mealTime
        }
    }

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
        editor.putInt("Hour", hour);
        editor.putInt("Minute", minute);
        editor.apply();

        System.out.println("time: " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
    }

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

}
