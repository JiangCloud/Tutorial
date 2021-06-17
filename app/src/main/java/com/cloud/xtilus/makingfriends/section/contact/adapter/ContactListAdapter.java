package com.cloud.xtilus.makingfriends.section.contact.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cloud.xtilus.makingfriends.R;
import com.hyphenate.easeui.adapter.EaseBaseRecyclerViewAdapter;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseImageView;

public class ContactListAdapter extends EaseBaseRecyclerViewAdapter<EaseUser> {

    @Override
    public ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.demo_widget_contact_item, parent, false));
    }

    @Override
    public int getEmptyLayoutId() {
        return R.layout.demo_layout_friends_empty_list;
    }

    private class MyViewHolder extends ViewHolder<EaseUser> {
        private TextView mHeader;
        private EaseImageView mAvatar;
        private TextView mName;
        private TextView mSignature;
        private TextView mUnreadMsgNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void initView(View itemView) {
            mHeader = findViewById(R.id.header);
            mAvatar = findViewById(R.id.avatar);
            mName = findViewById(R.id.name);
            mSignature = findViewById(R.id.signature);
            mUnreadMsgNumber = findViewById(R.id.unread_msg_number);
        }

        @Override
        public void setData(EaseUser item, int position) {
            Log.e("TAG", "item = "+item.toString());
            String header = item.getInitialLetter();
            Log.e("TAG", "position = "+position + " header = "+header);
            mHeader.setVisibility(View.GONE);
            if(position == 0 || (header != null && !header.equals(getItem(position -1).getInitialLetter()))) {
                if(!TextUtils.isEmpty(header)) {
                    mHeader.setVisibility(View.VISIBLE);
                    mHeader.setText(header);
                }
            }
            Glide.with(mContext).load(item.getAvatar())
                    .apply(RequestOptions.placeholderOf(R.drawable.ease_default_avatar)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)).into(mAvatar);

            mName.setText(item.getNickname());
          //  mName.setText(item.getUsername());
        }
    }
}