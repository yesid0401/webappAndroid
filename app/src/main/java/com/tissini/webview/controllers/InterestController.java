package com.tissini.webview.controllers;

import com.tissini.webview.services.InterestServices;
import java.util.ArrayList;
import static com.tissini.webview.helpers.Functions.ParserDataLocalStorage;

public class InterestController {
    private static InterestServices interestServices = new InterestServices();


    public static  void saveInterestsInDataBase(String value){
        if(!value.equals("null")) {
            ArrayList values = ParserDataLocalStorage(value);
            String client_id    = values.get(0).toString();
            String client_stage = values.get(1).toString();
            String client_escalafon = values.get(2).toString();
            String client_name  = values.get(3).toString();
            interestServices.saveInterestsInDataBase(client_id, client_stage, client_name, "Android",client_escalafon,"tissini");
        }
    }

    public static void  addInterestsToUser(String value){
        interestServices.addInterestsToUser(value);
    }
}
