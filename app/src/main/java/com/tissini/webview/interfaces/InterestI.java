package com.tissini.webview.interfaces;

import com.tissini.webview.models.Interest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterestI {
    @POST("api/interests")
    Call<Interest> createInterest(@Body Interest interest);
}
