package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by scrat on 2017/11/15.
 */

public class BettingInfo implements Serializable {
    private RaceDetail race;
    private List<Betting> betting;

    // for history
    private int coin;
    private String status;
    private float odds;
    @SerializedName("create_ts")
    private long createTs;
    private List<Betting> bettings;

    public int getCoin() {
        return coin;
    }

    public float getOdds() {
        return odds;
    }

    public String getStatus() {
        return status;
    }

    public long getCreateTs() {
        return createTs;
    }

    public RaceDetail getRace() {
        return race;
    }

    public List<Betting> getBetting() {
        return betting == null ? bettings : betting;
    }
}
