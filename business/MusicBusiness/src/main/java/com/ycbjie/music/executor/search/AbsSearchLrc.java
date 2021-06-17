package com.ycbjie.music.executor.search;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.ycbjie.music.api.OnLineMusicModel;
import com.ycbjie.music.executor.inter.IExecutor;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.model.bean.MusicLrc;
import com.ycbjie.music.model.bean.SearchMusic;
import com.ycbjie.music.utils.FileMusicUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 如果本地歌曲没有歌词则从网络搜索歌词
 */
public abstract class AbsSearchLrc implements IExecutor<String> {


    private AudioBean audioBean;



    protected AbsSearchLrc(AudioBean audioBean) {
        this.audioBean = audioBean;
    }


    @Override
    public void execute() {
        onPrepare();

        if(audioBean.getType()==AudioBean.Type.ONLINE){

            downloadLrc(audioBean.getMid());
        }else {

            searchLrc();
        }



    }





    @SuppressLint("CheckResult")
    private void searchLrc() {
        OnLineMusicModel model = OnLineMusicModel.getInstance();
        model.startSearchMusic(OnLineMusicModel.METHOD_SEARCH_MUSIC, audioBean.getArtist()+ "%2f" + audioBean.getTitle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchMusic>() {
                    @Override
                    public void accept(SearchMusic searchMusic) throws Exception {
                        if (searchMusic == null || searchMusic.getData() == null ||TextUtils.isEmpty(searchMusic.getData().getSongid()) ) {
                            return;
                        }
                        downloadLrc(searchMusic.getData().getSongmid());
                    }
                });

    }


    @SuppressLint("CheckResult")
    private void downloadLrc(String songMid) {
        OnLineMusicModel model = OnLineMusicModel.getInstance();
        model.getLrc(OnLineMusicModel.METHOD_LRC,songMid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MusicLrc>() {
                    @Override
                    public void accept(MusicLrc musicLrc) throws Exception {
                        if (musicLrc == null || TextUtils.isEmpty(musicLrc.getData().getLyric())) {
                            return;
                        }
                        String filePath = FileMusicUtils.getLrcDir() + FileMusicUtils.getLrcFileName(audioBean.getArtist(), audioBean.getTitle());
                        FileMusicUtils.saveLrcFile(filePath, musicLrc.getData().getLyric());
                        onExecuteSuccess(filePath);
                    }
                });
    }



}
