package com.example.tubes_kelompok_d.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.tubes_kelompok_d.BookingHotel;
import com.example.tubes_kelompok_d.DetailChart;
import com.example.tubes_kelompok_d.R;
import com.example.tubes_kelompok_d.model.Chart;
import com.example.tubes_kelompok_d.model.ChartApi;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import static com.android.volley.Request.Method.DELETE;

public class AdapterChart extends RecyclerView.Adapter<AdapterChart.adapterChartlViewHolder> {

    private List<Chart> chartlList;
    private Context context;
    private View view;
    private AdapterChart.deleteItemListener mListener;

    public AdapterChart(Context context, List<Chart> chartlList,
                        AdapterChart.deleteItemListener mListener) {
        this.context         = context;
        this.chartlList      = chartlList;
        this.mListener       = mListener;
    }

    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public AdapterChart.adapterChartlViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.adapter_chart, parent, false);
        return new AdapterChart.adapterChartlViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChart.adapterChartlViewHolder holder, int position) {
        final Chart chart = chartlList.get(position);

        NumberFormat formatter = new DecimalFormat("#,###");
        holder.tvNamaHotel.setText(chart.getNama_hotel());
        holder.tvHarga.setText("Rp " + formatter.format(chart.getTotal_harga()));
        holder.tvCheckIn.setText("Check In " + chart.getCheck_in());
        holder.tvCheckOut.setText("Check Out " + chart.getCheck_out());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("chart", chart);
                data.putString("status", "edit");
                BookingHotel bookingHotel = new BookingHotel();
                bookingHotel.setArguments(data);
                loadFragment(bookingHotel);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Anda yakin ingin menghapus data ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteChart(chart.getIdChart());
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

        holder.cardHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                DetailChart dialog = new DetailChart();
                dialog.show(manager, "dialog");

                Bundle data = new Bundle();
                data.putSerializable("chart", chart);
                dialog.setArguments(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chartlList.size();
    }

    public class adapterChartlViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNamaHotel, tvHarga, tvCheckIn, tvCheckOut;
        private CardView cardHotel;
        private MaterialButton btnDelete, btnEdit;

        public adapterChartlViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaHotel = itemView.findViewById(R.id.tvNamaHotel);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            tvCheckIn = itemView.findViewById(R.id.tvCheckIn);
            tvCheckOut = itemView.findViewById(R.id.tvCheckOut);
            cardHotel = itemView.findViewById(R.id.cardHotel);
            btnDelete = itemView.findViewById(R.id.delete);
            btnEdit = itemView.findViewById(R.id.edit);
            cardHotel = itemView.findViewById(R.id.cardHotel);
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
        progressDialog.setTitle("Menghapus data booking");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(DELETE, ChartApi.URL_DELETE + idChart,
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
