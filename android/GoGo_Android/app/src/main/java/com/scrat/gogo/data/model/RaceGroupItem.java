package com.scrat.gogo.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by scrat on 2017/11/14.
 */

public class RaceGroupItem implements Serializable {
    private String dt;
    private List<Race> items;

    public String getDt() {
        return dt;
    }

    public List<Race> getItems() {
        return items;
    }
}
