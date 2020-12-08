package com.example.tubes_kelompok_d.binding;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tubes_kelompok_d.R;

public class GlideBindingAdapter {

    @BindingAdapter("imageUrl")
    public static void setImage(ImageView view, int imageUrl){
        Context context = view.getContext();

        RequestOptions option = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background);

        Glide.with(context)
                .setDefaultRequestOptions(option)
                .load(imageUrl)
                .into(view);
    }

    @BindingAdapter("imageUrlString")
    public static void setImageString(ImageView view, String imageUrl){
        Context context = view.getContext();

        RequestOptions option = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background);

        Glide.with(context)
                .setDefaultRequestOptions(option)
                .load(imageUrl)
                .into(view);
    }
}
