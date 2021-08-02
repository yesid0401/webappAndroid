package com.tissini.webview.services;

import android.webkit.WebView;

import com.google.gson.Gson;
import com.tissini.webview.BuildConfig;
import com.tissini.webview.interfaces.VersionI;
import com.tissini.webview.models.Version;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VersionServices {
    private VersionI versionI;
    String api = "https://v3.tissini.app/";
    public VersionServices() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        versionI = retrofit.create(VersionI.class);
    }

    public  void getVersion(WebView webView){
        try{
            Call<Version> call = versionI.getVersion();
            call.enqueue(new Callback<Version>() {
                @Override
                public void onResponse(Call<Version> call, Response<Version> response) {
                    if(!response.isSuccessful()){
                        System.out.println("Error al procesar la solicitud");
                        return;
                    }

                    Gson gson = new Gson();
                    String data = gson.toJson(response.body().getVersion_code());

                    int version_code_play_store = Integer.parseInt(data);
                    int version_actual = BuildConfig.VERSION_CODE;

                    if(version_actual < version_code_play_store){
                        webView.loadUrl("javascript:updateAvailable('true')");
                    }
                }

                @Override
                public void onFailure(Call<Version> call, Throwable t) {
                    System.out.println("Error al procesar la solicitud "+ t.getMessage());
                }
            });
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
