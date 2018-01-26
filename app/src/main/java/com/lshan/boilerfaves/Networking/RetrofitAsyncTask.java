package com.lshan.boilerfaves.Networking;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
/**
 * Created by lshan on 1/25/2018.
 */

public class RetrofitAsyncTask extends AsyncTask{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {


        try {
            //Synchronous retrofit call
            Response<List<String>> locationsResponse = MenuApiHelper.getInstance().getLocations().execute();
            for (String location:locationsResponse.body()){
                System.out.println(location);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
