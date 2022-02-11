package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/12.
 */

public class Goods implements Serializable {
    @SerializedName("goods_id")
    private String goodsId;
    private String cover;
    private String title;
    private int coin;
    @SerializedName("total")
    private int maxCount;
    @SerializedName("max_per_buy")
    private int maxPerBuyCount;
    @SerializedName("expired_ts")
    private long expiredTs;

    public String getGoodsId() {
        return goodsId;
    }

    public String getCover() {
        return cover;
    }

    public String getTitle() {
        return title;
    }

    public int getCoin() {
        return coin;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getMaxPerBuyCount() {
        return maxPerBuyCount;
    }

    public long getExpiredTs() {
        return expiredTs;
    }
}
