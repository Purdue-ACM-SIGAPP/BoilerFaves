package com.lshan.boilerfaves.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import com.lshan.boilerfaves.R;

import java.sql.SQLOutput;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class NotificationActivity extends AppCompatActivity {



    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.breakfastSwitch)
    SwitchCompat breakfastSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setVisibility(View.INVISIBLE);

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

    @OnClick(R.id.breakfastSwitch)
    public void onCheckedChanged(CompoundButton buttonView) {
        System.out.println("got here!!!!");
        if (false) {
            //if 'isChecked' is true do whatever you need...
            System.out.println("Breakfast is checked!!!!!!!");
        } else {
            System.out.println("Breakfast is unchecked!!!!");
        }
    }

}
