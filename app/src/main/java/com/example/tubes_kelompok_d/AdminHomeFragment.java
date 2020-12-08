package com.example.tubes_kelompok_d;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
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
import com.example.tubes_kelompok_d.adapter.AdapterHotelAdmin;
import com.example.tubes_kelompok_d.model.HotelAPI;
import com.example.tubes_kelompok_d.model.HotelWeb;
import com.google.android.material.button.MaterialButton;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class AdminHomeFragment extends Fragment {

    private AdapterHotelAdmin adapter;
    private List<HotelWeb> listHotel;
    private View root;

    private MaterialButton add, cetak;

    private PdfViewModel pdfViewModel;

    private static final String TAG = "PdfCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 101;
    private File pdfFile;
    private PdfWriter writer;
    private AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_admin_home, container, false);
        loadDaftarHotel();

        add = root.findViewById(R.id.add);
        cetak = root.findViewById(R.id.cetak);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("status", "tambah");
                TambahEditHotel bookingHotel = new TambahEditHotel();
                bookingHotel.setArguments(data);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager .beginTransaction()
                        .replace(R.id.fragment_container, bookingHotel)
                        .commit();
            }
        });

        cetak.setOnClickListener(v -> {
            builder = new AlertDialog.Builder(getContext());

            builder.setCancelable(false);
            builder.setMessage("Apakah anda yakin ingin mencetak surat pemesanan ?");
            builder.setPositiveButton("Ya", (dialog, which) -> {

                try {
                    createPdfWrapper();
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }
            });
            builder.setNegativeButton("Tidak", (dialog, which) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.WHITE);
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.WHITE);
        });

        return root;
    }

    public void loadDaftarHotel(){
        setAdapter();
        getHotel();
    }

    public void setAdapter(){
        listHotel = new ArrayList<HotelWeb>();
        RecyclerView recyclerView = root.findViewById(R.id.rvMhs);
        adapter = new AdapterHotelAdmin(root.getContext(), listHotel, new AdapterHotelAdmin.deleteItemListener() {
            @Override
            public void deleteItem(Boolean delete) {
                if (delete){
                    loadDaftarHotel();
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

    private void createPdf() throws FileNotFoundException, DocumentException {
        //isikan code createPdf()
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Download/");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Direktori baru untuk file pdf berhasil dibuat");
        }
        //TODO 2.1 - Ubah NPM menjadi NPM anda
        String pdfname = "DaftarHotel"+".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
        writer = PdfWriter.getInstance(document, output);
        document.open();
        //TODO 2.2 - Ubah XXXX menjadi NPM anda
        Paragraph judul = new Paragraph("Update Daftar Hotel \n\n", new
                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 16,
                com.itextpdf.text.Font.BOLD, BaseColor.BLACK));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);
        PdfPTable tables = new PdfPTable(new float[]{10, 10});
        tables.getDefaultCell().setFixedHeight(50);
        tables.setTotalWidth(PageSize.A4.getWidth());
        tables.setWidthPercentage(100);
        tables.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell cellSupplier = new PdfPCell();
        cellSupplier.setPaddingLeft(20);
        cellSupplier.setPaddingBottom(10);
        cellSupplier.setBorder(Rectangle.NO_BORDER);
        //TODO 2.3 - Ubah Nama Praktikan menjadi nama anda
        Paragraph Kepada= new Paragraph(
                "Kepada Yth : \n" + "Styephen William Buli"+"\n",
                new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                        com.itextpdf.text.Font.NORMAL, BaseColor.BLACK)
        );
        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String tglDicetak = sdf.format(currentTime);

        cellSupplier.addElement(Kepada);
        tables.addCell(cellSupplier);
        PdfPCell cellPO = new PdfPCell();
        //TODO 2.4 - Ubah NPM Praktikan dengan NPM anda dan ubah Tanggal Praktikum sesuai tanggalpraktikum modul 11 kelas anda
        Paragraph NomorTanggal = new Paragraph(
                "No : " + "180709590" + "\n\n" +
                        "Tanggal : " + tglDicetak + "\n",
                new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                        com.itextpdf.text.Font.NORMAL, BaseColor.BLACK));
        NomorTanggal.setPaddingTop(5);
        tables.addCell(NomorTanggal);
        document.add(tables);
        com.itextpdf.text.Font f = new
                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
        Paragraph Pembuka = new Paragraph("\nBerikut Merupakan Update Daftar Hotel : \n\n",f);
        Pembuka.setIndentationLeft(20);
        document.add(Pembuka);
        PdfPTable tableHeader = new PdfPTable(new float[]{10,10,10});
        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.getDefaultCell().setFixedHeight(30);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);
        //TODO 2.5 - Bagian ini tidak perlu diubah
        PdfPCell h1 = new PdfPCell(new Phrase("Hotel"));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPaddingBottom(5);
        PdfPCell h2 = new PdfPCell(new Phrase("Lokasi"));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPaddingBottom(5);
        PdfPCell h4 = new PdfPCell(new Phrase("Harga/malam"));
        h4.setHorizontalAlignment(Element.ALIGN_CENTER);
        h4.setPaddingBottom(5);
        tableHeader.addCell(h1);
        tableHeader.addCell(h2);
        tableHeader.addCell(h4);
        PdfPCell[] cells = tableHeader.getRow(0).getCells();
        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(BaseColor.GRAY);
        }
        document.add(tableHeader);
        PdfPTable tableData = new PdfPTable(new float[]{10,10,10});
        tableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableData.getDefaultCell().setFixedHeight(30);
        tableData.setTotalWidth(PageSize.A4.getWidth());
        tableData.setWidthPercentage(100);
        tableData.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        int arrLength = listHotel.size();
        for(int x=0;x<arrLength;x++){
            for(int i=0;i<cells.length;i++){
                if(i==0){
                    tableData.addCell(listHotel.get(x).getNamaHotel());
                }else if(i==1){
                    tableData.addCell(listHotel.get(x).getLokasiHotel());
                }else{
                    tableData.addCell("Rp " + String.valueOf(listHotel.get(x).getHarga()));
                }
            }
        }
        document.add(tableData);
        com.itextpdf.text.Font h = new
                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                com.itextpdf.text.Font.NORMAL);


        Paragraph P = new Paragraph("\nDicetak tanggal " + tglDicetak, h);
        P.setAlignment(Element.ALIGN_RIGHT);
        document.add(P);
        document.close();
        previewPdf();

    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage("Izinkan aplikasi untuk akses penyimpanan?")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {
        //isikan code createPdfWrapper()
        int hasWriteStoragePermission = 0;
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel(
                            (dialog, which) -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new
                                                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();
        }
    }

    private void previewPdf() {
        //isikan code previewPdf()
        PackageManager packageManager = getContext().getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                uri = FileProvider.getUriForFile(getActivity(), getContext().getPackageName()+".provider",
                        pdfFile);
            } else {
                uri = Uri.fromFile(pdfFile);
            }
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(uri, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            //TODO 2.6 - Sesuaikan package dengan package yang anda buat
            getContext().grantUriPermission("com.example.tubes_kelompok_d", uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(pdfIntent);
        } else {
            FancyToast.makeText(getContext(),"Unduh pembuka PDF untuk menampilkan file ini",
                    FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
        }
    }
}