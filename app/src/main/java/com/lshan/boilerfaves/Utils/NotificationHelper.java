package com.lshan.boilerfaves.Utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lshan.boilerfaves.Activities.MainActivity;
import com.lshan.boilerfaves.R;

import java.util.GregorianCalendar;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by lshan on 12/18/2017.
 */

public class NotificationHelper {

    //These are for the notification channels/code numbers
public static final int BREAKFAST = 1, LUNCH = 2, DINNER = 3;

    private static NotificationCompat.Builder mBuilder;

    public static void sendNotification(Context context, String title, String content, int notificationID){

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Need to do channel stuff for android oreo ugh
            String CHANNEL_ID = "boiler_faves_01";
            CharSequence name = "BoilerFaves";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            mNotifyMgr.createNotificationChannel(channel);

            Intent intent = new Intent(context, MainActivity.class);
            Bundle b  = new Bundle();
            b.putInt("notificationID", notificationID);
            intent.putExtras(b);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(contentIntent)
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            if (mNotifyMgr != null) {
                mNotifyMgr.notify(notificationID, notification);
            } else {
                Log.e("Notification", "Notification manager is null.");
            }

        } else {

            //https://developer.android.com/training/notify-user/build-notification.html#click
            mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(title)
                    .setContentText(content);

            Intent resultIntent = new Intent(context, MainActivity.class);
            Bundle b = new Bundle();
            b.putInt("notificationID", notificationID);
            resultIntent.putExtras(b);

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

            Notification notification = mBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            if (mNotifyMgr != null) {
                System.out.println(notificationID);
                mNotifyMgr.notify(notificationID, notification);
            } else {
                Log.e("Notification", "Notification manager is null.");
            }
        }
    }

    public static void scheduleNofication(Context context, long delayInMillis, String message, String title, int notificationID){
        Intent intentAlarm = new Intent(context, NotificationAlarmReciever.class);
        intentAlarm.putExtra("message", message);
        intentAlarm.putExtra("title", title);
        intentAlarm.putExtra("notificationID", notificationID);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Long triggerAtMillis = new GregorianCalendar().getTimeInMillis() + delayInMillis;
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis,
                PendingIntent.getBroadcast(context, notificationID, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public static class NotificationAlarmReciever extends BroadcastReceiver{

        //Will execute when alarm is triggered
        @Override
        public void onReceive(Context context, Intent intent) {
            sendNotification(context, intent.getStringExtra("title"),
                    intent.getStringExtra("message"), intent.getIntExtra("notificationID",0));
        }
    }
}
