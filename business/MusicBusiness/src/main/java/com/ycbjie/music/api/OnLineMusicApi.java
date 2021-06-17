package com.ycbjie.music.api;


import com.ycbjie.music.model.bean.Album;
import com.ycbjie.music.model.bean.ArtistInfo;
import com.ycbjie.music.model.bean.DownloadInfo;
import com.ycbjie.music.model.bean.MusicLrc;
import com.ycbjie.music.model.bean.OnlineMusicList;
import com.ycbjie.music.model.bean.SearchMusic;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/03/22
 *     desc  : 百度音乐接口
 *     revise:
 * </pre>
 */
public interface OnLineMusicApi {


    /**
     * 最新音乐
     */
    @GET("top")
    Observable<OnlineMusicList> getList2(@Query("method") String method,
                                         @Query("id") int id,
                                         @Query("pageSize") int pageSize,
                                         @Query("period") String period);


    /**
     * 个人详情
     */
    @GET("singer/desc")
    Observable<ArtistInfo> getArtistInfo(@Query("singermid") String singermid);


    /**
     * 搜索音乐
     */
    @GET("song/find")
    Observable<SearchMusic> startSearchMusic(@Query("method") String method,
                                             @Query("key") String query);


    /**
     * 搜索音乐歌词
     */
    @GET("lyric")
    Observable<MusicLrc> getLrc(@Query("method") String method,
                                @Query("songmid") String songmid);



    /**
     * 获取歌词信息
     */
    @GET("top")
    Observable<OnlineMusicList> getSongListInfo(@Query("method") String method,
                                                @Query("id") int id,
                                                @Query("pageSize") String size,
                                                @Query("offset") String offset);



    /**
     * 获取下载链接
     */
    @GET("song/url")
    Observable<DownloadInfo> getMusicDownloadInfo(@Query("method") String method,
                                                  @Query("id") String songid);


    /**
     * 获取播放链接
     */
    @GET("song/urls")
    Observable<String> getMusicPlayUrl(@Query("method") String method,
                                                  @Query("id") String songid);


    /**
     * 获取专辑信息
     */

    @GET("album")
    Observable<Album> getMusicAlbumInfo(@Query("albummid") String albummid);
}
