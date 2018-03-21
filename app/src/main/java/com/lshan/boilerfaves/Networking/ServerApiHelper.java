package com.lshan.boilerfaves.Networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lshan on 2/13/2018.
 */

public class ServerApiHelper {

    private static ServerApi serverApi;

    public static ServerApi getInstance() {
        if(serverApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://boilerfaves-api.sigapp.club/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            serverApi = retrofit.create(ServerApi.class);
        }

        return serverApi;
    }


}
