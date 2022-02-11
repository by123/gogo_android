package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/15.
 */

public class BettingItem implements Serializable {
    @SerializedName("betting_item_id")
    private String bettingItemId;
    private String title;
    private float odds;
    private String status;

    public String getBettingItemId() {
        return bettingItemId;
    }

    public String getTitle() {
        return title;
    }

    public float getOdds() {
        return odds;
    }

    public String getStatus() {
        return status;
    }
}
