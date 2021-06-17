package com.cloud.xtilus.makingfriends.section.conversation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.cloud.xtilus.makingfriends.section.dialog.DemoDialogFragment;
import com.cloud.xtilus.makingfriends.section.dialog.SimpleDialogFragment;
import com.cloud.xtilus.makingfriends.section.message.SystemMsgsActivity;
import com.cloud.xtilus.makingfriends.section.search.SearchConversationActivity;
import com.hyphenate.chat.EMConversation;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.common.constant.DemoConstant;
import com.cloud.xtilus.makingfriends.common.interfaceOrImplement.OnResourceParseCallback;
import com.cloud.xtilus.makingfriends.common.livedatas.LiveDataBus;
import com.cloud.xtilus.makingfriends.common.net.Resource;
import com.cloud.xtilus.makingfriends.common.utils.ToastUtils;
import com.cloud.xtilus.makingfriends.section.base.BaseActivity;
import com.cloud.xtilus.makingfriends.section.chat.activity.ChatActivity;
import com.cloud.xtilus.makingfriends.section.chat.viewmodel.MessageViewModel;
import com.cloud.xtilus.makingfriends.section.conversation.viewmodel.ConversationListViewModel;
import com.hyphenate.easeui.manager.EaseSystemMsgManager;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.easeui.modules.conversation.EaseConversationListFragment;
import com.hyphenate.easeui.modules.conversation.model.EaseConversationInfo;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseSearchTextView;



public class ConversationListFragment extends EaseConversationListFragment implements View.OnClickListener {
    private EaseSearchTextView tvSearch;

    private ConversationListViewModel mViewModel;

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //添加搜索会话布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.demo_layout_search, null);
        llRoot.addView(view, 0);
        tvSearch = view.findViewById(R.id.tv_search);
        conversationListLayout.getListAdapter().setEmptyLayoutId(R.layout.ease_layout_default_no_data);

        initViewModel();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item, int position) {
        EaseConversationInfo info = conversationListLayout.getItem(position);
        Object object = info.getInfo();

        if(object instanceof EMConversation) {
            switch (item.getItemId()) {
                case R.id.action_con_make_top :
                    conversationListLayout.makeConversationTop(position, info);
                    return true;
                case R.id.action_con_cancel_top :
                    conversationListLayout.cancelConversationTop(position, info);
                    return true;
                case R.id.action_con_delete :
                    showDeleteDialog(position, info);
                    return true;
            }
        }
        return super.onMenuItemClick(item, position);
    }

    private void showDeleteDialog(int position, EaseConversationInfo info) {
        new SimpleDialogFragment.Builder((BaseActivity) mContext)
                .setTitle(R.string.delete_conversation)
                .setOnConfirmClickListener(R.string.delete, new DemoDialogFragment.OnConfirmClickListener() {
                    @Override
                    public void onConfirmClick(View view) {
                        conversationListLayout.deleteConversation(position, info);
                    }
                })
                .showCancelButton(true)
                .show();
    }

    @Override
    public void initListener() {
        super.initListener();
        tvSearch.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(ConversationListViewModel.class);

        mViewModel.getDeleteObservable().observe(getViewLifecycleOwner(), response -> {
            parseResource(response, new OnResourceParseCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    LiveDataBus.get().with(DemoConstant.MESSAGE_CHANGE_CHANGE).postValue(new EaseEvent(DemoConstant.MESSAGE_CHANGE_CHANGE, EaseEvent.TYPE.MESSAGE));
                    //mViewModel.loadConversationList();
                    conversationListLayout.loadDefaultData();
                }
            });
        });

        mViewModel.getReadObservable().observe(getViewLifecycleOwner(), response -> {
            parseResource(response, new OnResourceParseCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    LiveDataBus.get().with(DemoConstant.MESSAGE_CHANGE_CHANGE).postValue(new EaseEvent(DemoConstant.MESSAGE_CHANGE_CHANGE, EaseEvent.TYPE.MESSAGE));
                    conversationListLayout.loadDefaultData();
                }
            });
        });

        MessageViewModel messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        LiveDataBus messageChange = messageViewModel.getMessageChange();
        messageChange.with(DemoConstant.NOTIFY_CHANGE, EaseEvent.class).observe(getViewLifecycleOwner(), this::loadList);
        messageChange.with(DemoConstant.MESSAGE_CHANGE_CHANGE, EaseEvent.class).observe(getViewLifecycleOwner(), this::loadList);
        messageChange.with(DemoConstant.GROUP_CHANGE, EaseEvent.class).observe(getViewLifecycleOwner(), this::loadList);
        messageChange.with(DemoConstant.CHAT_ROOM_CHANGE, EaseEvent.class).observe(getViewLifecycleOwner(), this::loadList);
        messageChange.with(DemoConstant.CONVERSATION_DELETE, EaseEvent.class).observe(getViewLifecycleOwner(), this::loadList);
        messageChange.with(DemoConstant.CONTACT_CHANGE, EaseEvent.class).observe(getViewLifecycleOwner(), this::loadList);
        messageChange.with(DemoConstant.MESSAGE_CALL_SAVE, Boolean.class).observe(getViewLifecycleOwner(), this::refreshList);
        messageChange.with(DemoConstant.MESSAGE_NOT_SEND, Boolean.class).observe(getViewLifecycleOwner(), this::refreshList);
    }

    private void refreshList(Boolean event) {
        if(event == null) {
            return;
        }
        if(event) {
            conversationListLayout.loadDefaultData();
        }
    }

    private void loadList(EaseEvent change) {
        if(change == null) {
            return;
        }
        if(change.isMessageChange() || change.isNotifyChange()
                || change.isGroupLeave() || change.isChatRoomLeave()
                || change.isContactChange()
                || change.type == EaseEvent.TYPE.CHAT_ROOM || change.isGroupChange()) {
            conversationListLayout.loadDefaultData();
        }
    }

    /**
     * 解析Resource<T>
     * @param response
     * @param callback
     * @param <T>
     */
    public <T> void parseResource(Resource<T> response, @NonNull OnResourceParseCallback<T> callback) {
        if(mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).parseResource(response, callback);
        }
    }

    /**
     * toast by string
     * @param message
     */
    public void showToast(String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search :
                SearchConversationActivity.actionStart(mContext);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        Object item = conversationListLayout.getItem(position).getInfo();
        if(item instanceof EMConversation) {
            if(EaseSystemMsgManager.getInstance().isSystemConversation((EMConversation) item)) {
                SystemMsgsActivity.actionStart(mContext);
            }else {
                ChatActivity.actionStart(mContext, ((EMConversation)item).conversationId(), EaseCommonUtils.getChatType((EMConversation) item));
            }
        }
    }
}
