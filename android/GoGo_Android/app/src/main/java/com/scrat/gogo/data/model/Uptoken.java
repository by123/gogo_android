package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/13.
 */

public class Uptoken implements Serializable {
    @SerializedName("upload_token")
    private String token;
    private String domain;

    public String getToken() {
        return token;
    }

    public String getDomain() {
        return domain;
    }
}
