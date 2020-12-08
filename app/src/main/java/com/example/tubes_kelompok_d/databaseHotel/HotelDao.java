package com.example.tubes_kelompok_d.databaseHotel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tubes_kelompok_d.model.Hotel;

import java.util.List;

@Dao
public interface HotelDao {

    @Query("SELECT * FROM Hotel")
    List<Hotel> getAll();

    @Insert
    void insert(Hotel hotel);

    @Update
    void update(Hotel hotel);

    @Delete
    void delete(Hotel hotel);
}
