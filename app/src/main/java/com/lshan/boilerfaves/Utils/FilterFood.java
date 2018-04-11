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
        FilterResults results = new FilterResults();
        ArrayList<FoodModel> availableFoods = new ArrayList<FoodModel>();

        

        results.values = availableFoods;
        results.count = availableFoods.size();
        return null;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

    }
}
