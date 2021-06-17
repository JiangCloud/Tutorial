package com.cloud.xtilus.makingfriends.section.chat.viewholder;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.cloud.xtilus.makingfriends.common.constant.DemoConstant;
import com.cloud.xtilus.makingfriends.common.manager.PushAndMessageHelper;
import com.cloud.xtilus.makingfriends.section.chat.views.ChatRowConferenceInvite;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.interfaces.MessageListItemClickListener;
import com.hyphenate.easeui.viewholder.EaseChatRowViewHolder;

public class ChatConferenceInviteViewHolder extends EaseChatRowViewHolder {

    public ChatConferenceInviteViewHolder(@NonNull View itemView, MessageListItemClickListener itemClickListener) {
        super(itemView, itemClickListener);
    }

    public static ChatConferenceInviteViewHolder create(ViewGroup parent, boolean isSender,
                                                        MessageListItemClickListener itemClickListener) {
        return new ChatConferenceInviteViewHolder(new ChatRowConferenceInvite(parent.getContext(), isSender), itemClickListener);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        super.onBubbleClick(message);
        String confId = message.getStringAttribute(DemoConstant.MSG_ATTR_CONF_ID, "");
        String confPassword = message.getStringAttribute(DemoConstant.MSG_ATTR_CONF_PASS,"");
        String extension = message.getStringAttribute(DemoConstant.MSG_ATTR_EXTENSION, "");
        PushAndMessageHelper.goConference(getContext(), confId, confPassword, extension);
    }
}
