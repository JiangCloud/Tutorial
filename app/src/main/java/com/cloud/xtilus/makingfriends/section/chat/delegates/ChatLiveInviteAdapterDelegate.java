package com.cloud.xtilus.makingfriends.section.chat.delegates;

import android.view.View;
import android.view.ViewGroup;

import com.cloud.xtilus.makingfriends.common.constant.DemoConstant;
import com.cloud.xtilus.makingfriends.section.chat.views.ChatRowLive;
import com.hyphenate.chat.EMMessage;
import com.cloud.xtilus.makingfriends.section.chat.viewholder.ChatLiveInviteViewHolder;
import com.hyphenate.easeui.delegate.EaseMessageAdapterDelegate;
import com.hyphenate.easeui.interfaces.MessageListItemClickListener;
import com.hyphenate.easeui.viewholder.EaseChatRowViewHolder;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;

import static com.hyphenate.chat.EMMessage.Type.TXT;

public class ChatLiveInviteAdapterDelegate extends EaseMessageAdapterDelegate<EMMessage, EaseChatRowViewHolder> {
    @Override
    public boolean isForViewType(EMMessage item, int position) {
        return item.getType() == TXT && !item.getStringAttribute(DemoConstant.EM_CONFERENCE_OP, "").equals("");
    }

    @Override
    protected EaseChatRow getEaseChatRow(ViewGroup parent, boolean isSender) {
        return new ChatRowLive(parent.getContext(), isSender);
    }

    @Override
    protected EaseChatRowViewHolder createViewHolder(View view, MessageListItemClickListener itemClickListener) {
        return new ChatLiveInviteViewHolder(view, itemClickListener);
    }
}
