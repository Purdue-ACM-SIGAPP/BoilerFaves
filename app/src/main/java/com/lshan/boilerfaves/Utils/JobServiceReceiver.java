package com.lshan.boilerfaves.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lshan on 2/15/2018.
 */

public class JobServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        JobUtil.scheduleJob(context);
    }

}
