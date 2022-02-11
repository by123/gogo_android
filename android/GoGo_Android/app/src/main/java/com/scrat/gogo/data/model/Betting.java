package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by scrat on 2017/11/15.
 */

public class Betting implements Serializable {
    @SerializedName("betting_id")
    private String bettingId;
    private String title;
    @SerializedName("expired_ts")
    private long expiredTs;
    private List<BettingItem> items;

    public String getBettingId() {
        return bettingId;
    }

    public String getTitle() {
        return title;
    }

    public long getExpiredTs() {
        return expiredTs;
    }

    public List<BettingItem> getItems() {
        return items;
    }
}
