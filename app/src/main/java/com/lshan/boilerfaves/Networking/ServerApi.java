package com.lshan.boilerfaves.Networking;


import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Models.SelectFoodModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lshan on 2/13/2018.
 */

public interface ServerApi {
    @GET("foods")
    Call<List<SelectFoodModel>> getFoods();
}
