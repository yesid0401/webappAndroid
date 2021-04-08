package com.tissini.webview.models;

public class JsonPlaceHolder {
    public int userId;
    public int id;
    public String title;
    public String body;
    public Object shopping_cart ;

    public JsonPlaceHolder() {
    }

    public Object getShopping_cart(){
        return  shopping_cart;
    }
    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
