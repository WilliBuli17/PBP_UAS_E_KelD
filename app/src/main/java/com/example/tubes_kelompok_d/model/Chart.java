package com.example.tubes_kelompok_d.model;

import java.io.Serializable;
import java.util.Date;

public class Chart implements Serializable {
    private  int idChart;
    private  String nama_hotel;
    private  String id_pelanggan;
    private  int jumlah_dewasa;
    private  int jumlah_anak;
    private  int jumlah_kamar;
    private  String check_in;
    private  String check_out;
    private  Double harga;
    private  Double total_harga;

    public Chart(int idChart, String nama_hotel, String id_pelanggan, int jumlah_dewasa,
                 int jumlah_anak, int jumlah_kamar, String check_in, String check_out,
                 Double harga, Double total_harga) {
        this.idChart = idChart;
        this.nama_hotel = nama_hotel;
        this.id_pelanggan = id_pelanggan;
        this.jumlah_dewasa = jumlah_dewasa;
        this.jumlah_anak = jumlah_anak;
        this.jumlah_kamar = jumlah_kamar;
        this.check_in = check_in;
        this.check_out = check_out;
        this.harga = harga;
        this.total_harga = total_harga;
    }

    public Chart(String nama_hotel, String id_pelanggan, int jumlah_dewasa, int jumlah_anak,
                 int jumlah_kamar, String check_in, String check_out, Double harga, Double total_harga) {
        this.nama_hotel = nama_hotel;
        this.id_pelanggan = id_pelanggan;
        this.jumlah_dewasa = jumlah_dewasa;
        this.jumlah_anak = jumlah_anak;
        this.jumlah_kamar = jumlah_kamar;
        this.check_in = check_in;
        this.check_out = check_out;
        this.harga = harga;
        this.total_harga = total_harga;
    }

    public Chart(int jumlah_dewasa, int jumlah_anak, int jumlah_kamar, String check_in,
                 String check_out, Double harga, Double total_harga) {
        this.jumlah_dewasa = jumlah_dewasa;
        this.jumlah_anak = jumlah_anak;
        this.jumlah_kamar = jumlah_kamar;
        this.check_in = check_in;
        this.check_out = check_out;
        this.harga = harga;
        this.total_harga = total_harga;
    }

    public int getIdChart() {
        return idChart;
    }

    public String getNama_hotel() {
        return nama_hotel;
    }

    public String getId_pelanggan() {
        return id_pelanggan;
    }

    public int getJumlah_dewasa() {
        return jumlah_dewasa;
    }

    public int getJumlah_anak() {
        return jumlah_anak;
    }

    public int getJumlah_kamar() {
        return jumlah_kamar;
    }

    public String getCheck_in() {
        return check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public Double getHarga() {
        return harga;
    }

    public Double getTotal_harga() {
        return total_harga;
    }
}
