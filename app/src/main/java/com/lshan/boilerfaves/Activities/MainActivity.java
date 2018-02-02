package com.lshan.boilerfaves.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.lshan.boilerfaves.Adapters.FoodAdapter;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Networking.MenuRetrievalTask;
import com.lshan.boilerfaves.R;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.mainRecyclerView)
    RecyclerView mainRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.availableFavesLayout)
    RelativeLayout availableFavesLayout;

    @BindView(R.id.noFavesLayout)
    RelativeLayout noFavesLayout;


    private FoodAdapter foodAdapter;
    final private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        List<FoodModel> faveList = SharedPrefsHelper.getFaveList(context);
        checkForFaves(faveList);

        if(faveList != null){
            startAdaptor(faveList);
        }else{
            SharedPrefsHelper.storeFaveList(new ArrayList<FoodModel>(), context);
        }

        //new SelectionRetrievalTask().execute();
        new MenuRetrievalTask(context, mainRecyclerView).execute();

    }

    private void checkForFaves(List<FoodModel> faveList){
        if(faveList == null || faveList.size() == 0){
            noFavesLayout.setVisibility(View.VISIBLE);
            availableFavesLayout.setVisibility(View.GONE);
        }else{
            noFavesLayout.setVisibility(View.GONE);
            availableFavesLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO maybe I should just call notfity dataset changed or something
        List<FoodModel> faveList = SharedPrefsHelper.getFaveList(context);
        checkForFaves(faveList);
        if(faveList != null){
            startAdaptor(faveList);
        }

        new MenuRetrievalTask(context, mainRecyclerView).execute();
    }


    //Shameless copy paste from https://stackoverflow.com/questions/31231609/creating-a-button-in-android-toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_notifications) {
                System.out.println("Notification Button Clicked");
            }

            if(id == R.id.action_add){
                launchFoodSelect();
            }

            return super.onOptionsItemSelected(item);
        }

    private void callRetrofit ()  {

    }

    private void startAdaptor(List<FoodModel> data){
        foodAdapter = new FoodAdapter(this, data);
        foodAdapter.setmOnListEmptyListener(new FoodAdapter.OnListEmptyListener() {
            @Override
            public void onListEmpty() {
                noFavesLayout.setVisibility(View.VISIBLE);
                availableFavesLayout.setVisibility(View.GONE);
            }
        });
        foodAdapter.notifyDataSetChanged();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(linearLayoutManager);

        mainRecyclerView.setAdapter(foodAdapter);
    }


    public void launchFoodSelect(){
        Intent intent = new Intent(context, SelectFoodActivity.class);
        context.startActivity(intent);
    }



}
