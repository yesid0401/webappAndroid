package com.tissini.webview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface InterestApi {

    @GET("api/interests")
    Call<List<Interest>> getInterests();

    @POST("api/interests")
    Call<Interest> createInterest(@Body Interest interest);
}
