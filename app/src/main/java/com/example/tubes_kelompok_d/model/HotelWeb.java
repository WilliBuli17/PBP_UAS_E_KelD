package com.example.tubes_kelompok_d.model;

import java.io.Serializable;

public class HotelWeb implements Serializable {
    private  int idHotel;
    private  String namaHotel;
    private  String lokasiHotel;
    private  String gambar;
    private  Double harga;

    public HotelWeb(int idHotel, String namaHotel, String lokasiHotel, Double harga, String gambar) {
        this.idHotel = idHotel;
        this.namaHotel = namaHotel;
        this.lokasiHotel = lokasiHotel;
        this.harga = harga;
        this.gambar = gambar;
    }

    public HotelWeb(int idHotel, String namaHotel, String lokasiHotel, Double harga) {
        this.idHotel = idHotel;
        this.namaHotel = namaHotel;
        this.lokasiHotel = lokasiHotel;
        this.harga = harga;
    }
    public HotelWeb(String namaHotel, String lokasiHotel, Double harga) {
        this.namaHotel = namaHotel;
        this.lokasiHotel = lokasiHotel;
        this.harga = harga;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public String getNamaHotel() {
        return namaHotel;
    }

    public String getLokasiHotel() {
        return lokasiHotel;
    }

    public String getGambar() {
        return gambar;
    }

    public Double getHarga() {
        return harga;
    }
}
