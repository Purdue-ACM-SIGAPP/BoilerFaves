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
import android.os.PersistableBundle;
import android.util.Log;

import com.lshan.boilerfaves.Networking.MenuRetrievalTask;
import com.lshan.boilerfaves.Services.NotificationJobService;
import com.lshan.boilerfaves.Services.NotificationService;
import com.lshan.boilerfaves.Utils.TimeHelper;

/**
 * Created by lshan on 2/27/2018.
 */

public class NotificationAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("In notif alarm receiver");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            JobScheduler jobScheduler =
                    (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            ComponentName name = new ComponentName(context, NotificationJobService.class);

            PersistableBundle bundle = new PersistableBundle();
            int notificiationType = intent.getExtras().getInt("notificationType");
            bundle.putInt("notificationType", notificiationType);

            jobScheduler.schedule(new JobInfo.Builder(1, name)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setExtras(bundle)
                    .build());
        }else{
            Intent serviceIntent = new Intent(context, NotificationService.class);
            context.startService(serviceIntent);
        }

    }
}
