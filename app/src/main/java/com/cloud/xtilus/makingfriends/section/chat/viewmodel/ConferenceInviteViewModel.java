package com.cloud.xtilus.makingfriends.section.chat.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cloud.xtilus.makingfriends.common.livedatas.SingleSourceLiveData;
import com.cloud.xtilus.makingfriends.common.net.Resource;
import com.cloud.xtilus.makingfriends.common.repositories.EMConferenceManagerRepository;
import com.cloud.xtilus.makingfriends.section.chat.model.KV;

import java.util.List;

public class ConferenceInviteViewModel extends AndroidViewModel {
    EMConferenceManagerRepository repository;
    private SingleSourceLiveData<Resource<List<KV<String, Integer>>>> conferenceInviteObservable;

    public ConferenceInviteViewModel(@NonNull Application application) {
        super(application);
        repository = new EMConferenceManagerRepository();
        conferenceInviteObservable = new SingleSourceLiveData<>();
    }

    public void getConferenceMembers(String groupId) {
        conferenceInviteObservable.setSource(repository.getConferenceMembers(groupId));
    }

    public LiveData<Resource<List<KV<String, Integer>>>> getConferenceInvite() {
        return conferenceInviteObservable;
    }
}
