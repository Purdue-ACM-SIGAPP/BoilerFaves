package com.lshan.boilerfaves.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lshan.boilerfaves.Adapters.FoodAdapter;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainRecyclerView)
    RecyclerView mainRecyclerView;


    private FoodAdapter foodAdapter;
    final private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        List<FoodModel> dummyList = new ArrayList<FoodModel>();
        for (int i = 0; i < 10; i++) {
            dummyList.add(new FoodModel());
        }
        startAdaptor(dummyList);

    }

    private void startAdaptor(List<FoodModel> data){
        foodAdapter = new FoodAdapter(this, data);
        foodAdapter.notifyDataSetChanged();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mainRecyclerView.setLayoutManager(linearLayoutManager);

        mainRecyclerView.setAdapter(foodAdapter);
    }
}
