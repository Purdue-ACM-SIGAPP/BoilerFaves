package com.lshan.boilerfaves.Activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.lshan.boilerfaves.Adapters.FoodAdapter;
import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.Networking.MenuRetrievalTask;
import com.lshan.boilerfaves.Networking.OnMenuRetrievalCompleted;
import com.lshan.boilerfaves.Networking.OnNotificationConstructed;
import com.lshan.boilerfaves.R;
import com.lshan.boilerfaves.Receivers.MasterAlarmReceiver;
import com.lshan.boilerfaves.Utils.CustomGridLayoutManager;
import com.lshan.boilerfaves.Utils.NotificationHelper;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class MainActivity extends AppCompatActivity implements OnMenuRetrievalCompleted {

    @BindView(R.id.mainRecyclerView)
    RecyclerView mainRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.availableFavesLayout)
    ScrollView availableFavesLayout;

    @BindView(R.id.noFavesLayout)
    RelativeLayout noFavesLayout;

    @BindView(R.id.progressLayout)
    RelativeLayout progressLayout;

    @BindView(R.id.mainLayout)
    FrameLayout mainLayout;

    @BindView(R.id.availabilitySwitch)
    SwitchCompat availabilitySwitch;

    @BindView(R.id.noAvailableFavesLayout)
    RelativeLayout noAvailableFavesLayout;

    final private Context context = this;

    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dismiss notification if the user clicked a notification to get here
        Bundle b = getIntent().getExtras();
        if (b != null) {
            int id = b.getInt("notificationID", 0);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(id);
        }

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        noAvailableFavesLayout.setVisibility(View.GONE);
        availabilitySwitch.setChecked(SharedPrefsHelper.getSharedPrefs(context).getBoolean("availabilitySwitchChecked", false));

        List<FoodModel> faveList = SharedPrefsHelper.getFaveList(context);
        List<FoodModel> availFaveList = SharedPrefsHelper.getFaveList(context);

        if (faveList != null) {
            startAdaptor(faveList);
            handleSwitchChange(availabilitySwitch, availabilitySwitch.isChecked());
        } else {
            SharedPrefsHelper.storeFaveList(new ArrayList<FoodModel>(), context);
            faveList = new ArrayList<FoodModel>();
        }

        for (int i = 0; i < faveList.size(); i++) {
            if (faveList.get(i).isAvailable) {
                availFaveList.add(faveList.get(i));
            }
        }


        //Run notification check
        //Needed to start the notification checking cycle the first time the app is launched
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MasterAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);

        checkAndDisplayFaves();
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

    public void showNoInternetDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setTitle("Data retrieval failed")
                .setMessage("Unable to connect to the Internet")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkAndDisplayFaves();
                    }
                });
        AlertDialog failure = alertDialogBuilder.create();
        failure.show();
    }

    public void checkForFaves(List<FoodModel> faveList) {
        if (faveList == null || faveList.size() == 0) {
            noFavesLayout.setVisibility(View.VISIBLE);
            noAvailableFavesLayout.setVisibility(View.GONE);
            availableFavesLayout.setVisibility(View.GONE);
        } else {
            noFavesLayout.setVisibility(View.GONE);
            availableFavesLayout.setVisibility(View.VISIBLE);
        }
    }

    private void checkAndDisplayFaves() {
        if (isOnline()) {
            mainLayout.setVisibility(View.GONE);
            new MenuRetrievalTask(
                    SharedPrefsHelper.getFaveList(context),
                    MainActivity.this).execute();
        } else {
            showNoInternetDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        noAvailableFavesLayout.setVisibility(View.GONE);
        availabilitySwitch.setChecked(SharedPrefsHelper.getSharedPrefs(context).getBoolean("availabilitySwitchChecked", false));

        checkAndDisplayFaves();
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

        if (id == R.id.action_add) {
            launchFoodSelect();
        }
        if (id == R.id.action_notifications) {
            launchNotifications();
        }

        return super.onOptionsItemSelected(item);
    }


    private void startAdaptor(List<FoodModel> data) {
        foodAdapter = new FoodAdapter(this, data);
        foodAdapter.setmOnListEmptyListener(new FoodAdapter.OnListEmptyListener() {
            @Override
            public void onListEmpty() {
                if (availabilitySwitch.isChecked() && SharedPrefsHelper.getFaveList(context).size() > 0) {
                    noAvailableFavesLayout.setVisibility(View.VISIBLE);
                    mainRecyclerView.setVisibility(View.GONE);
                } else {
                    noFavesLayout.setVisibility(View.VISIBLE);
                    availableFavesLayout.setVisibility(View.GONE);
                }
            }
        });


        CustomGridLayoutManager customGridLayoutManager = new CustomGridLayoutManager(context);
        customGridLayoutManager.setScrollEnabled(false);
        mainRecyclerView.setLayoutManager(customGridLayoutManager);

        mainRecyclerView.setAdapter(foodAdapter);

        foodAdapter.notifyDataSetChanged();
    }


    public void launchFoodSelect() {
        Intent intent = new Intent(context, SelectFoodActivity.class);
        context.startActivity(intent);
    }

    public void launchNotifications() {
        Intent intent = new Intent(context, NotificationActivity.class);
        context.startActivity(intent);
    }


    @OnCheckedChanged(R.id.availabilitySwitch)
    public void handleSwitchChange(SwitchCompat switchCompat, boolean isChecked) {
        SharedPreferences preferences = SharedPrefsHelper.getSharedPrefs(this);
        preferences.edit().putBoolean("availabilitySwitchChecked", isChecked).apply();

        List<FoodModel> faveList = SharedPrefsHelper.getFaveList(context);
        ArrayList<FoodModel> availFaveList = new ArrayList<FoodModel>();

        for (int i = 0; i < faveList.size(); i++) {
            if (faveList.get(i).isAvailable) {
                availFaveList.add(faveList.get(i));
            }
        }

        System.out.println("checked " + isChecked);

        if (isChecked) {
            if (availFaveList.size() == 0) {
                progressLayout.setVisibility(View.GONE);

                noAvailableFavesLayout.setVisibility(View.VISIBLE);
                mainRecyclerView.setVisibility(View.GONE);
            } else {
                mainRecyclerView.setVisibility(View.VISIBLE);
            }

            startAdaptor(availFaveList);

        } else {

            startAdaptor(faveList);

            mainRecyclerView.setVisibility(View.VISIBLE);
            noAvailableFavesLayout.setVisibility(View.GONE);
        }

        foodAdapter.notifyDataSetChanged();
    }


    //The results of the menu retrieval task are used here (make UI changes)
    @Override
    public void onMenuRetrievalCompleted(List<FoodModel> faves) {
        checkForFaves(faves);

        if (SharedPrefsHelper.getSharedPrefs(context).getBoolean("availabilitySwitchChecked", false)) {

            ArrayList<FoodModel> filteredFaves = filterAvailableFaves(new ArrayList<>(faves));
            foodAdapter.setFoods(filteredFaves);
            if (filteredFaves.size() > 0) {
                noAvailableFavesLayout.setVisibility(View.GONE);
                mainRecyclerView.setVisibility(View.VISIBLE);
            } else {
                if (SharedPrefsHelper.getFaveList(context).size() > 0) {
                    noAvailableFavesLayout.setVisibility(View.VISIBLE);
                    mainRecyclerView.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.GONE);
                }
            }
        } else {
            Collections.sort(faves);
            if (!faves.isEmpty() && foodAdapter != null) {
                foodAdapter.setFoods(faves);
            }
            mainRecyclerView.setVisibility(View.VISIBLE);
        }

        if (foodAdapter != null) {
            foodAdapter.notifyDataSetChanged();
        }

        //Need to call this so when main activity resumes it remembers availability
        SharedPrefsHelper.storeFaveList(faves, context);

        //Hide the progress bar and show the food list
        if (progressLayout != null) {
            progressLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
            mainRecyclerView.setVisibility(View.VISIBLE);
        }

    }


    private ArrayList<FoodModel> filterAvailableFaves(ArrayList<FoodModel> faveList) {
        ArrayList<FoodModel> availFaveList = new ArrayList<FoodModel>();

        for (int i = 0; i < faveList.size(); i++) {
            if (faveList.get(i).isAvailable) {
                availFaveList.add(faveList.get(i));
            }
        }

        Collections.sort(availFaveList);

        return availFaveList;
    }

}
