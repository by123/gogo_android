package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/12.
 */

public class Comment implements Serializable {
    @SerializedName("comment_id")
    private String commentId;
    private String content;
    @SerializedName("create_ts")
    private long createTs;
    @SerializedName("like_count")
    private int totalLike;
    @SerializedName("is_like")
    private boolean isLike;

    public String getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public long getCreateTs() {
        return createTs;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public boolean isLike() {
        return isLike;
    }

    public Comment setLike(boolean like) {
        if (like) {
            totalLike ++;
        } else {
            totalLike --;
        }
        isLike = like;
        return this;
    }

}
