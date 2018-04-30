package com.lshan.boilerfaves.Activities;

import android.content.Context;

import android.os.Bundle;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.lshan.boilerfaves.Adapters.SelectFoodAdapter;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Models.SelectFoodModel;
import com.lshan.boilerfaves.Networking.ServerApiHelper;
import com.lshan.boilerfaves.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

/**
 * This activity displays a list of foods that users can select as faves.
 */
public class SelectFoodActivity extends AppCompatActivity {


    @BindView(R.id.selectFoodRecyclerView)
    RecyclerView selectFoodRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private SelectFoodAdapter selectFoodAdapter;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_food);

        ButterKnife.bind(this);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("");

        callRetrofit();
    }


    private void callRetrofit ()  {

        progressBar.setVisibility(View.VISIBLE);
        selectFoodRecyclerView.setVisibility(View.GONE);

        ServerApiHelper.getInstance().getFoods().enqueue(new Callback<List<SelectFoodModel>>() {
             @Override
             public void onResponse(Call<List<SelectFoodModel>> call, Response<List<SelectFoodModel>> response) {
                 progressBar.setVisibility(View.GONE);
                 selectFoodRecyclerView.setVisibility(View.VISIBLE);

                 List<SelectFoodModel> selectFoodModels = response.body();
                 List<FoodModel> foods = new ArrayList<FoodModel>();

                 for(SelectFoodModel selectFoodModel: selectFoodModels){
                     foods.add(new FoodModel(selectFoodModel));
                 }

                 startAdaptor(foods);
             }

             @Override
             public void onFailure(Call<List<SelectFoodModel>> call, Throwable t) {
                 showNoInternetDialog();
                 Log.e("Retrofit", t.getMessage());
             }
         });

    }

    public void showNoInternetDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setTitle("Data retrieval failed")
                .setMessage("Either you don't have internet or something went wrong on our end...")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callRetrofit();
                    }
                });
        AlertDialog failure = alertDialogBuilder.create();
        failure.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.menu_search, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconified(false);
        searchView.setQueryHint("Search foods...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(selectFoodAdapter != null) {
                    selectFoodAdapter.searchFoods(query);
                }
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        searchView.clearFocus();
                    }
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                //change adapter model to fit query
                selectFoodRecyclerView.scrollToPosition(0);
                if(selectFoodAdapter != null) {
                    selectFoodAdapter.searchFoods(s);
                }
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
