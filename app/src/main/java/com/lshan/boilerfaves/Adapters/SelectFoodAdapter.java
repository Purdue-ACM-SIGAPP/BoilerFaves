package com.lshan.boilerfaves.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private List<FoodModel> faveList;

    public SelectFoodAdapter(Context context, List<FoodModel> data){
        this.foods = data;
        this.filteredFoods = foods;
        this.context = context;
        this.faveList = SharedPrefsHelper.getFaveList(context);

        if(faveList == null){
            faveList = new ArrayList<FoodModel>();
        }
    }

    //set model within search parameters
    public void searchFoods(String searchText){
        if(searchText.length() != 0){
            this.filteredFoods = new ArrayList<>();
            for(FoodModel f : this.foods){
                if(f.getName().toLowerCase().contains(searchText.toLowerCase())){
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
        holder.cardTitle.setText(filteredFoods.get(position).getName());

        FoodModel foodModel = filteredFoods.get(position);
        if(!faveList.contains(foodModel)){
            holder.faved.setVisibility(View.GONE);
        }else{
            holder.faved.setVisibility(View.VISIBLE);
        }
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

        @BindView(R.id.faved)
        ImageView faved;

        public AreaViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.select_food_card)
        public void onClickCard() {
            FoodModel foodModel = foods.get(this.getLayoutPosition());

            if(!faveList.contains(foodModel)){
                faveList.add(foodModel);
                SharedPrefsHelper.storeFaveList(faveList, context);

                //create toast that food has been added
                Toast.makeText(context, foodModel.getName() + " added to faves", Toast.LENGTH_LONG).show();
                //make checkmark visible

                notifyDataSetChanged();
            }else{
                faveList.remove(foodModel);
                SharedPrefsHelper.storeFaveList(faveList, context);

                Toast.makeText(context, foodModel.getName() + " removed from faves", Toast.LENGTH_LONG).show();

                notifyDataSetChanged();
            }
        }


    }

}
