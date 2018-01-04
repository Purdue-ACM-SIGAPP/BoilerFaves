package com.lshan.boilerfaves.Utils;

import android.content.Context;
import android.util.Log;

import com.lshan.boilerfaves.Models.BreakfastModel;
import com.lshan.boilerfaves.Models.DinnerModel;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Models.LunchModel;
import com.lshan.boilerfaves.Models.MenuModel;
import com.lshan.boilerfaves.Networking.MenuApi;
import com.lshan.boilerfaves.Networking.MenuApiHelper;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by lshan on 12/31/2017.
 */

public class MenuChecker {
    private Context context;
    private static ArrayList<FoodModel> todayFood;
    private static int menuCompleteCounter;

    public static void initalizeRepeatingCheck() {
        /*

        // Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

    */
    }

    //Returns string array of faves available today
    public static ArrayList<FoodModel> getAvailableFaves(Context context){
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String date = dateFormat.format(new Date());

        date = "12-01-2017";

        String[] diningCourts = {"earhart", "ford", "wiley", "windsor", "hillenbrand"};

        menuCompleteCounter = 0;

        MenuApi menuApi = MenuApiHelper.getInstance();
        List<Observable<MenuModel>> observables = new ArrayList<>();

        for(String diningCourt: diningCourts){
            /*menuApi.getMenu(diningCourt, date)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseData -> {

                    });
                    */
            observables.add(
                    menuApi
                    .getMenu(diningCourt, date)
                    .observeOn(Schedulers.newThread())
                    .subscribeOn(Schedulers.io())
                    );
        }

        for(Observable observable : observables){
            System.out.println("Subscribing");
            observable.subscribe();
        }


        Observable.combineLatest(observables.get(0), observables.get(1), (result1, result2) -> {
            System.out.println("HERE");
            return null;
        });

        Observable.combineLatest(observables, args -> {
            System.out.println("here");
            return null;
        });


        return new ArrayList<>();
    }

    public static void processMenu(){
        System.out.println("DONE");
        for(FoodModel foodModel : todayFood){
            System.out.println(foodModel.Name);
        }
    }

    public static void addFoods(MenuModel menuModel){
        ArrayList <FoodModel> foodList = new ArrayList<>();
        todayFood = new ArrayList<>();
        MenuModel result = menuModel;

        if (result.Breakfast != null) {
            for (BreakfastModel location : result.Breakfast) {
                for (FoodModel food : location.Items) {
                    if (!foodList.contains(food)) {
                        foodList.add(food);
                    }
                }
            }
        }

        if (result.Lunch != null) {
            for (LunchModel location : result.Lunch) {
                for (FoodModel food : location.Items) {
                    if (!foodList.contains(food)) {
                        foodList.add(food);
                    }
                }
            }
        }

        if (result.Dinner != null) {
            for (DinnerModel location : result.Dinner) {
                for (FoodModel food : location.Items) {
                    if (!foodList.contains(food)) {
                        foodList.add(food);
                    }
                }
            }
        }

        addToTodayFood(foodList);

        menuCompleteCounter++;
    }

    public static synchronized void addToTodayFood(ArrayList<FoodModel> foodList){
        for(FoodModel foodModel: foodList){
            if(!todayFood.contains(foodModel)){
                todayFood.add(foodModel);
            }
        }
    }

    public static void callMenuApi(final String diningCourt, final String date) {



        /*MenuApiHelper.getInstance().getMenu(diningCourt, date).enqueue(new Callback<MenuModel>() {
            @Override
            public void onResponse(Call<MenuModel> call, Response<MenuModel> response) {
                Log.i("Retrofit", "Found menu for " + diningCourt + " on " + date);
                ArrayList <FoodModel> foodList = new ArrayList<>();
                MenuModel result = response.body();

                if (result.Breakfast != null) {
                    for (BreakfastModel location : result.Breakfast) {
                        for (FoodModel food : location.Items) {
                            if (!foodList.contains(food)) {

                                foodList.add(food);
                            }
                        }
                    }
                }

                if (result.Lunch != null) {
                    for (LunchModel location : result.Lunch) {
                        for (FoodModel food : location.Items) {
                            if (!foodList.contains(food)) {
                                foodList.add(food);
                            }
                        }
                    }
                }

                if (result.Dinner != null) {
                    for (DinnerModel location : result.Dinner) {
                        for (FoodModel food : location.Items) {
                            if (!foodList.contains(food)) {
                                foodList.add(food);
                            }
                        }
                    }
                }

                addToTodayList(foodList);
                incrementMenuCompleteCounter();
            }

            @Override
            public void onFailure(Call<MenuModel> call, Throwable t) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                    .setTitle("Data retrieval failed")
                    .setMessage("Unable to connect to the Internet")
                    .setCancelable(false)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            callRetrofit();
                        }
                    });
            AlertDialog failure = alertDialogBuilder.create();
            failure.show();

                Log.e("Retrofit", "Unable to connect to api or something");
                Log.e("Retrofit", t.getMessage());

                incrementMenuCompleteCounter();
            }
        });
*/

        }


        synchronized static void incrementMenuCompleteCounter(){
            menuCompleteCounter++;
        }

        synchronized static void addToTodayList(ArrayList<FoodModel> list){
            if(todayFood.isEmpty()){
                for(FoodModel foodModel: list){
                    todayFood.add(foodModel);
                }
            }else {
                for(FoodModel foodModel: list){
                    if(!todayFood.contains(foodModel)){
                        todayFood.add(foodModel);
                    }
                }

            }
        }

    }


