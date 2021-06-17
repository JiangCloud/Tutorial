package com.cloud.xtilus.makingfriends.util.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cloud.xtilus.makingfriends.R;
import com.vmloft.develop.library.tools.picker.VMPickerLoader;


/**
 * Create by lzan13 on 2019/5/22 13:24
 *
 * 图片加载简单封装
 */
public class ALoader {

    /**
     * 加载图片
     *
     * @param context   上下文
     * @param options   加载图片配置
     * @param imageView 目标 view
     */
    public static void load(Context context, VMPickerLoader.Options options, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        if (options.isCircle) {
            requestOptions.circleCrop();
        } else if (options.isRadius) {
            requestOptions.transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(options.radiusSize)));
        }
        if (options.isBlur) {
            requestOptions.transform(new BlurTransformation());
        }
        Glide.with(context)
                .load(options.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.default_avatar)
                .apply(requestOptions)
                .thumbnail(placeholder(context, options))
                .into(imageView);
    }

    /**
     * 统一处理占位图
     *
     * @param context 上下文对象
     * @param options 加载配置
     * @return
     */
    private static RequestBuilder<Drawable> placeholder(Context context, VMPickerLoader.Options options) {
        int resId = R.mipmap.img_default_match;
        RequestOptions requestOptions = new RequestOptions();
        if (options.isCircle) {
            requestOptions.circleCrop();
        } else if (options.isRadius) {
            requestOptions.transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(options.radiusSize)));
        }
        if (options.isBlur) {
            requestOptions.transform(new BlurTransformation());
        }

        return Glide.with(context).load(resId).apply(requestOptions);
    }

    /**
     * 拼装图片路径
     */
    public static String wrapUrl(String name) {

        return null;
      //  return BuildConfig.uploadUrl + name;
    }
}
