package com.lshan.boilerfaves.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Menu;

import com.lshan.boilerfaves.Networking.MenuRetrievalTask;

/**
 * Created by lshan on 3/1/2018.
 */

//This service should send a notification (if it needs to be sent)

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService{

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Context context = getApplicationContext();

        int notificationType = jobParameters.getExtras().getInt("notificationType");
        System.out.println("Notif job service. Type=" + notificationType);

        new MenuRetrievalTask(context, null, notificationType).execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
