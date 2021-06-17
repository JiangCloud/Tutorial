package com.ycbjie.music.executor.online;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;

import com.ycbjie.music.api.OnLineMusicModel;
import com.ycbjie.music.model.bean.Album;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.model.bean.DownloadInfo;
import com.ycbjie.music.model.bean.MusicLrc;
import com.ycbjie.music.model.bean.OnlineMusicList;
import com.ycbjie.music.utils.FileMusicUtils;
import com.ycbjie.webviewlib.utils.OkHttpUtils;
import com.ycbjie.webviewlib.utils.X5LogUtils;

import java.io.File;
import java.io.FileWriter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;


/**
 * 播放在线音乐
 */
public abstract class AbsPlayOnlineMusic extends AbsPlayMusic {

    private OnlineMusicList.SongListBean mOnlineMusic;
    private Activity mActivity;
    private AudioBean music;

    public AbsPlayOnlineMusic(Activity activity, OnlineMusicList.SongListBean onlineMusic) {
        super(activity);
        this.mActivity = activity;
        mOnlineMusic = onlineMusic;

    }

    @Override
    public void onPrepare() {

    }

    @Override
    protected void getPlayInfo() {
        String artist = mOnlineMusic.getSingerName();
        String title = mOnlineMusic.getTitle();

        music = new AudioBean();
        music.setType(AudioBean.Type.ONLINE);
        music.setTitle(title);
        music.setArtist(artist);
       // music.setAlbum(mOnlineMusic.getTitle());
        music.setAlbum(mOnlineMusic.getAlbumMid());

        music.setMid(mOnlineMusic.getMid());
       // music.setAlbumId();

        // 下载歌词
        String lrcFileName = FileMusicUtils.getLrcFileName(artist, title);
        File lrcFile = new File(FileMusicUtils.getLrcDir() + lrcFileName);
        if (!lrcFile.exists() && !TextUtils.isEmpty(mOnlineMusic.getMid())) {
          //  downloadLrc(mOnlineMusic.getMid(),artist, title);
        }

        // 下载封面
        String albumFileName = FileMusicUtils.getAlbumFileName(artist, title);
        File albumFile = new File(FileMusicUtils.getAlbumDir(), albumFileName);
       // String picUrl = mOnlineMusic.getUrl();

       String albumMid =mOnlineMusic.getAlbumMid();
      /*  if (TextUtils.isEmpty(albumMid)) {
            picUrl = mOnlineMusic.getUrl();
        }*/


        if (!albumFile.exists() && !TextUtils.isEmpty(albumMid)) {


            //downloadAlbumPic(albumMid,albumFile);
        } else {
            mCounter++;
        }

        if(albumFile.exists()){
          //  music.setCoverPath(albumFile.getPath());
        }
       // music.setCoverPath(albumFile.getPath());

        // 获取歌曲播放链接
        getMusicInfo(mOnlineMusic.getMid(),mOnlineMusic.getInterval()*1000);
    }


    @SuppressLint("CheckResult")
    private void downloadAlbumPic(String songId,File albumFile ) {
        OnLineMusicModel model = OnLineMusicModel.getInstance();


        model.getMusicAlbumInfo(songId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Album>() {


                    @Override
                    public void accept(Album album) throws Exception {
                        if (album.getData()==null) {
                            return;
                        }

                        String picUrl="http:"+album.getData().getPicUrl();
                        OkHttpUtils.downloadFile(mActivity.getBaseContext(), picUrl,albumFile, new OkHttpUtils.FileCallback() {
                                    @Override
                                    public void success() {
                                        music.setCoverPath(albumFile.getPath());
                                        X5LogUtils.e("---------========download file success");

                                    }

                                    @Override
                                    public void fail(int code, Exception e) {
                                        X5LogUtils.e("---------========"+e.getMessage());
                                    }
                                });


                       // String filePath = FileMusicUtils.getAlbumDir() + FileMusicUtils.getAlbumFileName(artist, title);
                       // FileMusicUtils.saveLrcFile(filePath, album.getData().getPicUrl());
                    }


                });
    }



    @SuppressLint("CheckResult")
    private void downloadLrc(String songId,String artist, String title ) {
        OnLineMusicModel model = OnLineMusicModel.getInstance();
        model.getLrc(OnLineMusicModel.METHOD_LRC,songId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MusicLrc>() {
                    @Override
                    public void accept(MusicLrc musicLrc) throws Exception {
                        if (musicLrc == null || TextUtils.isEmpty(musicLrc.getData().getLyric())) {
                            return;
                        }
                        String filePath = FileMusicUtils.getLrcDir() + FileMusicUtils.getLrcFileName(artist, title);
                        FileMusicUtils.saveLrcFile(filePath, musicLrc.getData().getLyric());
                        //onExecuteSuccess(filePath);
                    }
                });
    }







    @SuppressLint("CheckResult")
    private void getMusicInfo(String songId, int duration) {
        OnLineMusicModel model = OnLineMusicModel.getInstance();
        model.getMusicDownloadInfo(OnLineMusicModel.METHOD_DOWNLOAD_MUSIC,songId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadInfo>() {
                    @Override
                    public void accept(DownloadInfo downloadInfo) throws Exception {
                        if (downloadInfo == null || downloadInfo.getData() == null) {
                            onExecuteFail(null);
                            return;
                        }
                        music.setPath(downloadInfo.getData());
                        music.setDuration(duration);
                        checkCounter(music);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof RuntimeException) {
                            onExecuteFail(null);
                        }
                    }
                },new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }


}
