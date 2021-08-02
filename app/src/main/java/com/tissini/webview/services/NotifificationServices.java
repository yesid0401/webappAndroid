package com.tissini.webview.services;

import com.tissini.webview.interfaces.NotificationI;
import com.tissini.webview.models.Notification;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotifificationServices {
    private  NotificationI notificationI;
    private String apiLocal ="http://localhost:8000/";
    private String apiProduction = "https://backofficeapi.tissini.app/";

    public NotifificationServices() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiProduction)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        notificationI = retrofit.create(NotificationI.class);
    }

    public void readNotification(String idClient,String idNotification ) {
        Notification notification =  new Notification(idClient,idNotification);
        Call<Notification> call = notificationI.readNotification(notification);

        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(!response.isSuccessful()){
                    System.err.println(" ERROR AL PROCESAR SOLICITUS DESDE RESPONSE");
                }

                System.out.println(response.body());

            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                System.err.println(" ERROR AL PROCESAR SOLICITUS DESDE FAILURE => "+t.getMessage());
            }
        });
    }

}
