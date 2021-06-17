package com.cloud.xtilus.makingfriends.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cloud.xtilus.makingfriends.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private DisplayImageOptions options;
    @ViewInject(R.id.pager)
    private ViewPager viewPager;
    @ViewInject(R.id.viewGroup)
    private LinearLayout viewGroup;
     // 装点点的ImageView数组
    private ImageView[] tips;

    private List ImgList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager_fragment);
        ViewUtils.inject(this);
        initTips(0);
        init();

    }



    private  void  initTips( int position){


        tips=new ImageView[8];
        for(int i=0;i<6;i++){

            //动态创建imageView
            ImageView imageViews = new ImageView(this);
            LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(10,10);
            para.setMargins(20,0,0,0);
            imageViews.setLayoutParams(para);


             tips[i]=imageViews;
            if(position == 0){

                imageViews.setBackgroundResource(R.mipmap.page_indicator_focused);
             }else{

                imageViews.setBackgroundResource(R.mipmap.page_indicator_unfocused);

        }

             viewGroup.addView(imageViews);
        }

    }


    private  void init() {
        ImgList=getImgPathList();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_stub)
                .showImageOnFail(R.mipmap.ic_launcher)
                .resetViewBeforeLoading(true)
                //.cacheOnDisk(true)
                .imageScaleType(ImageScaleType.NONE)
               // .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        viewPager.setAdapter(new ImageAdapter(ImgList, getBaseContext()));
        Intent intent = getIntent();
        int curNum = (int) intent.getExtras().get("position");
        viewPager.setCurrentItem(curNum);
        viewPager.setOnPageChangeListener(this);

    }



    // 获取手机图片地址列表
    private ArrayList<String> getImgPathList() {
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { "_id", "_data" }, null, null,  MediaStore.Images.Media._ID +" DESC");
        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));// 将图片路径添加到list中
        }
        cursor.close();
        return list;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setImageBackground(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<6; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.mipmap.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
            }
        }
    }

    private class ImageAdapter extends PagerAdapter {
       private LayoutInflater inflater;
        private List<String> imagUrl;
        private  Context context;
        public  ImageAdapter(List<String> urls,Context context){
            this.imagUrl=urls;
            this.context=context;

        }

        @Override
        public int getCount() {
            return imagUrl.size();
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            inflater=LayoutInflater.from(getBaseContext());
            View imageLayout=  inflater.inflate(R.layout.itern_pager_image, view, false);
            ImageView  imageView= (ImageView) imageLayout.findViewById(R.id.image);
            final   ProgressBar progressBar= (ProgressBar) imageLayout.findViewById(R.id.loading);

          //  LinearLayout group= (LinearLayout)imageLayout.findViewById(R.id.viewGroup);
            /*tips=new ImageView[imagUrl.size()];
            for(int i=0;i<6;i++){
                ImageView imageViews = new ImageView(context);
                imageViews.setLayoutParams(new ViewGroup.LayoutParams(10, 10));

                tips[i]=imageView;
                if(position == 0){
                    imageViews.setBackgroundResource(R.mipmap.page_indicator_focused);
                }else{
                    imageViews.setBackgroundResource(R.mipmap.page_indicator_unfocused);
                }

                group.addView(imageViews);

            }*/



            ImageLoader.getInstance().displayImage( "file://"+imagUrl.get(position).trim(), imageView, options, new SimpleImageLoadingListener(){
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }


                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    //super.onLoadingFailed(imageUri, view, failReason);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }
            });

            view.addView(imageLayout);
            return imageLayout;

        }





        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }
}
