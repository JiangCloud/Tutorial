package com.cloud.xtilus.makingfriends.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cloud.xtilus.makingfriends.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * Created by Administrator on 2016/4/10.
 */
public class PicturesAdapter extends BaseAdapter {

    private Context context;
    private List<String> url;
    public PicturesAdapter(Context context,List<String> imageUrl) {
        this.context=context;
        this.url=imageUrl;
    }

    @Override
    public int getCount() {
        return url.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View view=  layoutInflater.inflate(R.layout.pic_litern, null);
        ImageView picImageView= (ImageView) view.findViewById(R.id.imageView_pic);


        DisplayImageOptions   options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_stub)			// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)	// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.default_ptr_flip)		// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
                //.cacheOnDisk(true)							// 设置下载的图片是否缓存在SD卡中
                        .imageScaleType(ImageScaleType.NONE)   //图片不会调整   //EXACTLY_STRETCHED 图片会缩放到目标大小完全。非常重要，也就是说，这个view有多大，图片就会缩放到多大
                .build();
        picImageView.setImageResource(R.mipmap.add_friend);
        ImageLoader.getInstance().displayImage("file://"+url.get(position).trim(), picImageView, options);

        return view;
    }



}
