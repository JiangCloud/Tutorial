package com.cloud.xtilus.makingfriends.Adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.bean.User;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.cloud.xtilus.makingfriends.util.Displayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/3/14.
 */
public class GetUserInfoAdapter extends ListViewAdapter<User> {

    DisplayImageOptions options;// DisplayImageOptions是用于设置图片显示的类


    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public GetUserInfoAdapter(Context context, List datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, User user) {
       ImageView head= holder.getView(R.id.headImage);
       //BaseImageDownloader.getStreamFromOtherSource()
        String imageUrl = Constant.BASE_URL+ user.getHeadPicUrl();
       /* 下面的加载网络图片中，用到了Android-Universal-Image-Loader框架
                */
        //显示图片的配置
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.chat_default_avatar)			// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)	// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.default_ptr_flip)		// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)							// 设置下载的图片是否缓存在SD卡中
                //.imageScaleType(ImageScaleType.NONE)   //图片不会调整   //EXACTLY_STRETCHED 图片会缩放到目标大小完全。非常重要，也就是说，这个view有多大，图片就会缩放到多大
                //.displayer(new RoundedBitmapDisplayer(20))//显示圆角图片
                .displayer(new Displayer(0))
                .build();
          ImageLoader.getInstance().displayImage(imageUrl.trim(), head, options);

        TextView tv_userNickName=  holder.getView(R.id.userNickName);
        TextView tv_makingFriendWord=holder.getView(R.id.makingFriendWord);
        TextView tv_userInfo= holder.getView(R.id.userInfo);
        if(user!=null){
            tv_userNickName.setText(user.getNickname());
            tv_userInfo.setText(String.valueOf(user.getAge()));
            tv_makingFriendWord.setText(user.getMakingfriendWord());
        }




    }
}
