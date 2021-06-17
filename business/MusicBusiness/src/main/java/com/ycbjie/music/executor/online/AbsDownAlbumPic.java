package com.ycbjie.music.executor.online;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.ycbjie.music.api.OnLineMusicModel;
import com.ycbjie.music.executor.inter.IExecutor;
import com.ycbjie.music.model.bean.Album;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.webviewlib.utils.OkHttpUtils;
import com.ycbjie.webviewlib.utils.X5LogUtils;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class AbsDownAlbumPic implements IExecutor<String> {

    private String albumMid;
    private Activity mActivity;
    private File albumFile;
    protected  AbsDownAlbumPic(Activity mActivity,String albumMid,File albumFile){

        this.albumMid=albumMid;
        this.mActivity=mActivity;
        this.albumFile=albumFile;


    }
    @Override
    public void execute() {
        onPrepare();
        downloadAlbumPic();
    }



    @SuppressLint("CheckResult")
    private void downloadAlbumPic() {
        OnLineMusicModel model = OnLineMusicModel.getInstance();


        model.getMusicAlbumInfo(albumMid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Album>() {

                    @Override
                    public void accept(Album album) throws Exception {
                        if (album.getData()==null) {
                            return;
                        }
                        String picUrl="http:"+album.getData().getPicUrl();

                        OkHttpUtils.downloadFile(mActivity, picUrl,albumFile, new OkHttpUtils.FileCallback() {
                            @Override
                            public void success() {
                                onExecuteSuccess(albumFile.getPath());
                                X5LogUtils.e("---------========download file success");

                            }
                            @Override
                            public void fail(int code, Exception e) {
                                X5LogUtils.e("---------========"+e.getMessage());
                            }
                        });

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof RuntimeException) {

                        }
                    }
                },new Action() {

                    @Override
                    public void run() throws Exception {

                    }
                });




    }





}
