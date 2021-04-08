package com.tissini.webview.models;

public class Interest {
    private int id;
    private String name;
    private String status;
    private String client_id;
    private String client_stage;

    public Interest(String client_id, String client_stage) {
        this.client_id = client_id;
        this.client_stage = client_stage;
    }


    public String getUser_id() {
        return client_id;
    }

    public void setUser_id(String user_id) {
        this.client_id = user_id;
    }

    public String getUser_stage() {
        return client_stage;
    }

    public void setUser_stage(String user_stage) {
        this.client_stage = user_stage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
