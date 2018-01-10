package com.lshan.boilerfaves.Models;

import java.util.ArrayList;

/**
 * Created by lshan on 1/10/2018.
 */

public class DiningCourtMenu {
    private ArrayList<FoodModel> breakfast;
    private ArrayList<FoodModel> lunch;
    private ArrayList<FoodModel> dinner;
    private String courtName; //Name of the dining court

    public DiningCourtMenu(String courtName, ArrayList<FoodModel> breakfast, ArrayList<FoodModel> lunch, ArrayList<FoodModel> dinner){
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.courtName = courtName;
    }


    public String getCourtName() {
        return courtName;
    }

    public ArrayList<FoodModel> getBreakfast() {
        return breakfast;
    }

    public ArrayList<FoodModel> getLunch() {
        return lunch;
    }

    public ArrayList<FoodModel> getDinner() {
        return dinner;
    }
}
