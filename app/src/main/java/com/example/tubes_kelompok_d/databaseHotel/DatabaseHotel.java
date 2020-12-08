package com.example.tubes_kelompok_d.databaseHotel;

import android.content.Context;

import androidx.room.Room;

import com.example.tubes_kelompok_d.model.Hotel;

public class DatabaseHotel {

    private Context context;
    private static DatabaseHotel databaseHotel;

    private AppDatabaseHotel database;

    private DatabaseHotel(Context context){
        this.context = context;
        database = Room.databaseBuilder(context, AppDatabaseHotel.class, "hotel").build();
    }

    public static synchronized DatabaseHotel getInstance(Context context){
        if (databaseHotel==null){
            databaseHotel = new DatabaseHotel(context);
        }
        return databaseHotel;
    }

    public AppDatabaseHotel getDatabase(){
        return database;
    }

}
