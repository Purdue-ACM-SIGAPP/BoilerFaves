package com.lshan.boilerfaves.Networking;

import com.lshan.boilerfaves.Models.MenuModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lshan on 12/17/2017.
 */

public interface MenuApi {
    @GET("earhart/12-01-2017")
    Call<MenuModel> testGet();

}
