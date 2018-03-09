package com.lshan.boilerfaves.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lshan on 3/8/2018.
 */

public class POJOFoodModel {

    private int id;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("isVegetarian")
    @Expose
    private Integer isVegetarian;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsVegetarian() {
        return isVegetarian;
    }

    public void setIsVegetarian(Integer isVegetarian) {
        this.isVegetarian = isVegetarian;
    }
}
