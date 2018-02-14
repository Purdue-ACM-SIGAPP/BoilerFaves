package com.lshan.boilerfaves.Networking;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by lshan on 12/17/2017.
 */

public class MenuApiHelper {
    private static MenuApi menuApi;

    public static MenuApi getInstance() {
        if(menuApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.hfs.purdue.edu/menus/v1/locations/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            menuApi = retrofit.create(MenuApi.class);
        }

        return menuApi;
    }

}
