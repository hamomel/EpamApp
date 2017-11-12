package com.hamom.epamapp.data.models;

import com.google.gson.annotations.SerializedName;

public class Todo {

    @SerializedName("id")
    private long id;

    @SerializedName("description")
    private String description;

    @SerializedName("title")
    private String title;

    @SerializedName("time")
    private long time;

    @SerializedName("priority")
    private int priority;

    public Todo(long id, String title, String description, long time, int priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.priority = priority;
    }

    public long getId() {
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