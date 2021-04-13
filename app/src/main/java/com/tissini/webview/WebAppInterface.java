package com.tissini.webview;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tissini.webview.interfaces.JsonPlaceHolderI;
import com.tissini.webview.models.JsonPlaceHolder;
import com.tissini.webview.models.MywebViewClient;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebAppInterface {
    Context mContext;
    WebView webView;
    MywebViewClient mywebViewClient;
    private JsonPlaceHolderI jsonPlaceHolderI;
    public String status;
    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c,WebView webView) {
        mContext = c;
        this.webView = webView;
        mywebViewClient = new MywebViewClient();
        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl("https://io.tissini.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

                ;
        //notificationI = retrofit.create(NotificationI.class);
        jsonPlaceHolderI = retrofit.create(JsonPlaceHolderI.class);
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void shareApi( String title,String body) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        mContext.startActivity(Intent.createChooser(intent, title));

    }

    @JavascriptInterface
    public void getCart(String processing){
        getToken();
    }

    public void getToken(){
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript("localStorage.getItem('token')", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        String token  =  (String) value.toString().replaceAll("^[\"']+|[\"']+$", "");
                        getJsonPlaceHolder(token);
                    }
                });
            }

        });
    }

    public void  getJsonPlaceHolder(String token ) {
        Timer timer = new Timer();

        try {

          timer.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){
                    Call<JsonPlaceHolder> call = jsonPlaceHolderI.getPost("Bearer " + token);

                    call.enqueue(new Callback<JsonPlaceHolder>() {
                        @Override
                        public void onResponse(Call<JsonPlaceHolder> call, Response<JsonPlaceHolder> response) {

                            if (!response.isSuccessful()) {
                                System.err.println("ERROR AL PROCESAR SOLICITUS DESDE RESPONSE");
                            }

                            Gson gson = new Gson();
                            String data = gson.toJson(response.body().getShopping_cart());
                            System.out.println(data);
                            JsonParser parser = new JsonParser();
                            JsonElement jsonTree = parser.parse(data);
                            JsonObject jsonObject = jsonTree.getAsJsonObject();
                            JsonElement statusj   = jsonObject.get("status");

                            if(jsonObject.get("status") != null){
                                status = (String) statusj.getAsString();
                                    if(!status.equals("processing")){
                                        sendStatusPayment(status);
                                        timer.cancel();
                                    }
                            }

                            System.out.println("imprimiendo status desde void onResponse => "+ (String) statusj.getAsString());

                        }

                        @Override
                        public void onFailure(Call<JsonPlaceHolder> call, Throwable t) {
                            System.err.println("ERROR AL PROCESAR SOLICITUS DESDE FAILURE " + t.getMessage());
                        }
                    });

                }

                },5000,10000);

        }
        catch (Exception e){
            System.out.println("Exception => " + e.getMessage());
        }
    }

    public void sendStatusPayment(String statu){

        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:syncShoppingCart('"+statu+"','payment')");
                webView.clearCache(true);

            }

        });

    }

}
