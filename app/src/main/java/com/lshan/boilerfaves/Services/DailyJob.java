package com.lshan.boilerfaves.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.annotations.Until;
import com.lshan.boilerfaves.Utils.JobUtil;

/**
 * Created by lshan on 2/15/2018.
 */

public class DailyJob extends JobService{

    private static final String TAG = "DailyService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job Started!");

        Intent service = new Intent(getApplicationContext(), JobService.class);
        getApplicationContext().startService(service);
        JobUtil.scheduleJob(getApplicationContext()); //This reschedules the job

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
