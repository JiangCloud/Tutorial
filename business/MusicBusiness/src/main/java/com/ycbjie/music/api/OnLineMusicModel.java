package com.ycbjie.music.api;

import com.yc.httpserver.RetrofitWrapper;
import com.ycbjie.music.model.bean.Album;
import com.ycbjie.music.model.bean.ArtistInfo;
import com.ycbjie.music.model.bean.DownloadInfo;
import com.ycbjie.music.model.bean.MusicLrc;
import com.ycbjie.music.model.bean.OnlineMusicList;
import com.ycbjie.music.model.bean.SearchMusic;

import io.reactivex.Observable;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/03/22
 *     desc  : 百度音乐接口
 *     revise:
 * </pre>
 */
public class OnLineMusicModel {


    private static final String BASE_URL = "http://193.112.117.238:3300/";
    public static final String METHOD_LINE_MUSIC = "baidu.ting.billboard.billList";
    public static final String METHOD_ARTIST_INFO = "baidu.ting.artist.getInfo";
    public static final String METHOD_SEARCH_MUSIC = "baidu.ting.search.catalogSug";
    public static final String METHOD_LRC = "baidu.ting.song.lry";
    public static final String METHOD_GET_MUSIC_LIST = "baidu.ting.billboard.billList";
    public static final String METHOD_DOWNLOAD_MUSIC = "baidu.ting.song.play";



    private static OnLineMusicModel model;
    private OnLineMusicApi mApiService;

    private OnLineMusicModel() {
        mApiService = RetrofitWrapper
                .getInstance(BASE_URL)
                .create(OnLineMusicApi.class);
    }


    public static OnLineMusicModel getInstance(){
        if(model == null) {
            model = new OnLineMusicModel();
        }
        return model;
    }

    /**
     * 获取排行榜歌曲信息
     */
    public Observable<OnlineMusicList> getList(String method , int type, int pageSize, String period) {
        return mApiService.getList2(method, type, pageSize, period);
    }

    /**
     * 个人详情
     */
    public Observable<ArtistInfo> getArtistInfo( String singermid) {


        return mApiService.getArtistInfo(singermid);
    }


    /**
     * 搜索音乐
     */
    public Observable<SearchMusic> startSearchMusic(String method , String query) {
        return mApiService.startSearchMusic(method, query);
    }

    /**
     * 搜索音乐歌词
     */
    public Observable<MusicLrc> getLrc(String method , String songid) {
        return mApiService.getLrc(method, songid);
    }

    /**
     * 获取歌词信息
     */
    public Observable<OnlineMusicList> getSongListInfo(String method , int type , String size , String offset) {
        return mApiService.getSongListInfo(method, type, size,offset);
    }

    /**
     * 获取下载链接
     */
    public Observable<DownloadInfo> getMusicDownloadInfo(String method , String songid) {
        return mApiService.getMusicDownloadInfo(method, songid);
    }


    public Observable<String>  getMusicPlayUrl(String method , String songid) {
        return mApiService.getMusicPlayUrl(method, songid);
    }


    /**
     * 获取专辑信息
     */

    public Observable<Album> getMusicAlbumInfo(String albummid) {
        return mApiService.getMusicAlbumInfo(albummid);
    }




}
