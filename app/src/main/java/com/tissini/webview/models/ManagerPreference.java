package com.tissini.webview.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import static com.tissini.webview.helpers.Functions.ParserDataLocalStorage;

public class ManagerPreference {

    public Context context;
    public SharedPreferences pref;
    public String KeyPreference;

    public ManagerPreference(Context context){
        this.context = context;
        this.KeyPreference = "idUserKey";
        this.pref =  PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    public  void saveUserPreference(String value){
        ArrayList values = ParserDataLocalStorage(value);
        String idUser = values.get(0).toString();

        SharedPreferences.Editor editor = this.pref.edit();
        editor.putString(this.KeyPreference,idUser);
        editor.apply();
    }

    public  String getUserPreference(){
        return  this.pref.getString(this.KeyPreference,"");
    }
}
