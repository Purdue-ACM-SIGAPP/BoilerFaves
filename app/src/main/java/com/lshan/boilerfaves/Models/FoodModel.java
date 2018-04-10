package com.lshan.boilerfaves.Models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lshan on 12/16/2017.
 */


public class FoodModel implements Comparable<FoodModel>{

    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("IsVegetarian")
    @Expose
    private boolean IsVegetarian;

    public boolean isAvailable;
    //<Meal, List of courts food is available for that meal>
    public HashMap<String, ArrayList<String>> availableCourts;


    public FoodModel(){
        isAvailable =  false;
        availableCourts = null;
    }

    public FoodModel(SelectFoodModel selectFoodModel){
        this.Name = selectFoodModel.getName();
        if(selectFoodModel.isVegetarian() == 1){
            this.IsVegetarian = true;
        }else{
            this.IsVegetarian = false;
        }
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }


    public boolean isAvailable(){
        return isAvailable;
    }


    public void setAvailableCourts(HashMap<String, ArrayList<String>> availableCourts){
        this.availableCourts = availableCourts;
    }

    public HashMap<String, ArrayList<String>> getAvailableCourts() {
        return availableCourts;
    }

    @Override
    public boolean equals(Object obj){
        if (!FoodModel.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        FoodModel other = (FoodModel) obj;
        if(other.getName().equals(this.Name)){
            return true;
        }else{
            return false;
        }
    }


    @Override
    public int compareTo(@NonNull FoodModel other) {
        return this.Name.compareTo(other.getName());
    }

    public String getName() {
        return Name;
    }
}
