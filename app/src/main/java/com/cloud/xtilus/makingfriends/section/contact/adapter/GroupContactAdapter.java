package com.cloud.xtilus.makingfriends.section.contact.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cloud.xtilus.makingfriends.application.DemoHelper;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.cloud.xtilus.makingfriends.R;
import com.hyphenate.easeui.adapter.EaseBaseRecyclerViewAdapter;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseImageView;

public class GroupContactAdapter extends EaseBaseRecyclerViewAdapter<EMGroup> {
    @Override
    public ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new GroupViewHolder(LayoutInflater.from(mContext).inflate(R.layout.demo_widget_contact_item, parent, false));
    }

    private class GroupViewHolder extends ViewHolder<EMGroup> {
        private TextView mHeader;
        private EaseImageView mAvatar;
        private TextView mName;
        private TextView mSignature;
        private TextView mLabel;
        private TextView mUnreadMsgNumber;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void initView(View itemView) {
            mHeader = findViewById(R.id.header);
            mAvatar = findViewById(R.id.avatar);
            mName = findViewById(R.id.name);
            mSignature = findViewById(R.id.signature);
            mLabel = findViewById(R.id.label);
            mUnreadMsgNumber = findViewById(R.id.unread_msg_number);
        }

        @Override
        public void setData(EMGroup item, int position) {

           //获取群信息
           EaseUser mUser= DemoHelper.getInstance().getUserInfoByUserName( item.getGroupId());

            Glide.with(mContext).load( mUser.getAvatar())
                    .apply(RequestOptions.placeholderOf(R.drawable.ease_group_icon)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)).into(mAvatar);

           // mAvatar.setImageResource(R.drawable.ease_group_icon);
            mSignature.setVisibility(View.VISIBLE);
            mName.setText(item.getGroupName());
            mSignature.setText(item.getGroupId()+"");
            mLabel.setVisibility(View.GONE);
            if(isOwner(item.getOwner())) {
                mLabel.setVisibility(View.VISIBLE);
                mLabel.setText(R.string.group_owner);
            }
        }
    }

    private boolean isOwner(String owner) {
        return TextUtils.equals(EMClient.getInstance().getCurrentUser(), owner);
    }
}
