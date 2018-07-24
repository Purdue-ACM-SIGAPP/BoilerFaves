package com.lshan.boilerfaves.Networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by lshan on 12/17/2017.
 */

public class MenuApiHelper {
    private static MenuApi menuApi;

    public static MenuApi getInstance() {
        if(menuApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.hfs.purdue.edu/menus/v2/locations/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            menuApi = retrofit.create(MenuApi.class);
        }

        return menuApi;
    }

}
