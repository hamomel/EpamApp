package com.hamom.epamapp.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Todo implements Parcelable{

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

    protected Todo(Parcel in) {
        id = in.readLong();
        description = in.readString();
        title = in.readString();
        time = in.readLong();
        priority = in.readInt();
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(description);
        parcel.writeString(title);
        parcel.writeLong(time);
        parcel.writeInt(priority);
    }
}