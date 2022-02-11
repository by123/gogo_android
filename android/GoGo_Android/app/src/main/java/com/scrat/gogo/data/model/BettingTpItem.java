package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/22.
 */

public class BettingTpItem implements Serializable {
    private String tp;
    @SerializedName("tp_name")
    private String name;

    public String getTp() {
        return tp;
    }

    public String getName() {
        return name;
    }
}
