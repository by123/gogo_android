package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/14.
 */

public class Team implements Serializable {
    @SerializedName("team_id")
    private String teamId;
    @SerializedName("team_name")
    private String teamName;
    private String logo;

    public String getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getLogo() {
        return logo;
    }
}
