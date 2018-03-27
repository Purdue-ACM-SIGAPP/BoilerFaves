package com.lshan.boilerfaves.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lshan.boilerfaves.Networking.MenuRetrievalTask;
import com.lshan.boilerfaves.Receivers.MasterAlarmReceiver;
import com.lshan.boilerfaves.Receivers.NotificationAlarmReceiver;
import com.lshan.boilerfaves.Utils.TimeHelper;

/**
 * Created by lshan on 3/22/2018.
 */

public class MasterService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //This serves the same purpose as MasterJobService but it works on phones that don't run JobScheduler
    //This service should set an alarm for each meal
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = getApplicationContext();

        System.out.println("In master service (not job service)");

        //Set another alarm for the master (should run at 12:20 AM and schedule notification alarms for the next day)
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, MasterAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);

        Intent notifIntent = new Intent(context, NotificationAlarmReceiver.class);

        notifIntent.putExtra("notificationType", MenuRetrievalTask.BREAKFAST_NOTIFICATION);
        PendingIntent breakfastPendingIntent = PendingIntent.getBroadcast(context, 1, notifIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), breakfastPendingIntent);

        notifIntent.putExtra("notificationType", MenuRetrievalTask.LUNCH_NOTIFICATION);
        PendingIntent lunchPendingIntent = PendingIntent.getBroadcast(context, 2, notifIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 4000, lunchPendingIntent);

        notifIntent.putExtra("notificationType", MenuRetrievalTask.DINNER_NOTIFICATION);
        PendingIntent dinnerPendingIntent = PendingIntent.getBroadcast(context, 3, notifIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 7000, dinnerPendingIntent);


        return super.onStartCommand(intent, flags, startId);
    }
}
