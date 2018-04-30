package com.lshan.boilerfaves.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Menu;

import com.lshan.boilerfaves.Activities.MainActivity;
import com.lshan.boilerfaves.Networking.MenuRetrievalTask;
import com.lshan.boilerfaves.Networking.OnNotificationConstructed;
import com.lshan.boilerfaves.Utils.NotificationHelper;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;

/**
 * Created by lshan on 3/1/2018.
 *
 * This service will run the MenuRetrievalTask to generate a notification.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService implements OnNotificationConstructed{

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        int notificationType = jobParameters.getExtras().getInt("notificationType"); // breakfast, lunch, or dinner
        System.out.println("Notif job service. Type=" + notificationType);

        new MenuRetrievalTask(notificationType, SharedPrefsHelper.getFaveList(getApplicationContext()), this).execute();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    /**
     * Callback for sending the notification using the data from MenuRetrievalTask.
     *
     * @param title Notification title
     * @param message Notificaiton message
     * @param id Notification id
     */
    @Override
    public void onNotificationConstructed(String title, String message, int id) {
        System.out.println("In onNotificationConstructed");
        NotificationHelper.sendNotification(getApplicationContext(), title, message, id);
    }
}
