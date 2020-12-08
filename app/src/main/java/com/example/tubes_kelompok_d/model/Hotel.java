package com.example.tubes_kelompok_d.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tubes_kelompok_d.R;

import java.io.Serializable;

@Entity
public class Hotel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "idHotel")
    public int idHotel;

    @ColumnInfo(name = "gambarHotel")
    public String imgHotelUrl;

    @ColumnInfo(name = "namaHotel")
    public String namaHotel;

    @ColumnInfo(name = "lokasiHotel")
    public String lokasiHotel;

    @ColumnInfo(name = "hargaHotel")
    public String hargaHotel;

    public Hotel(String imgHotelUrl, String namaHotel, String lokasiHotel, String hargaHotel){
        this.imgHotelUrl = imgHotelUrl;
        this.namaHotel = namaHotel;
        this.lokasiHotel = lokasiHotel;
        this.hargaHotel = hargaHotel;
    }

   /* public String getNamaHotel() {
        return namaHotel;
    }

    public void setNamaHotel(String namaHotel) {
        this.namaHotel = namaHotel;
    }

    public String getLokasiHotel() {
        return lokasiHotel;
    }

    public void setLokasiHotel(String lokasiHotel) {
        this.lokasiHotel = lokasiHotel;
    }

    public String getHargaHotel() {
        return hargaHotel;
    }

    public void setHargaHotel(String hargaHotel) {
        this.hargaHotel = hargaHotel;
    }

    public String getImgHotelUrl() { return imgHotelUrl; }

    public void setImgHotelUrl(String imgHotelUrl) { this.imgHotelUrl = imgHotelUrl; }


    @BindingAdapter("showImg")
    public static void showImg (ImageView view, String imageUrl) {
        if (!imageUrl.isEmpty()) {
            Glide.with(view.getContext())
                    .load(imageUrl)
                    .apply(new RequestOptions().override(0, 172))
                    .into(view);
        } else {
            view.setImageDrawable(view.getContext().getDrawable(R.drawable.ic_phone));
        }
    }*/
}
