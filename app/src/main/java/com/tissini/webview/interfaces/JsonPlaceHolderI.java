package com.tissini.webview.interfaces;
import com.tissini.webview.models.JsonPlaceHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface JsonPlaceHolderI {

    @Headers({ "Content-Type: application/json;charset=UTF-8;","Cache-Control: no-cache","Cache-Control: no-store"})
    @GET("api/v1/cart")
    Call<JsonPlaceHolder> getPost(@Header("Authorization") String auth);
}
