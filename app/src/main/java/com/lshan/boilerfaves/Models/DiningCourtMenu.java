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

    /**
     * Used to convert the results of the api call into a more usable DiningCourtMenu object.
     * @param courtName The dining court's name.
     * @param model The MenuModel generated from the api call for the dining court.
     */
    public DiningCourtMenu(String courtName, MenuModel model){
        this.courtName = courtName;

        ArrayList<FoodModel> newBreakfast = new ArrayList<>();
        if(model.Breakfast != null) {
            for (BreakfastModel bModel : model.Breakfast) {
                for (FoodModel foodModel : bModel.Items) {
                    if (!newBreakfast.contains(foodModel)) {
                        newBreakfast.add(foodModel);
                    }
                }
            }
        }
        this.breakfast = newBreakfast;


        ArrayList<FoodModel> newLunch = new ArrayList<>();
        if(model.Lunch != null) {
            for (LunchModel lModel : model.Lunch) {
                for (FoodModel foodModel : lModel.Items) {
                    if (!newLunch.contains(foodModel)) {
                        newLunch.add(foodModel);
                    }
                }
            }
        }
        this.lunch = newLunch;

        ArrayList<FoodModel> newDinner = new ArrayList<>();
        if(model.Dinner != null) {
            for (DinnerModel dModel : model.Dinner) {
                for (FoodModel foodModel : dModel.Items) {
                    if (!newDinner.contains(foodModel)) {
                        newDinner.add(foodModel);
                    }
                }
            }
        }
        this.dinner = newDinner;

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
