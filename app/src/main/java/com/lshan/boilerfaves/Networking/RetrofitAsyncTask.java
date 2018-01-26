package com.lshan.boilerfaves.Networking;

import android.os.AsyncTask;

import com.lshan.boilerfaves.Models.MenuModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
/**
 * Created by lshan on 1/25/2018.
 */

public class RetrofitAsyncTask extends AsyncTask{

    @Override
    protected ArrayList<MenuModel> doInBackground(Object[] objects) {


        ArrayList<MenuModel> menus = new ArrayList<>();

        try {
            //Synchronous retrofit call
            Response<List<String>> locationsResponse = MenuApiHelper.getInstance().getLocations().execute();

            for (String diningCourt:locationsResponse.body()){
                Response<MenuModel> menuResponse = MenuApiHelper.getInstance().getMenu(diningCourt, "12-01-2017").execute();
                menus.add(menuResponse.body());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return menus;
    }

}
