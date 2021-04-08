package com.tissini.webview.interfaces;

import com.tissini.webview.models.Interest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface InterestApi {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/interests/all/false")
    Call<List<Interest>> getInterests(@Header("Authorization") String auth);

    @POST("api/interests")
    Call<Interest> createInterest(@Body Interest interest);


}
