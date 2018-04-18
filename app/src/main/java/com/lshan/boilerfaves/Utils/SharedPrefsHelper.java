package com.lshan.boilerfaves.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lshan.boilerfaves.Models.FoodModel;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by lshan on 12/17/2017.
 */

public class SharedPrefsHelper {

    public static final int BREAKFAST = 0;
    public static final int LUNCH = 1;
    public static final int DINNER = 2;

    public static SharedPreferences getSharedPrefs(Context context){
        return context.getSharedPreferences("com.lshan.boilerfaves", Context.MODE_PRIVATE);
    }

    public static int[] getMealTime(Context context, int meal){
        SharedPreferences prefs = getSharedPrefs(context);
        int[] result = new int[2];
        int hour = 0, min = 0;
        switch(meal){
            case BREAKFAST:
                hour = prefs.getInt("breakfastHour", 6);
                min = prefs.getInt("breakfastMinute", 0);
                break;
            case LUNCH:
                hour = prefs.getInt("lunchHour", 11);
                min = prefs.getInt("lunchMinute", 0);
                break;
            case DINNER:
                hour = prefs.getInt("dinnerHour", 16);
                min = prefs.getInt("dinnerMinute", 0);
                break;
        }

        result[0] = hour;
        result[1] = min;

        return result;
    }

    public static void storeFaveList(List<FoodModel> foodList, Context context){
        SharedPreferences sharedPreferences = getSharedPrefs(context);
        Gson gson = new Gson();
        String json = gson.toJson(foodList);
        sharedPreferences.edit().putString("FaveList", json).apply();
}

    public static List<FoodModel> getFaveList(Context context){
        SharedPreferences sharedPreferences = getSharedPrefs(context);
        Type type = new TypeToken<List<FoodModel>>(){}.getType();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("FaveList", "FailedFaveList");

        if(json.equals("FailedFaveList")){
            return null;
        }

        List<FoodModel> foods = gson.fromJson(json, type);
        return foods;
    }

}
