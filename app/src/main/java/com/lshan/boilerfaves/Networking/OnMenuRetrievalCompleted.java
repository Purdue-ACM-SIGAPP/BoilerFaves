package com.lshan.boilerfaves.Networking;

import com.lshan.boilerfaves.Models.FoodModel;

import java.util.List;

/**
 * Created by lshan on 4/22/2018.
 */

public interface OnMenuRetrievalCompleted {
    void onMenuRetrievalCompleted(List<FoodModel> faves);
}
