package com.lshan.boilerfaves.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lshan.boilerfaves.Adapters.FoodAdapter;
import com.lshan.boilerfaves.Adapters.SelectFoodAdapter;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectFoodActivity extends AppCompatActivity {


    @BindView(R.id.selectFoodRecyclerView)
    RecyclerView selectFoodRecyclerView;

    private SelectFoodAdapter selectFoodAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_food);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<FoodModel> dummyList = new ArrayList<FoodModel>();
        for (int i = 0; i < 10; i++) {
            dummyList.add(new FoodModel());
        }

        startAdaptor(dummyList);
    }




    private void startAdaptor(List<FoodModel> data){
        selectFoodAdapter = new SelectFoodAdapter(this, data);
        selectFoodAdapter.notifyDataSetChanged();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        selectFoodRecyclerView.setLayoutManager(linearLayoutManager);

        selectFoodRecyclerView.setAdapter(selectFoodAdapter);
    }

}
