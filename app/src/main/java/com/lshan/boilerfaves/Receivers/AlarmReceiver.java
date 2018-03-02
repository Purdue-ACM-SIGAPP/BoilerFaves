package com.lshan.boilerfaves.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.lshan.boilerfaves.Networking.MenuRetrievalTask;
import com.lshan.boilerfaves.Services.NotificationJobService;
import com.lshan.boilerfaves.Services.NotificationService;
import com.lshan.boilerfaves.Utils.TimeHelper;

/**
 * Created by lshan on 2/27/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();

        if(extras.getBoolean("bootRun")){
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, TimeHelper.getMillisUntil(1, 0), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        System.out.println("In alarm manager");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            JobScheduler jobScheduler =
                    (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            ComponentName name = new ComponentName(context, NotificationJobService.class);
            jobScheduler.schedule(new JobInfo.Builder(1, name)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build());
        }else{
            Intent serviceIntent = new Intent(context, NotificationService.class);
            context.startService(serviceIntent);
        }


    }
}
