package com.sharpnode.model;

/**
 * Created by admin on 12/2/2016.
 */

public class NotificationModel {
    private String title="";
    private String message="";
    private String dateTime="";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
