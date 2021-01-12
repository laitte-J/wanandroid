package com.arch.base.common;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class CommonBindingAdapters {

    @BindingAdapter("imageUrl")
    public static void loadImageUrl(ImageView view, String url) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(view.getContext())
                    .load(url)
                    .transition(withCrossFade())
                    .into(view);
        }
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, Boolean value) {
        view.setVisibility(value ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingConversion
    public static ColorStateList convertColorToColorStateList(int color) {
        return ColorStateList.valueOf(color);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter({"app:imageUrl", "app:placeHolder", "app:error"})
    public static void loadImage(ImageView imageView, String url, Drawable holderDrawable, Drawable errorDrawable) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(holderDrawable)
                .error(errorDrawable)
                .into(imageView);
    }

    @BindingAdapter({"app:imageUrlRadius"})
    public static void loadImageRadius(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.no_pic)
                .error(R.drawable.no_pic)
                .transform(new RoundedCorners(16))
                .into(imageView);
    }

    @BindingAdapter({"app:circleImageUrl"})
    public static void loadCircleImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.no_pic)
                .error(R.drawable.no_pic)
                .transform(new CircleCrop())
                .thumbnail(loadTransform(imageView.getContext(), R.drawable.no_pic))
                .thumbnail(loadTransform(imageView.getContext(), R.drawable.no_pic))
                .into(imageView);
    }


    private static RequestBuilder<Drawable> loadTransform(Context context, @DrawableRes int placeholderId) {

        return Glide.with(context)
                .load(placeholderId)
                .apply(new RequestOptions().centerCrop()
                        .transform(new CircleCrop()));

    }

    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.no_pic)
                .error(R.drawable.no_pic)
                .into(imageView);
    }

    @BindingAdapter({"app:htmlText"})
    public static void setHtml(TextView textView, String html) {
        textView.setText(Html.fromHtml(html));
    }

    @BindingAdapter({"app:tc"})
    public static void categoryTextColor(TextView textView, Boolean select) {
        if (select == null)
            select = false;
        textView.setTextColor((select) ? Color.parseColor("#0363F4") : Color.parseColor("#333333"));
    }

}
