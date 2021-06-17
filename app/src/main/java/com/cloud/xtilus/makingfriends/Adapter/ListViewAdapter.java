package com.cloud.xtilus.makingfriends.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by smyhvae on 2015/5/4.
 * 通用的ListView的BaseAdapter，所有的ListView的自定义adapter都可以继承这个类哦
 */
public abstract class ListViewAdapter<T> extends BaseAdapter {

    //为了让子类访问，于是将属性设置为protected
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId; //不同的ListView的item布局肯能不同，所以要把布局单独提取出来

    public ListViewAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //初始化ViewHolder,使用通用的ViewHolder，一样代码就搞定ViewHolder的初始化咯
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, layoutId, position);//layoutId就是单个item的布局

        convert(holder, getItem(position));
        return holder.getConvertView(); //这一行的代码要注意了
    }

    //将convert方法公布出去
    public abstract void convert(ViewHolder holder, T t);

}
