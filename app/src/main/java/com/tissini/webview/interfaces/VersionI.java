package com.tissini.webview.interfaces;



import com.tissini.webview.models.Version;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface VersionI {

    @Headers({ "Content-Type: application/json;charset=UTF-8","Cache-Control: no-cache","Cache-Control: no-store"})
    @GET("api/v3/version/3")
    Call<Version> getVersion();
}
