package com.arch.base.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

public class ImageUtils {
    public static void loadImageByString(Context context, ImageView imageView, String pathOrUrl, int defaultResId, int placeholder) {
        Glide.with(context).load(pathOrUrl).diskCacheStrategy(DiskCacheStrategy.ALL).error(defaultResId).placeholder(placeholder).into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param imageView
     * @param radiusDp  圆角的半径 （单位：dp）
     * @param url
     */
    public static void loadRoundImage(Context context, ImageView imageView, int radiusDp, String url) {
        Glide.with(context)
              .load(url)
              .diskCacheStrategy(DiskCacheStrategy.ALL)
              .fitCenter()
              .centerCrop()
              .transform(new RoundedCorners(radiusDp))
              .into(imageView);
    }

    public static void loadCircleImage(Context context, ImageView imageView, String url, int defaultImgId) {
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed()) {
                return;
            }
        }

        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(defaultImgId);
            return;
        }


        Glide.with(context)
              .load(url)
              .diskCacheStrategy(DiskCacheStrategy.ALL)
              .placeholder(defaultImgId)
              .priority(Priority.HIGH)
              .fitCenter()
              .centerCrop()
              .dontAnimate()
//    new RoundedCorners(16)
              .transform(new CircleCrop())
              .error(defaultImgId)
              .into(imageView);


    }

    public static Drawable bitmapToDrawable(Bitmap bitmap, Context context) {
        BitmapDrawable drawbale = new BitmapDrawable(context.getResources(),
              bitmap);
        return drawbale;
    }
}
