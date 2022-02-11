package com.scrat.gogo.data.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/12.
 */

public class NewsDetail extends News implements Serializable {
    private String body;
    private String url;
    @SerializedName("news_ts")
    private long newsTs;
    @SerializedName("is_like")
    private boolean isLike;

    public boolean isWebViewNews() {
        return !TextUtils.isEmpty(url);
    }

    public String getBody() {
        return body;
    }

    public String getUrl() {
        return url;
    }

    public long getNewsTs() {
        return newsTs;
    }

    public boolean isLike() {
        return isLike;
    }
}
