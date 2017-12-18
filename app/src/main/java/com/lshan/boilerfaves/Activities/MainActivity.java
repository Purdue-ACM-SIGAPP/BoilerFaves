package com.lshan.boilerfaves.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.lshan.boilerfaves.Adapters.FoodAdapter;
import com.lshan.boilerfaves.Models.BreakfastModel;
import com.lshan.boilerfaves.Models.DinnerModel;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Models.LunchModel;
import com.lshan.boilerfaves.Models.MenuModel;
import com.lshan.boilerfaves.Networking.MenuApiHelper;
import com.lshan.boilerfaves.R;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainRecyclerView)
    RecyclerView mainRecyclerView;

    @BindView(R.id.addNewButton)
    Button addButton;


    private FoodAdapter foodAdapter;
    final private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //callRetrofit();

        List<FoodModel> faveList = SharedPrefsHelper.getFaveList(context);
        if(faveList != null){
            startAdaptor(faveList);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO maybe I should just call notfity dataset changed or something
        List<FoodModel> faveList = SharedPrefsHelper.getFaveList(context);
        if(faveList != null){
            startAdaptor(faveList);
        }

    }

    private void callRetrofit ()  {


        MenuApiHelper.getInstance().testGet().enqueue(new Callback<MenuModel>(){
            @Override
            public void onResponse(Call<MenuModel> call, Response<MenuModel> response){
               Log.i("Retrofit", response.body().Breakfast.get(0).Items.get(0).Name);

               MenuModel result = response.body();

               ArrayList<FoodModel> foodList = new ArrayList<FoodModel>();

                if (result.Breakfast != null) {
                       for (BreakfastModel location : result.Breakfast) {
                           for (FoodModel food : location.Items) {
                               if (!foodList.contains(food)) {
                                   foodList.add(food);
                               }
                           }
                       }
                }

                if (result.Lunch != null) {
                    for (LunchModel location : result.Lunch) {
                        for (FoodModel food : location.Items) {
                            if (!foodList.contains(food)) {
                                foodList.add(food);
                            }
                        }
                    }
                }

                if (result.Dinner != null) {
                    for (DinnerModel location : result.Dinner) {
                        for (FoodModel food : location.Items) {
                            if (!foodList.contains(food)) {
                                foodList.add(food);
                            }
                        }
                    }
                }


                startAdaptor(foodList);


            }

            @Override
            public void onFailure(Call<MenuModel> call, Throwable t) {
                /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                        .setTitle("Data retrieval failed")
                        .setMessage("Unable to connect to the Internet")
                        .setCancelable(false)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                callRetrofit();
                            }
                        });
                AlertDialog failure = alertDialogBuilder.create();
                failure.show();
                */
                Log.e("Retrofit", "Unable to connect to api or something");
                Log.e("Retrofit", t.getMessage());
            }
        });



    }

    private void startAdaptor(List<FoodModel> data){
        foodAdapter = new FoodAdapter(this, data);
        foodAdapter.notifyDataSetChanged();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(linearLayoutManager);

        mainRecyclerView.setAdapter(foodAdapter);
    }


    @OnClick(R.id.addNewButton)
    public void launchFoodSelect(){
        Intent intent = new Intent(context, SelectFoodActivity.class);
        context.startActivity(intent);
    }
}
