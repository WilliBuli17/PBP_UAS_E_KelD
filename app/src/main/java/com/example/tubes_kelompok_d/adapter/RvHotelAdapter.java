package com.example.tubes_kelompok_d.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import com.example.tubes_kelompok_d.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tubes_kelompok_d.databinding.RvHotelBinding;
import com.example.tubes_kelompok_d.model.Hotel;
import com.example.tubes_kelompok_d.R;

import java.util.ArrayList;

public class RvHotelAdapter extends RecyclerView.Adapter<RvHotelAdapter.RecycleViewHolder> {

    private ArrayList<Hotel> result;
    private Context context;

    public RvHotelAdapter(Context context,ArrayList<Hotel> result) {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public RvHotelAdapter.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        RvHotelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.rv_hotel,parent,false);
        return new RvHotelAdapter.RecycleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RvHotelAdapter.RecycleViewHolder holder, int position) {
        final Hotel hotel = result.get(position);
        holder.bind(hotel);

    }

    @Override
    public int getItemCount() {
        return (result!=null) ? result.size():0;
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder{
        private RvHotelBinding itemBind;


        public RecycleViewHolder(RvHotelBinding binding) {
            super(binding.getRoot());
            this.itemBind = binding;
        }

        public void bind(Hotel object) {
            itemBind.setVariable(BR.hotel,object);
            itemBind.executePendingBindings();
        }
    }
}
