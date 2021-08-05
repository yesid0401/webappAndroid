package com.tissini.webview.models;

public class Interest {
    private String client_id;
    private String client_name;
    private String client_stage;
    private String client_platform;
    private String client_escalafon;
    private String client_app;

    public Interest(String client_id, String client_stage,String client_name,String client_platform,String client_escalafon,String client_app) {
        this.client_id = client_id;
        this.client_stage = client_stage;
        this.client_name = client_name;
        this.client_platform = client_platform;
        this.client_escalafon = client_escalafon;
        this.client_app = client_app;
    }
}
