package com.lshan.boilerfaves.Networking;


import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Models.POJOFoodModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by lshan on 2/13/2018.
 */

public interface ServerApi {
    @GET(".")
    Call<List<POJOFoodModel>> getFoods();
}
