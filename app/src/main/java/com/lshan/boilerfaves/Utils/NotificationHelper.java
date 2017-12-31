package com.lshan.boilerfaves.Utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lshan.boilerfaves.Activities.MainActivity;
import com.lshan.boilerfaves.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by lshan on 12/18/2017.
 */

public class NotificationHelper {

    private static NotificationCompat.Builder mBuilder;

    public static void sendNotification(Context context, String title, String content){

        //https://developer.android.com/training/notify-user/build-notification.html#click

        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(title)
                .setContentText(content);

        Intent resultIntent = new Intent(context, MainActivity.class);
        //TODO Actually should have the back stack ... do later
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 1;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if(mNotifyMgr != null) {
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }else{
            Log.e("Notification", "Notification manager is null.");
        }
    }

    public static void scheduleNofication(Context context, long triggerAtMillis, String message, String title){
        Intent intentAlarm = new Intent(context, NotificationAlarmReciever.class);
        intentAlarm.putExtra("message", message);
        intentAlarm.putExtra("title", title);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis,
                PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public static class NotificationAlarmReciever extends BroadcastReceiver{

        //Will execute when alarm is triggered
        @Override
        public void onReceive(Context context, Intent intent) {
            sendNotification(context, intent.getStringExtra("title"),
                    intent.getStringExtra("message"));
        }
    }
}
