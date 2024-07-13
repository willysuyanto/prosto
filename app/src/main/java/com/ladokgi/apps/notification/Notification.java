package com.ladokgi.apps.notification;

public class Notification {
    private String title, message, date;

    public Notification(String title, String message, String date) {
        this.title = title;
        this.message = message;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
    public String getDate() {
        return date;
    }
}
