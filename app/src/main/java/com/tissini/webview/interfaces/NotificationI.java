package com.tissini.webview.interfaces;

import com.tissini.webview.models.Notification;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.POST;

public interface NotificationI {


    @POST("api/notifications/read")
    Call<Notification> readNotification(@Body Notification notification);
}
