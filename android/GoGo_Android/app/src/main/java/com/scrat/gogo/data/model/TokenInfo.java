package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by scrat on 2017/11/3.
 */

public class TokenInfo {
    private String uid;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("access_token")
    private String accessToken;
//    @SerializedName("expired_in")
//    private long expireIn;

    public String getUid() {
        return uid;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
