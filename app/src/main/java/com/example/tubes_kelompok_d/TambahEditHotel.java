package com.example.tubes_kelompok_d;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tubes_kelompok_d.model.HotelAPI;
import com.example.tubes_kelompok_d.model.HotelWeb;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class TambahEditHotel extends Fragment {

    private TextInputEditText txtNamaHotel, txtAlamat, txtHarga;
    private ImageView ivGambar;
    private Button btnSimpan, btnBatal, btnUnggah;
    private String status, selected;
    private int idHotel;
    private HotelWeb hotel;
    private View view;

    private Bitmap bitmap;
    private static final int PERMISSION_CODE = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tambah_edit_hotel, container, false);
        init();
        setAttribut();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void init(){
        hotel   = (HotelWeb) getArguments().getSerializable("hotel");
        txtNamaHotel         = view.findViewById(R.id.txtnamaHotel);
        txtAlamat    = view.findViewById(R.id.txtAlamat);
        txtHarga            = view.findViewById(R.id.txtHarga);
        btnSimpan           = view.findViewById(R.id.btnSimpan);
        btnUnggah           = view.findViewById(R.id.btnUnggah);
        btnBatal            = view.findViewById(R.id.btnBatal);
        ivGambar            = view.findViewById(R.id.ivGambar);

        status = getArguments().getString("status");
        if(status.equals("edit"))
        {
            idHotel = hotel.getIdHotel();
            txtNamaHotel.setText(hotel.getNamaHotel());
            txtAlamat.setText(hotel.getLokasiHotel());
            txtHarga.setText(String.valueOf(Math.round(hotel.getHarga())));
            Glide.with(view.getContext())
                    .load(HotelAPI.URL_IMAGE + hotel.getGambar())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivGambar);
        }
    }

    private void setAttribut() {
        btnUnggah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Layanan Ini Tidak Tersedia", Toast.LENGTH_SHORT).show();

                /*LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View view = layoutInflater.inflate(R.layout.pilih_media, null);

                final AlertDialog alertD = new AlertDialog.Builder(view.getContext()).create();

                Button btnKamera = view.findViewById(R.id.btnKamera);
                Button btnGaleri = view.findViewById(R.id.btnGaleri);

                btnKamera.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        selected="kamera";
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                            if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)==
                                    PackageManager.PERMISSION_DENIED ||
                                    getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                            PackageManager.PERMISSION_DENIED){
                                String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission,PERMISSION_CODE);
                            }
                            else{
                                openCamera();
                            }
                        }
                        else{
                            openCamera();
                        }
                        alertD.dismiss();
                    }
                });

                btnGaleri.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        selected="galeri";
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                            if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                    PackageManager.PERMISSION_DENIED){
                                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission,PERMISSION_CODE);
                            }
                            else{
                                openGallery();
                            }
                        }
                        else{
                            openGallery();
                        }
                        alertD.dismiss();
                    }
                });

                alertD.setView(view);
                alertD.show();*/
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaHotel  = txtNamaHotel.getText().toString();
                String alamat = txtAlamat.getText().toString();

                if(namaHotel.isEmpty() || alamat.isEmpty() || txtHarga.getText().toString().isEmpty())
                    Toast.makeText(getContext(), "Data Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show();
                else{
                    Double harga     = Double.parseDouble(txtHarga.getText().toString());
                    hotel = new HotelWeb(namaHotel, alamat, harga);
                    if(status.equals("edit"))
                        editBuku(idHotel);
                    else
                        tambahBuku();
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Admin.class)); }
        });
    }

    private void openGallery(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    if(selected.equals("kamera"))
                        openCamera();
                    else
                        openGallery();
                }else{
                    Toast.makeText(getContext() ,"Permision denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1)
        {
            Uri selectedImage = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            ivGambar.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);
        }
        else if(resultCode == RESULT_OK && requestCode == 2)
        {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            ivGambar.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void tambahBuku(){
        //Tambahkan tambah buku disini
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data Hotel");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, HotelAPI.URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan status dari response
                    if(obj.getString("message").equals("Add Hotel Succes")) {
                        startActivity(new Intent(getActivity(), Admin.class));
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
                params.put("nama_hotel", hotel.getNamaHotel());
                params.put("alamat_hotel", hotel.getLokasiHotel());
                params.put("harga_hotel", String.valueOf(hotel.getHarga()));

                /*if(bitmap != null) {
                    String imageData = setImageToString(bitmap);
                    params.put("foto_hotel", imageData);
                }*/
                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void editBuku(int idBuku) {
        //Tambahkan edit buku disini
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data Hotel");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(PUT, HotelAPI.URL_UPDATE + idBuku, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan status dari response
                    if(obj.getString("message").equals("Update Hotel Succes")) {
                        startActivity(new Intent(getActivity(), Admin.class));
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
                params.put("nama_hotel", hotel.getNamaHotel());
                params.put("alamat_hotel", hotel.getLokasiHotel());
                params.put("harga_hotel", String.valueOf(hotel.getHarga()));

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    private String setImageToString(Bitmap b){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}