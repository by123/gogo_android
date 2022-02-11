package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/17.
 */

public class GoodsDetail extends Goods implements Serializable {
    @SerializedName("total_apply")
    private int totalExchange;
    @SerializedName("total_buy")
    private int totalUserBuy;
    private String description;

    public int getTotalExchange() {
        return totalExchange;
    }

    public int getTotalUserBuy() {
        return totalUserBuy;
    }

    public String getDescription() {
        return description;
    }
}
