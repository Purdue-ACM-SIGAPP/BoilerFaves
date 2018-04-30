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
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;
import com.lshan.boilerfaves.Utils.TimeHelper;

/**
 * Created by lshan on 3/22/2018.
 *
 * The purpose of this JobService is to set (up to) three notification checks (breakfast,
 * lunch, and dinner) for later in the day. The service also sets an alarm to reschedule itself
 * for the next day.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MasterJobService extends JobService{
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Context context = getApplicationContext();

        System.out.println("In master job service");

        //Set another alarm for the master (should run at 12:20 AM and schedule notification alarms for the next day)
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, MasterAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TimeHelper.getMillisUntil(0, 20), pendingIntent);

        Intent notifIntent = new Intent(context, NotificationAlarmReceiver.class);

        // Breakfast
        // Before scheduling a notification, check if the user has that notification turned on
        boolean sendBreakfast = SharedPrefsHelper.getSharedPrefs(context).getBoolean("sendBreakfastNotif", true);
        System.out.println(sendBreakfast);
        if(sendBreakfast) {
            int breakfastHour = SharedPrefsHelper.getSharedPrefs(context).getInt("breakfastHour", 6);
            int breakfastMinute = SharedPrefsHelper.getSharedPrefs(context).getInt("breakfastMinute", 0);
            notifIntent.putExtra("notificationType", MenuRetrievalTask.BREAKFAST_NOTIFICATION);
            PendingIntent breakfastPendingIntent = PendingIntent.getBroadcast(context, 1, notifIntent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TimeHelper.getMillisUntil(breakfastHour, breakfastMinute), breakfastPendingIntent);
        }

        //Lunch
        boolean sendLunch = SharedPrefsHelper.getSharedPrefs(context).getBoolean("sendLunchNotif", true);
        if(sendLunch) {
            int lunchHour = SharedPrefsHelper.getSharedPrefs(context).getInt("lunchHour", 11);
            int lunchMinute = SharedPrefsHelper.getSharedPrefs(context).getInt("lunchMinute", 0);
            notifIntent.putExtra("notificationType", MenuRetrievalTask.LUNCH_NOTIFICATION);
            PendingIntent lunchPendingIntent = PendingIntent.getBroadcast(context, 2, notifIntent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TimeHelper.getMillisUntil(lunchHour, lunchMinute), lunchPendingIntent);
        }

        //Dinner
        boolean sendDinner = SharedPrefsHelper.getSharedPrefs(context).getBoolean("sendDinnerNotif", true);
        if(sendDinner) {
            int dinnerHour = SharedPrefsHelper.getSharedPrefs(context).getInt("dinnerHour", 16);
            int dinnerMinute = SharedPrefsHelper.getSharedPrefs(context).getInt("dinnerMinute", 0);
            notifIntent.putExtra("notificationType", MenuRetrievalTask.DINNER_NOTIFICATION);
            PendingIntent dinnerPendingIntent = PendingIntent.getBroadcast(context, 3, notifIntent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TimeHelper.getMillisUntil(dinnerHour, dinnerMinute), dinnerPendingIntent);
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
