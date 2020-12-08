package com.example.tubes_kelompok_d.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tubes_kelompok_d.BookingHotel;
import com.example.tubes_kelompok_d.R;
import com.example.tubes_kelompok_d.TambahEditHotel;
import com.example.tubes_kelompok_d.model.ChartApi;
import com.example.tubes_kelompok_d.model.HotelWeb;
import com.example.tubes_kelompok_d.model.HotelAPI;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.DELETE;

public class AdapterHotelAdmin extends RecyclerView.Adapter<AdapterHotelAdmin.adapterHotelAdminViewHolder> {

    private List<HotelWeb> hotelList;
    private Context context;
    private View view;
    private AdapterHotelAdmin.deleteItemListener mListener;

    public AdapterHotelAdmin(Context context, List<HotelWeb> hotelList,
                             AdapterHotelAdmin.deleteItemListener mListener) {
        this.context = context;
        this.hotelList = hotelList;
        this.mListener = mListener;
    }

    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public AdapterHotelAdmin.adapterHotelAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.adapter_hotel_admin, parent, false);
        return new AdapterHotelAdmin.adapterHotelAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHotelAdmin.adapterHotelAdminViewHolder holder, int position) {
        final HotelWeb hotel = hotelList.get(position);

        NumberFormat formatter = new DecimalFormat("#,###");
        holder.tvNamaHotel.setText(hotel.getNamaHotel());
        holder.tvLokasiHotel.setText(hotel.getLokasiHotel());
        holder.txtHarga.setText("Rp " + formatter.format(hotel.getHarga()));
        Glide.with(context)
                .load(HotelAPI.URL_IMAGE + hotel.getGambar())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivGambar);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("hotel", hotel);
                data.putString("status", "edit");
                TambahEditHotel bookingHotel = new TambahEditHotel();
                bookingHotel.setArguments(data);
                loadFragment(bookingHotel);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Anda yakin ingin menghapus hotel ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteChart(hotel.getIdHotel());
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public class adapterHotelAdminViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNamaHotel, tvLokasiHotel, txtHarga;
        private ImageView ivGambar;
        private MaterialButton edit, delete;
        private CardView cardHotel;

        public adapterHotelAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaHotel = itemView.findViewById(R.id.tvNamaHotel);
            tvLokasiHotel = itemView.findViewById(R.id.tvLokasiHotel);
            txtHarga = itemView.findViewById(R.id.tvHarga);
            ivGambar = itemView.findViewById(R.id.ivGambar);
            cardHotel = itemView.findViewById(R.id.cardHotel);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }


    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment).commit();
    }

    public void deleteChart(int idChart){
        //Tambahkan hapus buku disini
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data hotel");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(DELETE, HotelAPI.URL_DELETE + idChart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                        progressDialog.dismiss();
                        try {
                            //Mengubah response string menjadi object
                            JSONObject obj = new JSONObject(response);

                            //obj.getString("message") digunakan untuk mengambil pesan message dari response
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                            notifyDataSetChanged();
                            mListener.deleteItem(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}
