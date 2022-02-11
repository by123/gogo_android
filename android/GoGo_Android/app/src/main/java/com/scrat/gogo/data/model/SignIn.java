package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by scrat on 2017/12/17.
 */

public class SignIn implements Serializable {
    @SerializedName("has_sign")
    private boolean hasSign;
    private int day;
    @SerializedName("gift_coin")
    private List<String> giftCoinList;

    public boolean isHasSign() {
        return hasSign;
    }

    public int getDay() {
        return day;
    }

    public List<String> getGiftCoinList() {
        return giftCoinList;
    }
}
