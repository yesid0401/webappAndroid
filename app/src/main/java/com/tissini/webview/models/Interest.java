package com.tissini.webview.models;

public class Interest {
    private String client_id;
    private String client_name;
    private String client_stage;
    private  String client_platform;
    public Interest(String client_id, String client_stage,String client_name,String client_platform) {
        this.client_id = client_id;
        this.client_stage = client_stage;
        this.client_name = client_name;
        this.client_platform = client_platform;
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

    public String getClient_platform() {
        return client_platform;
    }

    public void setClient_platform(String client_platform) {
        this.client_platform = client_platform;
    }

    public void setClient_stage(String client_stage) {
        this.client_stage = client_stage;
    }


}
