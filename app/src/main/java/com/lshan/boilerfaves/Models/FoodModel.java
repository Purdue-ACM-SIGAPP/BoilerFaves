package com.lshan.boilerfaves.Models;

import android.support.annotation.NonNull;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by lshan on 12/16/2017.
 */


public class FoodModel implements Comparable<FoodModel>{

    public String Name;

    public boolean IsVegetarian;


    public FoodModel(){}


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
