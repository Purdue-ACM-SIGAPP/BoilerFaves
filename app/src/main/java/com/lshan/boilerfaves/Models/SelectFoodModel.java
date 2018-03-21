package com.lshan.boilerfaves.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lshan.boilerfaves.Networking.SelectionRetrievalTask;

/**
 * Created by lshan on 3/20/2018.
 */

public class SelectFoodModel {
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("IsVegetarian")
    @Expose
    private int IsVegetarian;

    public SelectFoodModel(){

    }

    public String getName() {
        return Name;
    }

    public int isVegetarian() {
        return IsVegetarian;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setVegetarian(int vegetarian) {
        IsVegetarian = vegetarian;
    }
}
