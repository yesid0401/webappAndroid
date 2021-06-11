package com.tissini.webview.controllers;

import com.tissini.webview.services.InterestServices;

public class InterestController {
    private static InterestServices interestServices = new InterestServices();


    public static  void createInterest(String client_id,String client_stage, String client_name,String client_platform ){
        interestServices.createInterest(client_id,client_stage,client_name,client_platform);
    }
}
