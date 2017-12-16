package com.lshan.boilerfaves.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    }

    @Override
    public int getItemCount(){
        return foods.size();
    }


    public class AreaViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.food_card)
        CardView foodCard;

        public AreaViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
