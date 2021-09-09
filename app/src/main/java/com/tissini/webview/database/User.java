package com.tissini.webview.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public  int uid;

    @ColumnInfo(name = "idUser")
    public  String idUser;

    public int getUid() {
        return uid;
    }

    public String getIdUser() {
        return idUser;
    }
}
