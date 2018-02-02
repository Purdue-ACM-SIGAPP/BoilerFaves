package com.lshan.boilerfaves.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lshan.boilerfaves.Adapters.FoodAdapter;
import com.lshan.boilerfaves.Adapters.SelectFoodAdapter;
import com.lshan.boilerfaves.Models.BreakfastModel;
import com.lshan.boilerfaves.Models.DinnerModel;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Models.LunchModel;
import com.lshan.boilerfaves.Models.MenuModel;
import com.lshan.boilerfaves.Networking.MenuApiHelper;
import com.lshan.boilerfaves.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectFoodActivity extends AppCompatActivity {


    @BindView(R.id.selectFoodRecyclerView)
    RecyclerView selectFoodRecyclerView;

    private SelectFoodAdapter selectFoodAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_food);

        ButterKnife.bind(this);

        context = this.context;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        callRetrofit();
    }


    private void callRetrofit ()  {


        MenuApiHelper.getInstance().getMenu("earhart", "12-01-2017").enqueue(new Callback<MenuModel>(){
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
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
                                Log.e("Retrofit", "Unable to connect to api or something");
                Log.e("Retrofit", t.getMessage());
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.menu_items, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;
    }

    private void startAdaptor(List<FoodModel> data){
        selectFoodAdapter = new SelectFoodAdapter(this, data);
        selectFoodAdapter.notifyDataSetChanged();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        selectFoodRecyclerView.setLayoutManager(linearLayoutManager);

        selectFoodRecyclerView.setAdapter(selectFoodAdapter);
    }

}
