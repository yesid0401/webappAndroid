package com.tissini.webview.models;

public class Notification {
    private String idClient;
    private String idNotification;


    public Notification(String idClient, String idNotification) {
        this.idClient = idClient;
        this.idNotification = idNotification;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(String idNotification) {
        this.idNotification = idNotification;
    }


}
