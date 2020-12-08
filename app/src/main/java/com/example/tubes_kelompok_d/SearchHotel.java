package com.example.tubes_kelompok_d;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tubes_kelompok_d.adapter.AdapterHotel;
import com.example.tubes_kelompok_d.model.HotelWeb;
import com.example.tubes_kelompok_d.model.HotelAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class SearchHotel extends AppCompatActivity {

    private AdapterHotel adapter;
    private List<HotelWeb> listHotel;

    /*ActivitySearchHotelBinding binding;
    private ArrayList<HotelWeb> listHotel;
    private RecyclerView recyclerView;
    private RvHotelAdapter adapter;
    private RecyclerView.LayoutManager manager;
    Button gantiPencarian;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hotel);

        loadDaftarHotel();

       /* binding = DataBindingUtil.setContentView(this,R.layout.activity_search_hotel);
        listHotel = new DaftarHotel().HOTEL;
        adapter = new RvHotelAdapter(this,listHotel);
        binding.setHotelAdapter(adapter);
        gantiPencarian = findViewById(R.id.gntPencarian);
        gantiPencarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchHotel.this, Navbar.class));
            }
        });*/

//
//        ArrayList<Hotel> hotel = new ArrayList<>();
//        hotel.add(new Hotel(R.drawable.horison, "Horison", "Jogja", "Rp450.000"));
//
//        recyclerView = findViewById(R.id.rvHotel);
//        recyclerView.setHasFixedSize(true);
//        manager = new LinearLayoutManager(this);
//        adapter = new RvHotelAdapter(hotel);
//
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(adapter);
    }

    public void loadDaftarHotel() {
        setAdapter();
        getHotel();
    }

    public void setAdapter() {
        listHotel = new ArrayList<HotelWeb>();
        RecyclerView recyclerView = findViewById(R.id.rvMhs);
        adapter = new AdapterHotel(getApplicationContext(), listHotel);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }

    public void getHotel() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data buku");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, HotelAPI.URL_SELECT,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("dataBuku");

                    if (!listHotel.isEmpty())
                        listHotel.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int idHotel = Integer.parseInt(jsonObject.optString("idBuku"));
                        String namaHotel = jsonObject.optString("namaBuku");
                        String lokasiHotel = jsonObject.optString("pengarang");
                        Double harga = Double.parseDouble(jsonObject.optString("harga"));
                        String gambar = jsonObject.optString("gambar");

                        HotelWeb hotel = new HotelWeb(idHotel, namaHotel, lokasiHotel, harga, gambar);

                        listHotel.add(hotel);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), response.optString("message"), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}