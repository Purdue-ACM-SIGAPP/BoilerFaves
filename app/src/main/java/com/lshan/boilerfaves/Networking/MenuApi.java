package com.lshan.boilerfaves.Networking;

import com.lshan.boilerfaves.Models.MenuModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;


/**
 * Created by lshan on 12/17/2017.
 */

public interface MenuApi {
    //12-01-2017
    @GET("{diningCourt}/{date}")
    Call<MenuModel> getMenu(@Path("diningCourt") String diningCourt,
                                  @Path("date") String date);

}
