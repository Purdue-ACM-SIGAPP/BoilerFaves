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

/*---- The BoilerFaves Notification Cycle ----*/
/*
BoilerFaves uses a few classes to handle notifications. JobServices are for newer devices and
regular services are used for KitKat devices. This class is the entry point for the
cycle. The MasterAlarmReceiver starts the master (job) service, which sets an alarm for each
notification. The master (job) service also sets an alarm (received here) to reschedule itself for
the next day. The notification alarms are received by NotificationAlarmReceiver, which runs the notification
(job) service. The notification (job) service will run a check and display a notification if
necessary.

  |-> MasterAlarmReceiver -> Master Service ---> NotificationAlarmReceiver -> Notification Service (Breakfast)
  |----------------------------------|       |-> NotificationAlarmReceiver -> Notification Service (Lunch)
                                             |-> NotificationAlarmReceiver -> Notification Service (Dinner)
 */

/**
 * Created by lshan on 3/22/2018.
 *
 * The purpose of this receiver is to run the MasterJobService. The master job service needs
 * to run once a day (to schedule fave checks) so we use an alarm to set it for the next day.
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
