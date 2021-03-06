package com.lshan.boilerfaves.Utils;

import java.util.Calendar;

/**
 * Created by lshan on 1/12/2018.
 */

public class TimeHelper {


    //Returns the number of milliseconds until the given time.
    //Uses 24 hour time. Starts at the current time.
    public static long getMillisUntil(int hour, int minute){

        //https://stackoverflow.com/questions/11989555/get-milliseconds-until-midnight
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long howMany = (c.getTimeInMillis()-System.currentTimeMillis());

        if(howMany <= 0){
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DATE, 1);
            tomorrow.set(Calendar.HOUR_OF_DAY, hour);
            tomorrow.set(Calendar.MINUTE, minute);
            tomorrow.set(Calendar.SECOND, 0);
            tomorrow.set(Calendar.MILLISECOND, 0);
            long tomorrowTime = tomorrow.getTimeInMillis()-System.currentTimeMillis();
            return tomorrowTime;
        }

        return howMany;
    }

}
