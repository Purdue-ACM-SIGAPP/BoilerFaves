package com.lshan.boilerfaves.Networking;

import android.content.Context;
import android.util.Log;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.lshan.boilerfaves.Models.BreakfastModel;
import com.lshan.boilerfaves.Models.DinnerModel;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Models.LunchModel;
import com.lshan.boilerfaves.Models.MenuModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.http.HTTP;

/**
 * Created by lshan on 1/4/2018.
 */

public class MenuRetrievalTask extends AsyncTask<Void, Void, HashMap<String, HashMap<String, ArrayList<FoodModel>>>>{

    private static final String API_URL = "https://api.hfs.purdue.edu/menus/v1/locations/";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected HashMap<String, HashMap<String, ArrayList<FoodModel>>> doInBackground(Void... voids) {

        String[] diningCourts = {"earhart", "ford", "wiley", "windsor", "hillenbrand"};

        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String date = dateFormat.format(new Date());

        date = "12-01-2017";

        HashMap<String, HashMap<String, ArrayList<FoodModel>>> menus = new HashMap<>();

        for(String court: diningCourts) {

            try {
                URL url = new URL(API_URL + court + "/" + date);
                HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while((line = bufferedReader.readLine()) != null){
                        stringBuilder.append(line);
                    }

                    bufferedReader.close();
                    Gson g = new Gson();
                    MenuModel menu = g.fromJson(stringBuilder.toString(), MenuModel.class);
                    menus.put(court, processMenu(menu));

                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }

        }

        return menus;
    }

    private HashMap<String, ArrayList<FoodModel>> processMenu(MenuModel menuModel){
        ArrayList <FoodModel> breakfast, lunch, dinner;

        breakfast = new ArrayList<>();
        if (menuModel.Breakfast != null) {
            for (BreakfastModel location : menuModel.Breakfast) {
                for (FoodModel food : location.Items) {
                    if (!breakfast.contains(food)) {
                        breakfast.add(food);
                    }
                }
            }
        }

        lunch = new ArrayList<>();
        if (menuModel.Lunch != null) {
            for (LunchModel location : menuModel.Lunch) {
                for (FoodModel food : location.Items) {
                    if (!lunch.contains(food)) {
                        lunch.add(food);
                    }
                }
            }
        }

        dinner = new ArrayList<>();
        if (menuModel.Dinner != null) {
            for (DinnerModel location : menuModel.Dinner) {
                for (FoodModel food : location.Items) {
                    if (!dinner.contains(food)) {
                        dinner.add(food);
                    }
                }
            }
        }


        HashMap<String, ArrayList<FoodModel>> result = new HashMap<>();
        result.put("breakfast", breakfast);
        result.put("lunch", lunch);
        result.put("dinner", dinner);

        return result;
    }

    @Override
    protected void onPostExecute(HashMap<String, HashMap<String, ArrayList<FoodModel>>> menus) {
        System.out.println("here");
    }
}
