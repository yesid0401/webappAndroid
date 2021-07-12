package com.tissini.webview.models;

public class Interest {
    private String client_id;
    private String client_name;
    private String client_stage;
    private String client_platform;
    private String client_escalafon;
    public Interest(String client_id, String client_stage,String client_name,String client_platform,String client_escalafon) {
        this.client_id = client_id;
        this.client_stage = client_stage;
        this.client_name = client_name;
        this.client_platform = client_platform;
        this.client_escalafon = client_escalafon;
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

    public String getClient_escalafon() {
        return client_escalafon;
    }

    public void setClient_escalafon(String client_escalafon) {
        this.client_escalafon = client_escalafon;
    }
}
