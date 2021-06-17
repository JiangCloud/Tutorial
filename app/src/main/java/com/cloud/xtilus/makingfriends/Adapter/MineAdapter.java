package com.cloud.xtilus.makingfriends.Adapter;

import android.content.Context;
import android.widget.TextView;

import com.cloud.xtilus.makingfriends.R;

import java.util.List;

/**
 * Created by cloud on 2016/4/10.
 */
public class MineAdapter extends ListViewAdapter<String> {

    public MineAdapter(Context context, List datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String o) {
      TextView tv_mineSeting= holder.getView(R.id.tv_mineSeting);
        tv_mineSeting.setText(o);

    }
}
