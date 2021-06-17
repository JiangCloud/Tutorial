package com.cloud.xtilus.makingfriends.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.xtilus.makingfriends.R;

import java.util.List;

/**
 * Created by Administrator on 2016/3/14.
 */
public class MyAdapter extends BaseAdapter {
   // private List datas;
    private Context context;
    private String [] texts=new String[]{"充值","提现","优惠活动","代理中心","在线客服"};
    private int[] images= new int[]
            {R.mipmap.icon_cztx,R.mipmap.icon_tzjl,R.mipmap.icon_yhhd,R.mipmap.icon_dlzx,R.mipmap.icon_onlineservice

            };

    public MyAdapter(Context context){
      //  this.datas=list;
       this.context=context;


    }


    @Override
    public int getCount() {
        return texts.length;
    }

    @Override
    public Object getItem(int position) {
        return  position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=  LayoutInflater.from(context);
        View view=  layoutInflater.inflate(R.layout.itern_nav_view,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.nav_imag);
        imageView.setImageResource(images[position]);
        TextView tv= (TextView) view.findViewById(R.id.nav_tv);
        tv.setText(texts[position]);
        return view;
    }
}
