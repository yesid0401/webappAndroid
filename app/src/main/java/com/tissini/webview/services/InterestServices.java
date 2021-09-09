package com.tissini.webview.services;

import com.pusher.pushnotifications.PushNotifications;
import com.tissini.webview.interfaces.InterestI;
import com.tissini.webview.models.Interest;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.tissini.webview.helpers.Functions.ParserDataLocalStorage;

public class InterestServices {
    private InterestI interestI;
    private String apiLocal ="http://192.168.1.10:8000/";
    private String apiProduction = "https://backofficeapi.tissini.app/";
    public InterestServices() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiLocal)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        interestI = retrofit.create(InterestI.class);
    }

    public void saveInterestsInDataBase(String client_id,String client_stage,String client_name, String client_platform,String client_escalafon ) {

        Interest interest =  new Interest(client_id,client_stage,client_name,client_platform,client_escalafon);
        Call<Interest> call = interestI.createInterest(interest);

        call.enqueue(new Callback<Interest>() {
            @Override
            public void onResponse(Call<Interest> call, Response<Interest> response) {
                if(!response.isSuccessful()){
                    System.err.println(" ERROR AL PROCESAR SOLICITUS DESDE RESPONSE");
                }

                  System.out.println("RESPONESE => "+response.body().toString());
            }

            @Override
            public void onFailure(Call<Interest> call, Throwable t) {
                System.err.println(" ERROR AL PROCESAR SOLICITUS DESDE FAILURE In => "+t.getMessage());
            }
        });
    }

    public  void addInterestsToUser(String value){
        PushNotifications.clearDeviceInterests();
        PushNotifications.addDeviceInterest("general");

        if(!value.equals("null")){

            ArrayList values = ParserDataLocalStorage(value);

            String user_id         = values.get(0).toString();
            String user_stage      = values.get(1).toString();
            String user_escalafon  = values.get(2).toString();
            PushNotifications.addDeviceInterest(user_id);
            PushNotifications.addDeviceInterest("Login");
            PushNotifications.addDeviceInterest(user_stage);
            PushNotifications.addDeviceInterest(user_escalafon);
            PushNotifications.addDeviceInterest("Android");
            PushNotifications.removeDeviceInterest("noLogin");
            System.out.println(PushNotifications.getDeviceInterests());

        }else{
            PushNotifications.addDeviceInterest("noLogin");
            System.out.println(PushNotifications.getDeviceInterests());
        }
    }
}
