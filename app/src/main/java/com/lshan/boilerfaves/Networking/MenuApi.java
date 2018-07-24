package com.lshan.boilerfaves.Networking;

import com.lshan.boilerfaves.Models.DiningCourtWrapperModel;
import com.lshan.boilerfaves.Models.MenuModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by lshan on 12/17/2017.
 */

public interface MenuApi {

    @GET(".")
    Call<DiningCourtWrapperModel> getLocations();

    //12-01-2017
    @GET("{diningCourt}/{date}")
    Call<MenuModel> getMenu(@Path("diningCourt") String diningCourt,
                                  @Path("date") String date);





}
