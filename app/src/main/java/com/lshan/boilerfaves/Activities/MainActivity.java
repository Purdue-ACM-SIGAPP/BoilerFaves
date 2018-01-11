package com.lshan.boilerfaves.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.lshan.boilerfaves.Adapters.FoodAdapter;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Networking.MenuRetrievalTask;
import com.lshan.boilerfaves.R;
import com.lshan.boilerfaves.Utils.NotificationHelper;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;

import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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


        new MenuRetrievalTask(context).execute();

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
