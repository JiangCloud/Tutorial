package com.cloud.xtilus.makingfriends.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.bean.User;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class UserIntroduceActivity extends AppCompatActivity {

    private  User us;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_user_introduce);
        Intent intent=getIntent();
        us= (User) intent.getSerializableExtra("user");
       // User us= (User) bundle.get("user");
        String imageUrl= Constant.BASE_URL+ us.getHeadPicUrl();

        ImageView imageView= (ImageView) findViewById(R.id.pic);
        DisplayImageOptions  options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.phone)			// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)	// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.default_ptr_flip)		// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)							// 设置下载的图片是否缓存在SD卡中
                        //.imageScaleType(ImageScaleType.NONE)   //图片不会调整   //EXACTLY_STRETCHED 图片会缩放到目标大小完全。非常重要，也就是说，这个view有多大，图片就会缩放到多大
                .displayer(new RoundedBitmapDisplayer(20))//显示圆角图片
                .build();
        ImageLoader.getInstance().displayImage(imageUrl.trim(), imageView, options);


    }






}
