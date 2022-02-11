package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by scrat on 2017/11/21.
 */

public class RaceInfo implements Serializable {
    private RaceDetail race;
    @SerializedName("betting_tps")
    private List<BettingTpItem> tpItems;

    public RaceDetail getRace() {
        return race;
    }

    public List<BettingTpItem> getTpItems() {
        return tpItems;
    }
}
