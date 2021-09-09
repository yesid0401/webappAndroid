package com.tissini.webview.controllers;

import android.content.Context;

import com.tissini.webview.database.AppDataBase;
import com.tissini.webview.database.User;

import java.util.ArrayList;

import static com.tissini.webview.helpers.Functions.ParserDataLocalStorage;

public class UserController {
    public static void saveUserInternalDataBase(Context context,String value){
        ArrayList values = ParserDataLocalStorage(value);
        String idUser = values.get(0).toString();
        User user = new User();
        user.idUser = idUser;
        AppDataBase db = AppDataBase.getDbInstance(context.getApplicationContext());

        if(db.userDao().getIdUser(idUser) == null ){
            db.userDao().insertUser(user);
            db.userDao().deleteUsers(idUser);
        }
    }

    public static String getDataUser(Context context){
        AppDataBase db = AppDataBase.getDbInstance(context.getApplicationContext());
        return db.userDao().getUser().getIdUser();
    }

}
