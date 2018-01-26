package com.lshan.boilerfaves.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.R;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lshan on 12/16/2017.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.AreaViewHolder>{

    private List<FoodModel> foods;
    private Context context;
    private OnListEmptyListener mOnListEmptyListener;

    //Used to edit the appropriate parts of the availability gridlayout
    private final int BREAKFAST_OFFSET = 0;
    private final int LUNCH_OFFSET = 6;
    private final int DINNER_OFFSET = 12;




    public FoodAdapter(Context context, List<FoodModel> data){
        this.foods = data;
        this.context = context;
    }

    //Used to display the "No faves selected" message
    public interface OnListEmptyListener{
        public void onListEmpty();
    }

    public void setmOnListEmptyListener(OnListEmptyListener onListEmptyListener){
        mOnListEmptyListener = onListEmptyListener;
    }

    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_card, parent, false);

        return new AreaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AreaViewHolder holder, int position){
        FoodModel food = foods.get(position);
        holder.cardTitle.setText(food.Name);


        //Update card layout based on availability
        if(food.isAvailable){
            holder.availabilityLayout.setVisibility(View.VISIBLE);
            holder.unavailableMessage.setVisibility(View.GONE);
        }else{
            holder.availabilityLayout.setVisibility(View.GONE);
            holder.unavailableMessage.setVisibility(View.VISIBLE);
        }

        HashMap<String, ArrayList<String>> availableCourts = food.getAvailableCourts();
        GridLayout grid = holder.availabilityGrid;

        if(availableCourts != null) {

            if (availableCourts.containsKey("Breakfast")) {
                grid.getChildAt(0).setVisibility(View.VISIBLE);

                ArrayList<String> courtList = availableCourts.get("Breakfast");
                displayDiningCourts(grid, courtList, BREAKFAST_OFFSET);
            } else {
                for (int i = 0; i < 6; i++) {
                    grid.getChildAt(i).setVisibility(View.GONE);
                }
            }

            if (availableCourts.containsKey("Lunch")) {
                grid.getChildAt(6).setVisibility(View.VISIBLE);

                ArrayList<String> courtList = availableCourts.get("Lunch");
                displayDiningCourts(grid, courtList, LUNCH_OFFSET);
            } else {
                for (int i = 6; i < 12; i++) {
                    grid.getChildAt(i).setVisibility(View.GONE);
                }
            }

            if (availableCourts.containsKey("Dinner")) {
                grid.getChildAt(12).setVisibility(View.VISIBLE);

                ArrayList<String> courtList = availableCourts.get("Dinner");
                displayDiningCourts(grid, courtList, DINNER_OFFSET);
            } else {
                for (int i = 12; i < 18; i++) {
                    grid.getChildAt(i).setVisibility(View.GONE);
                }
            }
        }

    }

    public void displayDiningCourts(GridLayout grid, ArrayList<String> courtList, int offset){

        //Use i to select spot in gridlayout, j to iterate through courtsList
        int j =0;
        for (int i = 1 + offset; i < 6 + offset; i++) {
            ImageView icon = (ImageView) grid.getChildAt(i);

            if(j<courtList.size()){
                String court = courtList.get(j);
                icon.setVisibility(View.VISIBLE);
                switch(court){
                    case "Windsor":
                        icon.setImageResource(R.drawable.ic_windsor);
                        break;
                    case "Ford":
                        icon.setImageResource(R.drawable.ic_ford);
                        break;
                    case "Wiley":
                        icon.setImageResource(R.drawable.ic_wiley);
                        break;
                    case "Earhart":
                        icon.setImageResource(R.drawable.ic_earhart);
                        break;
                    case "Hillenbrand":
                        icon.setImageResource(R.drawable.ic_hillenbrand);
                        break;
                    default:
                        icon.setImageResource(R.drawable.ic_launcher_foreground);
                }

               j++;

            }else{
                icon.setVisibility(View.GONE);
            }
        }

    }



    @Override
    public int getItemCount(){
        return foods.size();
    }

    public void setFoods(List<FoodModel> foods){
        this.foods = foods;
    }



    public class AreaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.food_card)
        CardView foodCard;

        @BindView(R.id.title)
        TextView cardTitle;

        @BindView(R.id.removeButton)
        ImageButton removeButton;

        @BindView(R.id.availability)
        RelativeLayout availabilityLayout;

        @BindView(R.id.availabilityGrid)
        GridLayout availabilityGrid;

        @BindView(R.id.unavailableMessage)
        TextView unavailableMessage;

        public AreaViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        @OnClick(R.id.removeButton)
        public void removeItem(){
            int position = this.getLayoutPosition();

            FoodModel foodModel = foods.get(position);
            List<FoodModel> faveList = SharedPrefsHelper.getFaveList(context);
            if(faveList != null && faveList.contains(foodModel)){
                faveList.remove(foodModel);
                if(faveList.size() == 0){
                    //Need to display the no faves selected screen
                    if(mOnListEmptyListener != null){
                        mOnListEmptyListener.onListEmpty();
                    }
                }

                SharedPrefsHelper.storeFaveList(faveList, context);
            }


            foods.remove(position);
            notifyDataSetChanged();
        }

    }


}
