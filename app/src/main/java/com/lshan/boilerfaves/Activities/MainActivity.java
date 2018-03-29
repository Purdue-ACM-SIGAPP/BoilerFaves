package com.lshan.boilerfaves.Activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
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
import com.lshan.boilerfaves.Receivers.MasterAlarmReceiver;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        //Dismiss notification if the user clicked a notification to get here
        Bundle b = getIntent().getExtras();
        if(b != null){
            int id = b.getInt("notificationID", 0);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(id);
        }

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        List<FoodModel> faveList = SharedPrefsHelper.getFaveList(context);
        checkForFaves(faveList);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MasterAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);

        if(faveList != null){
            startAdaptor(faveList);
        }else{
            SharedPrefsHelper.storeFaveList(new ArrayList<FoodModel>(), context);
        }

        //new SelectionRetrievalTask().execute();


        if (isOnline()) {
            new MenuRetrievalTask(context, mainRecyclerView, MenuRetrievalTask.NO_NOTIFICATION).execute();
        } else {
            showNoInternetDialog();
        }

        //JobUtil.scheduleJob(context);

    }

    //https://stackoverflow.com/questions/9521232/how-to-catch-an-exception-if-the-internet-or-signal-is-down
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void showNoInternetDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setTitle("Data retrieval failed")
                .setMessage("Unable to connect to the Internet")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isOnline()){
                            new MenuRetrievalTask(context, mainRecyclerView, MenuRetrievalTask.NO_NOTIFICATION).execute();
                        } else {
                            showNoInternetDialog();
                        }
                    }
                });
        AlertDialog failure = alertDialogBuilder.create();
        failure.show();
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

        if (isOnline()) {
            new MenuRetrievalTask(context, mainRecyclerView, MenuRetrievalTask.NO_NOTIFICATION).execute();
        } else {
            showNoInternetDialog();
        }
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

        if(id == R.id.action_add){
            launchFoodSelect();
        }
        if(id == R.id.action_notifications){
            launchNotifications();
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

    public void launchNotifications(){
        Intent intent = new Intent(context, NotificationActivity.class);
        context.startActivity(intent);

    }



}
