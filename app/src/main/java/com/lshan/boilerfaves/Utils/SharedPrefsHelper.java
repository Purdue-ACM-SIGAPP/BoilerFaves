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
    public static SharedPreferences getSharedPrefs(Context context){
        return context.getSharedPreferences("com.lshan.boilerfaves", Context.MODE_PRIVATE);
    }

    public static void storeFoodList(List<FoodModel> foodList, Context context){
        SharedPreferences sharedPreferences = getSharedPrefs(context);
        Gson gson = new Gson();
        String json = gson.toJson(foodList);
        sharedPreferences.edit().putString("FaveList", json).commit();
    }

    public static List<FoodModel> getFoodList(Context context){
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
