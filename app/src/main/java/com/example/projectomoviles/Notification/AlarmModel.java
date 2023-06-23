package com.example.projectomoviles.Notification;

public class AlarmModel {
    private int id;
    private String title;
    private long time;

    public AlarmModel() {
    }

    public AlarmModel(int id, String title, long time) {
        this.id = id;
        this.title = title;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
