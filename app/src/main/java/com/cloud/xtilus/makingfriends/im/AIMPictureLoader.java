package com.cloud.xtilus.makingfriends.im;

import android.content.Context;
import android.widget.ImageView;

import com.cloud.xtilus.makingfriends.util.glide.ALoader;
import com.vmloft.develop.library.tools.picker.VMPickerLoader;

/**
 * Create by lzan13 on 2019/5/23 09:57
 *
 * 实现 IM 图片加载接口
 */
public class AIMPictureLoader  extends VMPickerLoader {

    /**
     * 通过上下文对象加载图片
     *
     * @param context   上下文对象
     * @param options   加载配置
     * @param imageView 目标控件
     */
    @Override
    public void load(Context context, Options options, ImageView imageView) {
        ALoader.load(context, options, imageView);
    }
}
