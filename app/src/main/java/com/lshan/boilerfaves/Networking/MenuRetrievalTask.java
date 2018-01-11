package com.lshan.boilerfaves.Networking;

import android.content.Context;
import android.util.Log;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.lshan.boilerfaves.Models.BreakfastModel;
import com.lshan.boilerfaves.Models.DinnerModel;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Models.LunchModel;
import com.lshan.boilerfaves.Models.DiningCourtMenu;
import com.lshan.boilerfaves.Models.MenuModel;
import com.lshan.boilerfaves.Utils.NotificationHelper;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;

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
import java.util.Map;

/**
 * Created by lshan on 1/4/2018.
 */

public class MenuRetrievalTask extends AsyncTask<Void, Void, ArrayList<DiningCourtMenu>>{

    private static final String API_URL = "https://api.hfs.purdue.edu/menus/v1/locations/";
    private Context context;


    public MenuRetrievalTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<DiningCourtMenu> doInBackground(Void... voids) {

        String[] diningCourts = {"earhart", "ford", "wiley", "windsor", "hillenbrand"};

        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String date = dateFormat.format(new Date());

        ArrayList<DiningCourtMenu> menus = new ArrayList<>();

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
                    menus.add(processMenu(court, menu));

                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }

        }

        return menus;
    }

    private DiningCourtMenu processMenu(String courtName, MenuModel menuModel){
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


        DiningCourtMenu result = new DiningCourtMenu(courtName, breakfast, lunch, dinner);

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<DiningCourtMenu> menus) {
        List<FoodModel> faves = SharedPrefsHelper.getFaveList(context);
        ArrayList<DiningCourtMenu> availableFaves = new ArrayList<>();

        for(DiningCourtMenu menu: menus){
            availableFaves.add(checkForFaves(faves, menu));
        }

        StringBuilder breakfastMessageBuilder = new StringBuilder().append("Faves available at ");
        StringBuilder lunchMessageBuilder = new StringBuilder().append("Faves available at ");
        StringBuilder dinnerMessageBuilder = new StringBuilder().append("Faves available at ");

        boolean breakfastAvailable = false, lunchAvailable = false, dinnerAvailable = false;

        for(DiningCourtMenu menu : availableFaves){
            if(menu != null){
                if(menu.getBreakfast().size() > 0){
                    breakfastMessageBuilder.append(menu.getCourtName() + " ");
                    breakfastAvailable = true;
                }

                if(menu.getLunch().size() > 0){
                    lunchMessageBuilder.append(menu.getCourtName() + " ");
                    lunchAvailable = true;
                }

                if(menu.getDinner().size() > 0){
                    dinnerMessageBuilder.append(menu.getCourtName() + " ");
                    dinnerAvailable = true;
                }
            }
        }

        if(breakfastAvailable){
            breakfastMessageBuilder.append("for breakfast!");
            NotificationHelper.scheduleNofication(context, 0, breakfastMessageBuilder.toString(), "Faves For Breakfast", NotificationHelper.BREAKFAST);
        }

        if(lunchAvailable){
            lunchMessageBuilder.append("for lunch!");
            NotificationHelper.scheduleNofication(context, 0, lunchMessageBuilder.toString(), "Faves For Lunch", NotificationHelper.LUNCH);
        }

        if(dinnerAvailable){
            dinnerMessageBuilder.append("for dinner!");
            NotificationHelper.scheduleNofication(context, 0, dinnerMessageBuilder.toString(), "Faves For Dinner", NotificationHelper.DINNER);
        }

    }

    //Returns a DiningCourtMenu with only that court's available faves
    //If a court has no available faves, returns null
    private DiningCourtMenu checkForFaves(List<FoodModel> faves, DiningCourtMenu menu){
        ArrayList<FoodModel> breakfast, lunch, dinner;

        breakfast = new ArrayList<>();
        for(FoodModel foodModel: menu.getBreakfast()){
            if(faves.contains(foodModel)){
                breakfast.add(foodModel);
            }
        }

        lunch = new ArrayList<>();
        for(FoodModel foodModel: menu.getLunch()){
            if(faves.contains(foodModel)){
                lunch.add(foodModel);
            }
        }

        dinner = new ArrayList<>();
        for(FoodModel foodModel: menu.getDinner()){
            if(faves.contains(foodModel)){
                dinner.add(foodModel);
            }
        }

        if(breakfast.size() == 0 && lunch.size() == 0 && dinner.size() == 0){
            return null;
        }else{
            return new DiningCourtMenu(menu.getCourtName(), breakfast, lunch, dinner);
        }

    }
}
