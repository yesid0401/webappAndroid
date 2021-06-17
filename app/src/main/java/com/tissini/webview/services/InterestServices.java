package com.tissini.webview.services;

import com.pusher.pushnotifications.PushNotifications;
import com.tissini.webview.interfaces.InterestI;
import com.tissini.webview.models.Interest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tissini.webview.helpers.Functions.ParserData;

public class InterestServices {
    private InterestI interestI;

    public InterestServices() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://backendnotifications.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        interestI = retrofit.create(InterestI.class);
    }

    public void saveInterestsInDataBase(String client_id,String client_stage,String client_name, String client_platform ) {
        Interest interest =  new Interest(client_id,client_stage,client_name,client_platform);
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
                System.err.println(" ERROR AL PROCESAR SOLICITUS DESDE FAILURE => "+t.getMessage());
            }
        });
    }

    public  void addInterestsToUser(String value){
        if(!value.equals("null")){

            String[] values = ParserData(value);

            String user_id         = values[0];
            String user_stage      = values[1];
            String user_escalafon  = values[2];

            PushNotifications.clearDeviceInterests();
            PushNotifications.addDeviceInterest("general");
            PushNotifications.addDeviceInterest(user_id);
            PushNotifications.addDeviceInterest("Login");
            PushNotifications.addDeviceInterest(user_stage);
            PushNotifications.addDeviceInterest("Android");
            PushNotifications.removeDeviceInterest("noLogin");

            System.out.println(PushNotifications.getDeviceInterests());

            if(!user_escalafon.equals("null"))
                PushNotifications.addDeviceInterest(user_escalafon);
        }else{
            PushNotifications.clearDeviceInterests();
            PushNotifications.addDeviceInterest("noLogin");
            System.out.println(PushNotifications.getDeviceInterests());
        }
    }
}
