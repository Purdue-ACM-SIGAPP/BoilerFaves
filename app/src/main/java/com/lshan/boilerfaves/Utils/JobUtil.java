package com.lshan.boilerfaves.Utils;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.lshan.boilerfaves.Services.DailyJob;

/**
 * Created by lshan on 2/15/2018.
 */

public class JobUtil {
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, DailyJob.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(builder.build());

        if(resultCode == JobScheduler.RESULT_SUCCESS){
            System.out.println("Job Success");
        }else{
            System.out.println("Job Failure");
        }
    }
}
