package com.tissini.webview.controllers;

import com.tissini.webview.services.InterestServices;

import static com.tissini.webview.helpers.Functions.ParserData;

public class InterestController {
    private static InterestServices interestServices = new InterestServices();


    public static  void saveInterestsInDataBase(String value){
        if(!value.equals("null")) {
            String[] values = ParserData(value);
            String client_id    = values[0];
            String client_stage = values[1];
            String client_name  = values[3];
            interestServices.saveInterestsInDataBase(client_id, client_stage, client_name, "Android");
        }
    }

    public static void  addInterestsToUser(String value){
        interestServices.addInterestsToUser(value);
    }
}
