package com.cloud.xtilus.makingfriends.section.chat.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cloud.xtilus.makingfriends.common.livedatas.SingleSourceLiveData;
import com.cloud.xtilus.makingfriends.common.net.Resource;
import com.cloud.xtilus.makingfriends.common.repositories.DemoMediaManagerRepository;
import com.hyphenate.easeui.model.VideoEntity;

import java.util.List;

public class VideoListViewModel extends AndroidViewModel {
    private SingleSourceLiveData<Resource<List<VideoEntity>>> videoListObservable;
    private DemoMediaManagerRepository repository;

    public VideoListViewModel(@NonNull Application application) {
        super(application);
        repository = new DemoMediaManagerRepository();
        videoListObservable = new SingleSourceLiveData<>();
    }

    public LiveData<Resource<List<VideoEntity>>> getVideoListObservable() {
        return videoListObservable;
    }

    public void getVideoList(Context context) {
        videoListObservable.setSource(repository.getVideoListFromMediaAndSelfFolder(context));
    }

}
