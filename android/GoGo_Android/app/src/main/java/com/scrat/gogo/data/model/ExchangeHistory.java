package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/17.
 */

public class ExchangeHistory implements Serializable {
    private Goods goods;
    @SerializedName("goods_order")
    private GoodsOrder order;

    public Goods getGoods() {
        return goods;
    }

    public GoodsOrder getOrder() {
        return order;
    }
}
