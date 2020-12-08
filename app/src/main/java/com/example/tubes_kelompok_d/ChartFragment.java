package com.example.tubes_kelompok_d;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tubes_kelompok_d.adapter.AdapterChart;
import com.example.tubes_kelompok_d.model.Chart;
import com.example.tubes_kelompok_d.model.ChartApi;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;


public class ChartFragment extends Fragment {
    private AdapterChart adapter;
    private List<Chart> listChart;
    private View root;
    private FirebaseAuth mAuth;
    private  String id_User;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_chart, container, false);
        mAuth = FirebaseAuth.getInstance();
        id_User = mAuth.getCurrentUser().getUid();
        loadDaftarChart();
        return root;
    }

    public void loadDaftarChart(){
        setAdapter();
        getHotel();
    }

    public void setAdapter(){
        listChart = new ArrayList<Chart>();
        RecyclerView recyclerView = root.findViewById(R.id.rvMhs);
        adapter = new AdapterChart(root.getContext(), listChart, new AdapterChart.deleteItemListener() {
            @Override
            public void deleteItem(Boolean delete) {
                if (delete){
                    loadDaftarChart();
                }
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    public void getHotel() {
        //Tambahkan tampil buku disini
        RequestQueue queue = Volley.newRequestQueue(root.getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(root.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data Booking");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, ChartApi.URL_SELECT + id_User,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!listChart.isEmpty())
                        listChart.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int idHotel           = Integer.parseInt(jsonObject.optString("id"));
                        String namaHotel      = jsonObject.optString("nama_hotel");
                        String id_pelanggan   = jsonObject.optString("id_pelanggan");
                        int jumlah_dewasa     = Integer.parseInt(jsonObject.optString("jumlah_dewasa"));
                        int jumlah_anak       = Integer.parseInt(jsonObject.optString("jumlah_anak"));
                        int jumlah_kamar      = Integer.parseInt(jsonObject.optString("jumlah_kamar"));
                        String check_in       = jsonObject.optString("check_in");
                        String check_out      = jsonObject.optString("check_out");
                        Double harga          = Double.parseDouble(jsonObject.optString("harga"));
                        Double total_harga          = Double.parseDouble(jsonObject.optString("total_harga"));

                        //Membuat objek buku
                        Chart chart = new Chart(idHotel, namaHotel, id_pelanggan,
                                jumlah_dewasa, jumlah_anak, jumlah_kamar,check_in,check_out,
                                harga, total_harga);

                        //Menambahkan objek buku ke listBuku
                        listChart.add(chart);
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(root.getContext(), response.optString("message"), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(root.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}