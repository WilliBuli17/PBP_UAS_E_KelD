package com.example.tubes_kelompok_d.databaseHotel;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tubes_kelompok_d.model.Hotel;

@Database(entities = {Hotel.class}, version = 1, exportSchema = false)
public abstract class AppDatabaseHotel extends RoomDatabase {
    public abstract HotelDao hotelDao();
}
