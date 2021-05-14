package com.tissini.webview.models;

public class Interest {
    private String client_id;
    private String client_name;
    private String client_stage;

    public Interest(String client_id, String client_stage,String client_name) {
        this.client_id = client_id;
        this.client_stage = client_stage;
        this.client_name = client_name;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_stage() {
        return client_stage;
    }

    public void setClient_stage(String client_stage) {
        this.client_stage = client_stage;
    }
}
