package com.lshan.boilerfaves.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lshan.boilerfaves.Networking.MenuRetrievalTask;
import com.lshan.boilerfaves.Receivers.NotificationAlarmReceiver;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;

/**
 * Created by lshan on 3/1/2018.
 */

public class NotificationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("In Service");

        Context context = getApplicationContext();

        int notificationType = 0;
        if(intent.getExtras() == null){
            Log.e("Notification Service", "Bundle was null");
        }else {
            notificationType = intent.getExtras().getInt("notificationType");
        }

        System.out.println("Notif service (not job service). Type=" + notificationType);

        //new MenuRetrievalTask(context, null, MenuRetrievalTask.NO_NOTIFICATION).execute();

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, NotificationAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }
}
