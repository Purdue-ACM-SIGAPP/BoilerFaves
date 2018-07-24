package com.lshan.boilerfaves.Models;

import java.util.List;

/**
 * Created by lshan on 12/17/2017.
 */

public class MenuModel {
    public List<MealModel> Meals;

    public MealModel getBreakfast() {
        for (MealModel m : Meals) {
            if (m.Name.equals("Breakfast")) return m;
        }
        return null;
    }

    public MealModel getLunch() {
        for (MealModel m : Meals) {
            if (m.Name.equals("Lunch")) return m;
        }
        return null;
    }

    public MealModel getDinner() {
        for (MealModel m : Meals) {
            if (m.Name.equals("Dinner")) return m;
        }
        return null;
    }
}
