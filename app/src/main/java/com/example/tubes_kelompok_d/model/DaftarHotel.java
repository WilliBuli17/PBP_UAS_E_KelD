package com.example.tubes_kelompok_d.model;

import java.util.ArrayList;

public class DaftarHotel {

    public ArrayList<Hotel> HOTEL;

    public DaftarHotel(){
        HOTEL = new ArrayList();
        HOTEL.add(Permata);
        HOTEL.add(Mutiara);
        HOTEL.add(Bintang);
    }

    public static final Hotel Permata = new Hotel("https://www.ahstatic.com/photos/5473_ho_00_p_1024x768.jpg", "Hotel Permata", "jl. merdeka", "1.000.000");
    public static final Hotel Mutiara = new Hotel("https://www.ahstatic.com/photos/5473_ho_00_p_1024x768.jpg", "Hotel Mutiara", "jl. ombak", "500.000");
    public static final Hotel Bintang = new Hotel("https://www.ahstatic.com/photos/5473_ho_00_p_1024x768.jpg", "Hotel Bintang", "jl. sukajadi", "2.000.000");


}
