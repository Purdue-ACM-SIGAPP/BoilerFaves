package com.lshan.boilerfaves.Networking;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Models.MenuModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by lshan on 1/24/2018.
 */

public class SelectionRetrievalTask extends AsyncTask <Void, Void, ArrayList<FoodModel>> {

    //Note: for local testing, run ipconfig in windows terminal and copy the IPv4 Address
    private String API_URL = "http://10.186.116.11:3000/";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<FoodModel> doInBackground(Void... voids) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }

                bufferedReader.close();
                //Gson g = new Gson();

                System.out.println(stringBuilder.toString());

            } finally {
                urlConnection.disconnect();
            }

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<FoodModel> foodModels) {
        super.onPostExecute(foodModels);
    }
}
