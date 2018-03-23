package com.lshan.boilerfaves.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.lshan.boilerfaves.Networking.MenuRetrievalTask;
import com.lshan.boilerfaves.Receivers.NotificationAlarmReceiver;
import com.lshan.boilerfaves.Receivers.MasterAlarmReceiver;

/**
 * Created by lshan on 3/22/2018.
 */

//This service should set three notification alarms (breakfast, lunch dinner) and reschedule the master service for the next day

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MasterJobService extends JobService{
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Context context = getApplicationContext();

        System.out.println("In master job service");

        //Set another alarm for the master
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, MasterAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pendingIntent);

        Intent notifIntent = new Intent(context, NotificationAlarmReceiver.class);

        notifIntent.putExtra("notificationType", MenuRetrievalTask.BREAKFAST_NOTIFICATION);
        PendingIntent breakfastPendingIntent = PendingIntent.getBroadcast(context, 1, notifIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, breakfastPendingIntent);

        notifIntent.putExtra("notificationType", MenuRetrievalTask.LUNCH_NOTIFICATION);
        PendingIntent lunchPendingIntent = PendingIntent.getBroadcast(context, 2, notifIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, lunchPendingIntent);

        notifIntent.putExtra("notificationType", MenuRetrievalTask.DINNER_NOTIFICATION);
        PendingIntent dinnerPendingIntent = PendingIntent.getBroadcast(context, 3, notifIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, dinnerPendingIntent);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
