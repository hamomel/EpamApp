package com.hamom.epamapp.data.network.responces;

import com.google.gson.annotations.SerializedName;

public class TodoRes {

    @SerializedName("id")
    private String id;

    @SerializedName("description")
    private String description;

    @SerializedName("title")
    private String title;

    @SerializedName("time")
    private long time;

    @SerializedName("priority")
    private int priority;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public long getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public int getPriority() {
        return priority;
    }
}