package com.lshan.boilerfaves.Networking;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.os.AsyncTask;
import android.view.View;

import com.google.gson.Gson;
import com.lshan.boilerfaves.Activities.MainActivity;
import com.lshan.boilerfaves.Adapters.FoodAdapter;
import com.lshan.boilerfaves.Models.BreakfastModel;
import com.lshan.boilerfaves.Models.DinnerModel;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Models.LunchModel;
import com.lshan.boilerfaves.Models.DiningCourtMenu;
import com.lshan.boilerfaves.Models.MenuModel;
import com.lshan.boilerfaves.R;
import com.lshan.boilerfaves.Utils.NotificationHelper;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;
import com.lshan.boilerfaves.Utils.TimeHelper;

import java.io.BufferedReader;
import java.io.IOException;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * Created by lshan on 1/4/2018.
 */

public class MenuRetrievalTask extends AsyncTask<Void, Void, ArrayList<DiningCourtMenu>>{

    private static final String API_URL = "https://api.hfs.purdue.edu/menus/v1/locations/";
    private RecyclerView mainRecyclerView;
    private Context context;



    public MenuRetrievalTask(Context context, RecyclerView mainRecyclerView){
        this.context = context;
        this.mainRecyclerView = mainRecyclerView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<DiningCourtMenu> doInBackground(Void... voids) {

        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String date = dateFormat.format(new Date());

        ArrayList<DiningCourtMenu> diningCourtMenus = new ArrayList<>();

        try {
            //Synchronous retrofit call
            Response<List<String>> locationsResponse = MenuApiHelper.getInstance().getLocations().execute();
            if (locationsResponse.isSuccessful()) {
                for (String diningCourt : locationsResponse.body()) {
                    Response<MenuModel> menuResponse = MenuApiHelper.getInstance().getMenu(diningCourt, date).execute();
                    diningCourtMenus.add(new DiningCourtMenu(diningCourt, menuResponse.body()));
                }
            } else {
                Log.e("Retrofit synch call: ", locationsResponse.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Retrofit synch call: ", e.getMessage());
        }

        return diningCourtMenus;
    }

    @Override
    protected void onPostExecute(ArrayList<DiningCourtMenu> menus) {

        //TODO Do I need to move the notifications stuff to doInBackground so it can be called without passing the recyclerView?

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

                String courtName = menu.getCourtName();
                if(menu.getBreakfast().size() > 0){

                    //Needed to update availability on the cardViews
                    for(FoodModel foodModel: menu.getBreakfast()){
                        faves.get(faves.indexOf(foodModel)).setAvailable(true);

                        addAvailableCourt("Breakfast", courtName, faves.get(faves.indexOf(foodModel)));
                    }

                    breakfastMessageBuilder.append(courtName + " ");
                    breakfastAvailable = true;
                }

                if(menu.getLunch().size() > 0){

                    for(FoodModel foodModel: menu.getLunch()){
                        faves.get(faves.indexOf(foodModel)).setAvailable(true);

                        addAvailableCourt("Lunch", courtName, faves.get(faves.indexOf(foodModel)));
                    }

                    lunchMessageBuilder.append(courtName + " ");
                    lunchAvailable = true;
                }

                if(menu.getDinner().size() > 0){

                    for(FoodModel foodModel: menu.getDinner()){
                        faves.get(faves.indexOf(foodModel)).setAvailable(true);

                        addAvailableCourt("Dinner", courtName, faves.get(faves.indexOf(foodModel)));
                    }

                    dinnerMessageBuilder.append(courtName + " ");
                    dinnerAvailable = true;
                }

                if(mainRecyclerView != null) {
                    FoodAdapter foodAdapter = (FoodAdapter) mainRecyclerView.getAdapter();
                    foodAdapter.setFoods(faves);
                    foodAdapter.notifyDataSetChanged();
                }

                //Need to call this so when main activity resumes it remembers availability
                SharedPrefsHelper.storeFaveList(faves, context);

            }
        }

        if(breakfastAvailable){
            breakfastMessageBuilder.append("for breakfast!");
            NotificationHelper.scheduleNofication(context, TimeHelper.getMillisUntil(6, 0), breakfastMessageBuilder.toString(), "Faves For Breakfast", NotificationHelper.BREAKFAST);
        }

        if(lunchAvailable){
            lunchMessageBuilder.append("for lunch!");
            NotificationHelper.scheduleNofication(context, TimeHelper.getMillisUntil(11, 0), lunchMessageBuilder.toString(), "Faves For Lunch", NotificationHelper.LUNCH);
        }

        if(dinnerAvailable){
            dinnerMessageBuilder.append("for dinner!");
            NotificationHelper.scheduleNofication(context, TimeHelper.getMillisUntil(16, 0), dinnerMessageBuilder.toString(), "Faves For Dinner", NotificationHelper.DINNER);
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

    private void addAvailableCourt(String meal, String court, FoodModel foodModel){
        HashMap<String, ArrayList<String>> availableCourts = foodModel.getAvailableCourts();
        if(availableCourts == null){
            availableCourts = new HashMap<>();

            availableCourts.put(meal, new ArrayList<>());
            availableCourts.get(meal).add(court);

            foodModel.setAvailableCourts(availableCourts);
        }else{

            if(!availableCourts.containsKey(meal)){
                availableCourts.put(meal, new ArrayList<>());
                availableCourts.get(meal).add(court);
            }else{
                if(!(availableCourts.get(meal).contains(court))){
                    availableCourts.get(meal).add(court);
                }
            }
        }


    }
}
