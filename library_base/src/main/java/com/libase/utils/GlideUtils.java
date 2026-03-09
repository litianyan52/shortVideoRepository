package com.libase.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.libase.R;

public class GlideUtils {
    public static void LoadImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext())//传入上下文
                .load(url)
                .into(imageView);
    }

    public static void LoadAvatar(String url, ImageView imageView) {
        Glide.with(imageView.getContext())//传入上下文
                .load(url)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }
}
