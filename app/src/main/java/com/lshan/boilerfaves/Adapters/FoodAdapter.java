package com.lshan.boilerfaves.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
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

    public FoodAdapter(Context context, List<FoodModel> data){
        this.foods = data;
        this.context = context;
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

        if(food.isAvailable){
            holder.availabilityLayout.setVisibility(View.VISIBLE);
        }else{
            holder.availabilityLayout.setVisibility(View.GONE);
        }

        HashMap<String, ArrayList<String>> availableCourts = food.getAvailableCourts();


    }


    @Override
    public int getItemCount(){
        return foods.size();
    }

    public List<FoodModel> getFoods() {
        return foods;
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
                SharedPrefsHelper.storeFaveList(faveList, context);
            }

            foods.remove(position);
            notifyDataSetChanged();
        }


    }

}
