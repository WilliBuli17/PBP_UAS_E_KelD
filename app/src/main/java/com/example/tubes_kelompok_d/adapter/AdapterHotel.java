package com.example.tubes_kelompok_d.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tubes_kelompok_d.BookingHotel;
import com.example.tubes_kelompok_d.R;
import com.example.tubes_kelompok_d.model.HotelWeb;
import com.example.tubes_kelompok_d.model.HotelAPI;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterHotel extends RecyclerView.Adapter<AdapterHotel.adapterHotelViewHolder> {

    private List<HotelWeb> hotelList;
    private List<HotelWeb> hotelListFiltered;
    private Context context;
    private View view;

    public AdapterHotel(Context context, List<HotelWeb> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
        this.hotelListFiltered = hotelList;
    }

    @NonNull
    @Override
    public AdapterHotel.adapterHotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.adapter_hotel, parent, false);
        return new AdapterHotel.adapterHotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHotel.adapterHotelViewHolder holder, int position) {
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

        holder.booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("hotel", hotel);
                data.putString("status", "tambah");
                BookingHotel bookingHotel = new BookingHotel();
                bookingHotel.setArguments(data);
                loadFragment(bookingHotel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public class adapterHotelViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNamaHotel, tvLokasiHotel, txtHarga;
        private ImageView ivGambar;
        private MaterialButton booking;
        private CardView cardHotel;

        public adapterHotelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaHotel = itemView.findViewById(R.id.tvNamaHotel);
            tvLokasiHotel = itemView.findViewById(R.id.tvLokasiHotel);
            txtHarga = itemView.findViewById(R.id.tvHarga);
            ivGambar = itemView.findViewById(R.id.ivGambar);
            cardHotel = itemView.findViewById(R.id.cardHotel);
            booking = itemView.findViewById(R.id.booking);
        }
    }


    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment).commit();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString();
                if (userInput.isEmpty()) {
                    hotelListFiltered = hotelList;
                }
                else {
                    List<HotelWeb> filteredList = new ArrayList<>();
                    for(HotelWeb hotel : hotelList) {
                        if(String.valueOf(hotel.getNamaHotel()).toLowerCase().contains(userInput)) {
                            filteredList.add(hotel);
                        }
                    }
                    hotelListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = hotelListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                hotelListFiltered = (ArrayList<HotelWeb>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
