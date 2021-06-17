package com.cloud.xtilus.makingfriends.section.me.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cloud.xtilus.makingfriends.common.livedatas.SingleSourceLiveData;
import com.cloud.xtilus.makingfriends.common.net.Resource;
import com.cloud.xtilus.makingfriends.common.repositories.EMPushManagerRepository;
import com.hyphenate.chat.EMPushManager;

public class PushStyleViewModel extends AndroidViewModel {
    private EMPushManagerRepository repository;
    private SingleSourceLiveData<Resource<Boolean>> pushStyleObservable;

    public PushStyleViewModel(@NonNull Application application) {
        super(application);
        repository = new EMPushManagerRepository();
        pushStyleObservable = new SingleSourceLiveData<>();
    }

    public LiveData<Resource<Boolean>> getPushStyleObservable() {
        return pushStyleObservable;
    }

    public void updateStyle(EMPushManager.DisplayStyle style) {
        pushStyleObservable.setSource(repository.updatePushStyle(style));
    }
}

