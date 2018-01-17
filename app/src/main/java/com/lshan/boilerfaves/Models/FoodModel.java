package com.lshan.boilerfaves.Models;

import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lshan on 12/16/2017.
 */


public class FoodModel implements Comparable<FoodModel>{

    public String Name;

    public boolean IsVegetarian;
    public boolean isAvailable;
    public HashMap<String, ArrayList<String>> availableCourts;


    public FoodModel(){
        isAvailable =  false;
        availableCourts = null;
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
        if(other.Name.equals(this.Name)){
            return true;
        }else{
            return false;
        }
    }


    @Override
    public int compareTo(@NonNull FoodModel other) {
        return this.Name.compareTo(other.Name);
    }
}
