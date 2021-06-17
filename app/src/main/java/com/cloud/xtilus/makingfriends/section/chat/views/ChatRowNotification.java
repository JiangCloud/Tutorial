package com.cloud.xtilus.makingfriends.section.chat.views;

import android.content.Context;
import android.widget.TextView;

import com.hyphenate.chat.EMTextMessageBody;
import com.cloud.xtilus.makingfriends.R;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;

public class ChatRowNotification extends EaseChatRow {
    private TextView contentView;

    public ChatRowNotification(Context context, boolean isSender) {
        super(context, isSender);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(R.layout.demo_row_notification, this);
    }

    @Override
    protected void onFindViewById() {
        contentView = findViewById(R.id.tv_chatcontent);
    }

    @Override
    protected void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        contentView.setText(txtBody.getMessage());
    }
}

