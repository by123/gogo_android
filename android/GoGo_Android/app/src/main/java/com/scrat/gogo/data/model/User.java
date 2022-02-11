package com.scrat.gogo.data.model;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/12.
 */

public class User implements Serializable {
    private String uid;
    private String username;
    private String avatar;

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }
}
