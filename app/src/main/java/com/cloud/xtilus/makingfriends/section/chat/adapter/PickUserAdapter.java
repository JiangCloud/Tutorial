package com.cloud.xtilus.makingfriends.section.chat.adapter;


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
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseImageView;

public class PickUserAdapter extends EaseBaseRecyclerViewAdapter<EaseUser> {
    @Override
    public ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new PickUserViewHolder(LayoutInflater.from(mContext).inflate(R.layout.demo_widget_contact_item, parent, false));
    }

    private class PickUserViewHolder extends ViewHolder<EaseUser> {
        private TextView mHeader;
        private EaseImageView mAvatar;
        private TextView mName;
        private TextView mSignature;
        private TextView mUnreadMsgNumber;

        public PickUserViewHolder(@NonNull View itemView) {
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
            String header = EaseCommonUtils.getLetter(item.getNickname());
            Log.e("TAG", "GroupContactAdapter header = "+header);
            mHeader.setVisibility(View.GONE);
            // 是否显示字母
            if(position == 0 || (header != null && !header.equals(EaseCommonUtils.getLetter(getItem(position - 1).getNickname())))) {
                if(!TextUtils.isEmpty(header)) {
                    mHeader.setVisibility(View.VISIBLE);
                    mHeader.setText(header);
                }
            }
            Glide.with(mContext).load(item.getAvatar())
                    .apply(RequestOptions.placeholderOf(R.drawable.ease_default_avatar)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)).into(mAvatar);

            mName.setText(item.getNickname());
        }
    }
}
