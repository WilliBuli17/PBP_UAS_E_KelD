package com.example.tubes_kelompok_d;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tubes_kelompok_d.model.Chart;
import com.example.tubes_kelompok_d.model.ChartApi;
import com.example.tubes_kelompok_d.model.HotelWeb;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class BookingHotel extends Fragment {
    AutoCompleteTextView dewasa, anak, kamar;
    TextInputEditText tglCheckIn, tglCheckOut;
    DatePickerDialog datePickerDialog;
    MaterialButton carihotel;
    Calendar calendar;
    int dayIn=0, monthIn=0, yearIn=0, dayOut=0, monthOut=0, yearOut=0;

    View v;
    private HotelWeb hotel;
    String nama_hotel;
    String idPelanggan;
    double harga;
    private int idChart;
    private Chart chart;
    private FirebaseAuth mAuth;
    private String status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_booking_hotel, container, false);
        init();
        setAtribut();

        return v;
    }

    public void init(){
        mAuth = FirebaseAuth.getInstance();

        dewasa = v.findViewById(R.id.dewasa_text);
        anak = v.findViewById(R.id.anak_text);
        kamar = v.findViewById(R.id.kamar_text);
        tglCheckIn = v.findViewById(R.id.checkInInput);
        tglCheckOut = v.findViewById(R.id.checkOutInput);
        carihotel = v.findViewById(R.id.cariHotrl);

        status = getArguments().getString("status");
        if(status.equals("edit")) {
            chart = (Chart) getArguments().getSerializable("chart");
            idChart = chart.getIdChart();
            tglCheckIn.setText(chart.getCheck_in());
            tglCheckOut.setText(chart.getCheck_out());
            harga = chart.getHarga();
        }else{
            hotel = (HotelWeb) getArguments().getSerializable("hotel");
            nama_hotel = hotel.getNamaHotel();
            idPelanggan = mAuth.getCurrentUser().getUid();
            harga = hotel.getHarga();
        }
        ArrStr();
    }

    public void setAtribut(){
        tglCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                dayIn = calendar.get(Calendar.DAY_OF_MONTH);
                monthIn = calendar.get(Calendar.MONTH);
                yearIn = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int years, int months, int days) {
                        if(days<10){
                            if((months+1)<10){
                                tglCheckIn.setText(years + "-" + "0" + (months + 1) + "-" + "0" + days);
                            } else{
                                tglCheckIn.setText(years + "-" + (months + 1) + "-" + "0" + days);
                            }
                        } else{
                            if((months+1)<10){
                                tglCheckIn.setText(years + "-" + "0" + (months + 1) + "-" + days);
                            } else{
                                tglCheckIn.setText(years + "-" + (months + 1) + "-" + days);
                            }
                        }
                        dayIn = days;
                        monthIn = months+1;
                        yearIn = years;
                    }
                }, yearIn, monthIn, dayIn);
                datePickerDialog.show();
            }
        });

        tglCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                dayOut = calendar.get(Calendar.DAY_OF_MONTH);
                monthOut = calendar.get(Calendar.MONTH);
                yearOut = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int years, int months, int days) {
                        if(days<10){
                            if((months+1)<10){
                                tglCheckOut.setText(years + "-" + "0" + (months + 1) + "-" + "0" + days);
                            } else{
                                tglCheckOut.setText(years + "-" + (months + 1) + "-" + "0" + days);
                            }
                        } else{
                            if((months+1)<10){
                                tglCheckOut.setText(years + "-" + "0" + (months + 1) + "-" + days);
                            } else{
                                tglCheckOut.setText(years + "-" + (months + 1) + "-" + days);
                            }
                        }
                        dayOut = days;
                        monthOut = months+1;
                        yearOut = years;
                    }
                }, yearOut, monthOut, dayOut);
                datePickerDialog.show();
            }
        });

        carihotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.equals("edit")) {
                    int jmlhDws = Integer.parseInt(dewasa.getText().toString());
                    int jmlhAnk = Integer.parseInt(anak.getText().toString());
                    int jmlhKmr = Integer.parseInt(kamar.getText().toString());
                    String checkIn = String.valueOf(tglCheckIn.getText());
                    String checkOut = String.valueOf(tglCheckOut.getText());
                    double cekIn = (yearIn*365) + (monthIn*30) + dayIn;
                    double cekOut = (yearOut*365) + (monthOut*30) + dayOut;
                    double hargas = harga;
                    double totalHarga = hargas * (cekOut-cekIn) * jmlhKmr;
                    chart = new Chart(jmlhDws, jmlhAnk, jmlhKmr, checkIn, checkOut, hargas, totalHarga);
                }else{
                    String nama_hotels = nama_hotel;
                    String idPelanggans = idPelanggan;
                    int jmlhDws = Integer.parseInt(dewasa.getText().toString());
                    int jmlhAnk = Integer.parseInt(anak.getText().toString());
                    int jmlhKmr = Integer.parseInt(kamar.getText().toString());
                    String checkIn = String.valueOf(tglCheckIn.getText());
                    String checkOut = String.valueOf(tglCheckOut.getText());
                    double cekIn = (yearIn*365) + (monthIn*30) + dayIn;
                    double cekOut = (yearOut*365) + (monthOut*30) + dayOut;
                    double hargas = harga;
                    double totalHarga = hargas * (cekOut-cekIn) * jmlhKmr;
                    chart = new Chart(nama_hotels, idPelanggans, jmlhDws, jmlhAnk,
                            jmlhKmr, checkIn, checkOut, hargas, totalHarga);
                }
                cek();
            }
        });
    }

    private void ArrStr() {
        final ArrayAdapter<String> arrayAdapterDewasa = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.dewasa_text));
        arrayAdapterDewasa.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        dewasa.setAdapter(arrayAdapterDewasa);

        final ArrayAdapter<String> arrayAdapterAnak = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.anak_text));
        arrayAdapterAnak.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        anak.setAdapter(arrayAdapterAnak);

        final ArrayAdapter<String> arrayAdapterKamar = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.kamar_text));
        arrayAdapterKamar.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        kamar.setAdapter(arrayAdapterKamar);
    }

    private void cek(){
        String dws = String.valueOf(dewasa.getText());
        String ank = String.valueOf(anak.getText());
        String kmr = String.valueOf(kamar.getText());
        int yIn = yearIn;
        int mIn = monthIn;
        int dIn = dayIn;
        int yOut = yearOut;
        int mOut = monthOut;
        int dOut = dayOut;
        double cekIn = (yIn*365) + (mIn*30) + dIn;
        double cekOut = (yOut*365) + (mOut*30) + dOut;

        if(yIn == 0 || mIn == 0  || dIn == 0 ) {
            Toast.makeText(getActivity(),"Enter Your Tanggal Check In",Toast.LENGTH_SHORT).show();
        }else if(yOut == 0 || mOut == 0  || dOut == 0 ) {
            Toast.makeText(getActivity(),"Enter Your Tanggal Check Out",Toast.LENGTH_SHORT).show();
        }else if(dws.isEmpty()) {
            Toast.makeText(getActivity(),"Enter Your Jumlah Dewasa",Toast.LENGTH_SHORT).show();
        }else if(ank.isEmpty()) {
            Toast.makeText(getActivity(),"Enter YourJumlah Anak",Toast.LENGTH_SHORT).show();
        }else if(kmr.isEmpty()) {
            Toast.makeText(getActivity(),"Enter Your Jumlah Kamar",Toast.LENGTH_SHORT).show();
        }else if(cekOut<=cekIn){
            Toast.makeText(getActivity(),"Minimal Booking 1 day",Toast.LENGTH_SHORT).show();
        }else if((cekOut-cekIn)>30){
            Toast.makeText(getActivity(),"Maximal Booking 30 day",Toast.LENGTH_SHORT).show();
        }else {
            if(status.equals("edit")) {
                editChart(idChart);
            }else{
                tambahChart();
            }
        }
    }

    public void tambahChart(){
        //Tambahkan tambah buku disini
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data Booking");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, ChartApi.URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan status dari response
                    if(obj.getString("message").equals("Add Chart Success")) {
                        startActivity(new Intent(getActivity(), Navbar.class));
                    }

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nama_hotel", chart.getNama_hotel());
                params.put("id_pelanggan", chart.getId_pelanggan());
                params.put("jumlah_dewasa", String.valueOf(chart.getJumlah_dewasa()));
                params.put("jumlah_anak", String.valueOf(chart.getJumlah_anak()));
                params.put("jumlah_kamar", String.valueOf(chart.getJumlah_kamar()));
                params.put("check_in", chart.getCheck_in());
                params.put("check_out", chart.getCheck_out());
                params.put("harga", String.valueOf(chart.getHarga()));
                params.put("total_harga", String.valueOf(chart.getTotal_harga()));


                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void editChart(int idChart) {
        //Tambahkan edit buku disini
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengedit data Booking");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(PUT, ChartApi.URL_UPDATE + idChart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan status dari response
                    if(obj.getString("message").equals("Update Chart Succes")) {
                        startActivity(new Intent(getActivity(), Navbar.class));
                    }

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("jumlah_dewasa", String.valueOf(chart.getJumlah_dewasa()));
                params.put("jumlah_anak", String.valueOf(chart.getJumlah_anak()));
                params.put("jumlah_kamar", String.valueOf(chart.getJumlah_kamar()));
                params.put("check_in", chart.getCheck_in());
                params.put("check_out", chart.getCheck_out());
                params.put("harga", String.valueOf(chart.getHarga()));
                params.put("total_harga", String.valueOf(chart.getTotal_harga()));

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}