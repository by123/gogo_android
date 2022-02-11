package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/17.
 */

public class GoodsOrder implements Serializable {
    private String status;
    @SerializedName("create_ts")
    private long createTs;

    public String getStatus() {
        return status;
    }

    public long getCreateTs() {
        return createTs;
    }
}
