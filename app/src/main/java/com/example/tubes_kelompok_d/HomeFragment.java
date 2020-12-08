package com.example.tubes_kelompok_d;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tubes_kelompok_d.adapter.AdapterHotel;
import com.example.tubes_kelompok_d.model.HotelWeb;
import com.example.tubes_kelompok_d.model.HotelAPI;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class HomeFragment extends Fragment {
    CarouselView carouselView;

    int[] sampleImages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5};
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    private AdapterHotel adapter;
    private List<HotelWeb> listHotel;
    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        loadDaftarHotel();
        carouselView = (CarouselView) root.findViewById(R.id.carouselView);
        carouselView.setImageListener(imageListener);
        carouselView.setPageCount(sampleImages.length);
        return root;
    }

    public void loadDaftarHotel(){
        setAdapter();
        getHotel();
    }

    public void setAdapter(){
        listHotel = new ArrayList<HotelWeb>();
        RecyclerView recyclerView = root.findViewById(R.id.rvMhs);
        adapter = new AdapterHotel(root.getContext(), listHotel);

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
        progressDialog.setTitle("Menampilkan data Hotel");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, HotelAPI.URL_SELECT,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!listHotel.isEmpty())
                        listHotel.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int idHotel          = Integer.parseInt(jsonObject.optString("id"));
                        String namaHotel     = jsonObject.optString("nama_hotel");
                        String lokasiHotel   = jsonObject.optString("alamat_hotel");
                        Double harga         = Double.parseDouble(jsonObject.optString("harga_hotel"));
                        String gambar        = jsonObject.optString("foto_hotel");

                        //Membuat objek buku
                        HotelWeb hotel = new HotelWeb(idHotel, namaHotel, lokasiHotel, harga, gambar);

                        //Menambahkan objek buku ke listBuku
                        listHotel.add(hotel);
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