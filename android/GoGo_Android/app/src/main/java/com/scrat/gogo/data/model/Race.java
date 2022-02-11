package com.scrat.gogo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by scrat on 2017/11/14.
 */

public class Race implements Serializable {
    @SerializedName("race_id")
    private String raceId;
    @SerializedName("score_a")
    private String scoreA;
    @SerializedName("score_b")
    private String scoreB;
    private String status;
    @SerializedName("team_a")
    private Team teamA;
    @SerializedName("team_b")
    private Team teamB;
    @SerializedName("race_ts")
    private long raceTs;
    @SerializedName("race_name")
    private String raceName;

    public String getRaceName() {
        return raceName;
    }

    public String getRaceId() {
        return raceId;
    }

    public String getScoreA() {
        return scoreA;
    }

    public String getScoreB() {
        return scoreB;
    }

    public String getStatus() {
        return status;
    }

    public Team getTeamA() {
        return teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public long getRaceTs() {
        return raceTs;
    }
}
