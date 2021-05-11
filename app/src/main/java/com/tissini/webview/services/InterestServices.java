package com.tissini.webview.services;

import com.tissini.webview.interfaces.InterestI;
import com.tissini.webview.interfaces.VersionI;
import com.tissini.webview.models.Interest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InterestServices {
    private InterestI interestI;

    public InterestServices() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.14:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        interestI = retrofit.create(InterestI.class);
    }

    public void createInterest(String client_id,String client_stage ) {
        Interest interest =  new Interest(client_id,client_stage);
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
}
