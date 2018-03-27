package com.lshan.boilerfaves.Receivers;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.lshan.boilerfaves.Services.MasterJobService;
import com.lshan.boilerfaves.Services.MasterService;
import com.lshan.boilerfaves.Services.NotificationJobService;
import com.lshan.boilerfaves.Services.NotificationService;

/**
 * Created by lshan on 3/22/2018.
 */

public class MasterAlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("In master alarm receiver");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            JobScheduler jobScheduler =
                    (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            ComponentName name = new ComponentName(context, MasterJobService.class);
            jobScheduler.schedule(new JobInfo.Builder(1, name)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build());
        }else{
            Intent serviceIntent = new Intent(context, MasterService.class);
            context.startService(serviceIntent);
        }

    }

}
