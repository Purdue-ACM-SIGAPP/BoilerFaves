package com.lshan.boilerfaves.Models;

import android.support.annotation.NonNull;

/**
 * Created by lshan on 12/16/2017.
 */

public class FoodModel implements Comparable{
    public String name;
    public boolean isVegetarian;


    public FoodModel(String name, boolean isVegetarian){
        this.name = name;
        this.isVegetarian = isVegetarian;
    }


    @Override
    public boolean equals(Object obj){
        if (!FoodModel.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        FoodModel other = (FoodModel) obj;
        if(other.name.equals(this.name)){
            return true;
        }else{
            return false;
        }
    }


    @Override
    public int compareTo(@NonNull Object o) {
        FoodModel other = (FoodModel)o;

        return this.name.compareTo(other.name);
    }
}
