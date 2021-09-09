package com.tissini.webview.models;

public class Notification {
    private String idClient;
    private String idNotification;
    private String status;

    public Notification(String idClient, String idNotification, String status) {
        this.idClient = idClient;
        this.idNotification = idNotification;
        this.status = status;
    }

}
