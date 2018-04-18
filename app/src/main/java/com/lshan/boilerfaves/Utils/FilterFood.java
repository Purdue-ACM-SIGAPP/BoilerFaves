package com.lshan.boilerfaves.Utils;

import android.widget.Filter;

import com.lshan.boilerfaves.Models.FoodModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by crosenblatt on 4/10/18.
 */

public class FilterFood extends Filter {

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        return null;
    }

    protected FilterResults performFiltering(ArrayList<FoodModel> foodList) {
        FilterResults results = new FilterResults();
        ArrayList<FoodModel> availableFoods = new ArrayList<FoodModel>();

        for(int i = 0; i < foodList.size(); i++) {
            if(foodList.get(i).isAvailable) {
                availableFoods.add(foodList.get(i));
            }
        }

        results.values = availableFoods;
        results.count = availableFoods.size();
        return null;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

    }
}
