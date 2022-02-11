package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/14.
 */

public class CoinPlan implements Serializable {
    @SerializedName("coin_plan_id")
    private String coinPlanId;
    private long fee;
    @SerializedName("coin_count")
    private int coinCount;
    private String gift_name;
    private int gift_count;
    private String gift_icon;

    public String getCoinPlanId() {
        return coinPlanId;
    }

    public long getFee() {
        return fee;
    }

    public int getCoinCount() {
        return coinCount;
    }

    public String getGift_name() { return gift_name;}

    public String getGift_icon() {return  gift_icon;}

    public int getGift_count() {return  gift_count;}

}
