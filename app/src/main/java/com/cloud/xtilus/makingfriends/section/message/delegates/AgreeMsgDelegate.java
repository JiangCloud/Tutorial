package com.cloud.xtilus.makingfriends.section.message.delegates;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.application.DemoHelper;
import com.cloud.xtilus.makingfriends.common.constant.DemoConstant;
import com.cloud.xtilus.makingfriends.common.db.entity.InviteMessageStatus;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.adapter.EaseBaseDelegate;
import com.hyphenate.easeui.adapter.EaseBaseRecyclerViewAdapter;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseDateUtils;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.exceptions.HyphenateException;

import java.util.Date;

public class AgreeMsgDelegate extends EaseBaseDelegate<EMMessage, AgreeMsgDelegate.ViewHolder> {

    private Context context;


    public AgreeMsgDelegate(Context context) {
        this.context = context;
    }

    @Override
    public boolean isForViewType(EMMessage msg, int position) {
        String statusParams = null;
        try {
            statusParams = msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_STATUS);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        InviteMessageStatus status = InviteMessageStatus.valueOf(statusParams);
        return status == InviteMessageStatus.BEAGREED
                || status == InviteMessageStatus.AGREED;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demo_layout_item_invite_msg_agree;
    }

    @Override
    protected AgreeMsgDelegate.ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    public class ViewHolder extends EaseBaseRecyclerViewAdapter.ViewHolder<EMMessage> {
        private TextView name;
        private TextView message;
        private EaseImageView avatar;
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void initView(View itemView) {
            name = findViewById(R.id.name);
            message = findViewById(R.id.message);
            avatar = findViewById(R.id.avatar);
            time = findViewById(R.id.time);
            avatar.setShapeType(DemoHelper.getInstance().getEaseAvatarOptions().getAvatarShape());
        }

        @Override
        public void setData(EMMessage msg, int position) {
            String reason = null;
            try {

                EaseUser easeUser= DemoHelper.getInstance().getUserInfoByUserName(msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM));
                name.setText(easeUser.getNickname());
                Glide.with(context).load(easeUser.getAvatar())
                        .apply(RequestOptions.placeholderOf(R.drawable.ease_default_avatar)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)).into(avatar);

                reason = msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_REASON);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            if(TextUtils.isEmpty(reason)) {
                String statusParams = null;
                try {
                    statusParams = msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_STATUS);
                    InviteMessageStatus status = InviteMessageStatus.valueOf(statusParams);
                    if(status == InviteMessageStatus.AGREED) {
                        reason = name.getContext().getString(InviteMessageStatus.AGREED.getMsgContent(), msg.getStringAttribute(DemoConstant.SYSTEM_MESSAGE_FROM));
                    }else if(status == InviteMessageStatus.BEAGREED) {
                        reason = name.getContext().getString(InviteMessageStatus.BEAGREED.getMsgContent());
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

            }
            message.setText(reason);
            time.setText(EaseDateUtils.getTimestampString(itemView.getContext(), new Date(msg.getMsgTime())));
        }
    }
}
