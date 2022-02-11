package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/17.
 */

public class Address implements Serializable {
    private String tel;
    private String receiver;
    private String location;
    @SerializedName("address_detail")
    private String detail;

    public String getTel() {
        return tel;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getLocation() {
        return location;
    }

    public String getDetail() {
        return detail;
    }
}
