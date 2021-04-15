package com.tissini.webview.models;

import android.app.Activity;
import android.widget.Toast;

import com.tissini.webview.BuildConfig;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JsoupClass {
   String version_name_current = "";
   String version_name_play_store = "";
   private Activity activity;
    public JsoupClass(Activity activity){
        this.activity = activity;
    }

    public void getText(){
        Thread downloadThread = new Thread() {
            public void run() {
                Element element;


                    //element  =  Jsoup.connect("https://play.google.com/store/apps/details?id=com.tissini.webview").get().select("div:matchesOwn(^Current Version$)").first().parent().select("span").first();

                    version_name_current    = BuildConfig.VERSION_NAME;
                    version_name_play_store = "45";

                    System.out.println("version_name_current => "+version_name_current);
                    System.out.println("version_name_play_store => "+version_name_play_store);
                    if(!version_name_current.equals(version_name_play_store)){
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, "AQUI MOSTRAMOS EL POPUP", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

            }
        }; downloadThread.start();
    }


}
