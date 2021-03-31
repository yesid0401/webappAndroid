package com.tissini.webview;

public class Interest {
    private int id;
    private String name;
    private String status;
    private String user_id;
    private String user_stage;

    public Interest(String user_id, String user_stage) {
        this.user_id = user_id;
        this.user_stage = user_stage;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_stage() {
        return user_stage;
    }

    public void setUser_stage(String user_stage) {
        this.user_stage = user_stage;
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
