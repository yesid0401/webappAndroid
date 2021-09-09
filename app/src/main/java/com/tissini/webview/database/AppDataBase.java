package com.tissini.webview.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
        public abstract UserDao userDao();

        public  static  AppDataBase INSTANCE;

        public  static  AppDataBase getDbInstance(Context context){
                if(INSTANCE == null){
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,"tissiniBD")
                                .allowMainThreadQueries()
                                .fallbackToDestructiveMigration()
                                .build();
                }
                return  INSTANCE;
        }
}

