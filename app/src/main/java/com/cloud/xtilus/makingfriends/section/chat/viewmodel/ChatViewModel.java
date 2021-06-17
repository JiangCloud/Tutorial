package com.cloud.xtilus.makingfriends.section.chat.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cloud.xtilus.makingfriends.common.livedatas.SingleSourceLiveData;
import com.cloud.xtilus.makingfriends.common.net.Resource;
import com.cloud.xtilus.makingfriends.common.repositories.EMChatRoomManagerRepository;
import com.cloud.xtilus.makingfriends.section.conversation.viewmodel.ConversationListViewModel;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;

public class ChatViewModel extends ConversationListViewModel {
    private EMChatRoomManagerRepository chatRoomManagerRepository;
    private SingleSourceLiveData<Resource<EMChatRoom>> chatRoomObservable;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatRoomManagerRepository = new EMChatRoomManagerRepository();
        chatRoomObservable = new SingleSourceLiveData<>();
    }

    public LiveData<Resource<EMChatRoom>> getChatRoomObservable() {
        return chatRoomObservable;
    }

    public void getChatRoom(String roomId) {
        EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(roomId);
        if(room != null) {
            chatRoomObservable.setSource(new MutableLiveData<>(Resource.success(room)));
        }else {
            chatRoomObservable.setSource(chatRoomManagerRepository.getChatRoomById(roomId));
        }
    }

}
