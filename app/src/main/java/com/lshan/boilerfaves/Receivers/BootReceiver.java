package com.lshan.boilerfaves.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lshan on 2/27/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("IN BOOT RECEIVER");
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("bootRun", true);
        context.sendBroadcast(alarmIntent);
    }
}
