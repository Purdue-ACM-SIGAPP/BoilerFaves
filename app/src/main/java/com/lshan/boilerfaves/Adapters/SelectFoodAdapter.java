package com.lshan.boilerfaves.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lshan.boilerfaves.Models.FoodModel;
import com.lshan.boilerfaves.R;
import com.lshan.boilerfaves.Utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lshan on 12/16/2017.
 */

public class SelectFoodAdapter extends RecyclerView.Adapter<SelectFoodAdapter.AreaViewHolder>{

    private List<FoodModel> foods;
    private List<FoodModel> filteredFoods;
    private Context context;

    public SelectFoodAdapter(Context context, List<FoodModel> data){
        this.foods = data;
        this.filteredFoods = foods;
        this.context = context;
    }

    //set model within search parameters
    public void searchFoods(String searchText){
        if(searchText.length() != 0){
            this.filteredFoods = new ArrayList<>();
            for(FoodModel f : this.foods){
                if(f.Name.toLowerCase().contains(searchText.toLowerCase())){
                    this.filteredFoods.add(f);
                }
            }
        }else{
            this.filteredFoods = this.foods;
        }
        notifyDataSetChanged();
    }

    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_food_card, parent, false);

        return new AreaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AreaViewHolder holder, int position){
        holder.cardTitle.setText(filteredFoods.get(position).Name);
    }

    @Override
    public int getItemCount(){
        return filteredFoods.size();
    }


    public class AreaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.select_food_card)
        CardView selectFoodCard;

        @BindView(R.id.title)
        TextView cardTitle;

        public AreaViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.select_food_card)
        public void onClickCard() {
            FoodModel foodModel = foods.get(this.getLayoutPosition());
            List<FoodModel> faveList = SharedPrefsHelper.getFaveList(context);

            if(faveList == null){
                faveList = new ArrayList<FoodModel>();
            }

            if(!faveList.contains(foodModel)){
                faveList.add(foodModel);
                SharedPrefsHelper.storeFaveList(faveList, context);
            }
        }


    }

}
