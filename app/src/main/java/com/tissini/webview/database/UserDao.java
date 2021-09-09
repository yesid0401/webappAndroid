package com.tissini.webview.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User LIMIT 1 ")
    User getUser();

    @Query("SELECT * FROM User WHERE idUser = (:idUser)")
    User getIdUser(String idUser);

    @Insert
    void insertUser(User user);

    @Query("DELETE FROM User WHERE idUser != (:idUser) ")
    void deleteUsers(String idUser);
}
