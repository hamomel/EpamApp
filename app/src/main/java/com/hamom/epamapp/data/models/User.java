package com.hamom.epamapp.data.models;

/**
 * Created by hamom on 12.11.17.
 */

public class User {
    private long id;
    private String name;

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
