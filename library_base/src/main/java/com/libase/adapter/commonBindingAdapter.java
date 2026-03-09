package com.libase.adapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.libase.R;
import com.libase.utils.GlideUtils;

public class commonBindingAdapter {
    @BindingAdapter("ImageUri")
    public static void LoadImage(ImageView imageView , String uri)
    {
        if (uri!=null && !uri.isEmpty())
        {
            Glide.with(imageView.getContext())
                    .load(uri)
                    .into(imageView);
        }
//        else
//        {
//            Glide.with(imageView.getContext())
//                    .load(uri)
//                    .fallback(R)  //为null时的占位图
//                    .error(R.mipmap.icon_user_unlogin)
//                    .into(imageView);
//        }
    }

    /**
     * 加载圆形图片
     * @param imageView
     * @param uri
     */
    @BindingAdapter("CircleImageUri")
    public static void LoadCircleImage(ImageView imageView , String uri)
    {
        if (uri!=null && !uri.isEmpty())
        {
            Glide.with(imageView.getContext())
                    .load(uri)
                    .fallback(R.mipmap.icon_user_unlogin)  //为null时的占位图
                    .placeholder(R.mipmap.icon_user_unlogin)
                    .error(R.mipmap.icon_user_unlogin)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(imageView);
        }
        else
        {  //确保在为uri为null或者其他异常情况时有展占位图
            Glide.with(imageView.getContext())
                    .load(uri)
                    .fallback(R.mipmap.icon_user_unlogin)  //为null时的占位图
                    .error(R.mipmap.icon_user_unlogin)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(imageView);
        }
    }
}
