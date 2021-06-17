package com.cloud.xtilus.makingfriends.cache;

/**
 * Created by Administrator on 2018/10/8.
 */

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.cloud.xtilus.makingfriends.bean.ResponseBean;
import com.cloud.xtilus.makingfriends.http.BaseCallback;
import com.cloud.xtilus.makingfriends.http.OkHttpHelper;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.util.EMLog;
import com.j256.ormlite.dao.Dao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 用户缓存管理类
 * Created by Martin on 2017/4/24.
 */
public class UserCacheManager {

    /**
     * 消息扩展属性
     */
    private static final String kChatUserId = "ChatUserId";// 环信ID
    private static final String kChatUserNick = "ChatUserNick";// 昵称
    private static final String kChatUserPic = "ChatUserPic";// 头像Url

    /**
     * 获取所有用户信息
     * @return
     */
    public static List<UserCacheInfo> getAll(){
        Dao<UserCacheInfo, Integer> dao = SqliteHelper.getInstance().getUserDao();
        try {
            List<UserCacheInfo> list = dao.queryForAll();

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /***
     * 更新群名称
     * @param  groupId
     * @return  int
     */

    public static int updateGroupInfo(String groupId,String groupName){
        int row=0;

        UserCacheInfo userCacheInfo = getFromCache(groupId);

        if(userCacheInfo!=null){
            userCacheInfo.setUserId(groupId);
            userCacheInfo.setNickName(groupName);
            EMLog.d("userCacheInfo","======"+groupId);
            EMLog.d("userCacheInfo","======"+groupName);
            Dao<UserCacheInfo, Integer> dao = SqliteHelper.getInstance().getUserDao();
            try {
                row = dao.update(userCacheInfo);

                EMLog.d("row","======"+row);
                return row;
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        return row;
    }



    /**
     * 获取用户信息
     * @param userId 用户环信ID
     * @return
     */
    public static UserCacheInfo get(final String userId){
        UserCacheInfo info = null;

        // 如果本地缓存不存在或者过期，则从存储服务器获取
        if (notExistedOrExpired(userId)){
            Map<String,Object > param=new HashMap<String,Object>();
            param.put("fxId",userId);
           if(userId!=null){
                Log.d("UsercacheManger","============="+userId);
            }

            OkHttpHelper.getInstance().post( Constant.URL_FIND_UserInfo,param, new BaseCallback<ResponseBean<UserWebInfo>>() {
                @Override
                public void onBeforeRequest(Request request) {

                }

                @Override
                public void onFailure(Request request, Exception e) {
                    Log.d("URL_FIND_UserInfo","URL_FIND_UserInfo============="+e.getMessage());

                }

                @Override
                public void onResponse(Response response) {

                }

                @Override
                public void onSuccess(Response response, ResponseBean<UserWebInfo> userWebInfoResponseBean) {


                    if(userWebInfoResponseBean.getRet_data() == null) return;

                   String url= getImageUrl(userWebInfoResponseBean.getRet_data().getAvatar());


                    save( userWebInfoResponseBean.getRet_data().getFxid(),userWebInfoResponseBean.getRet_data().getNick()
                            ,userWebInfoResponseBean.getRet_data().getAvatar());
                   // save(info.getOpenId(), info.getNickName(),info.getAvatarUrl());

                }



                @Override
                public void onError(Response response, int code, Exception e) {

                }
            });


/*
            UserWebManager.getUserInfoAync(userId, new UserWebManager.UserCallback() {
                @Override
                public void onCompleted(UserWebInfo info) {
                    if(info == null) return;



                    // 缓存到本地
                    // info.getOpenId() 为该用户的【环信ID】
                    save(info.getOpenId(), info.getNickName(),info.getAvatarUrl());
                }
            });*/
        }
        // 从本地缓存中获取用户数据
        info = getFromCache(userId);

        return info;
    }



    private static String getImageUrl(String url){


        DisplayImageOptions options = new DisplayImageOptions.Builder()
              //  .showImageOnLoading(R.mipmap.ic_stub)			// 设置图片下载期间显示的图片
                //.showImageForEmptyUri(R.mipmap.ic_launcher)	// 设置图片Uri为空或是错误的时候显示的图片
              //  .showImageOnFail(R.drawable.default_ptr_flip)		// 设置图片加载或解码过程中发生错误显示的图片
               // .cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)							// 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.NONE)   //图片不会调整   //EXACTLY_STRETCHED 图片会缩放到目标大小完全。非常重要，也就是说，这个view有多大，图片就会缩放到多大
                .build();
           ImageLoader.getInstance().loadImage(url,options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                Log.d("onLoadingFailed","onLoadingFailed======"+failReason.toString());
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                Log.d("onLoadingComplete","onLoadingComplete======"+s);

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
      // ImageLoader.getInstance().displayImage("file://"+url.get(position).trim(), picImageView, options);



        return null;

    }





    /**
     * 获取用户信息
     * @param userId 用户环信ID
     * @return
     */
    public static UserCacheInfo getFromCache(String userId){

        try {
            Dao<UserCacheInfo, Integer> dao = SqliteHelper.getInstance().getUserDao();
            UserCacheInfo model = dao.queryBuilder().where().eq("userId", userId).queryForFirst();
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    public static EaseUser getEaseUser(String userId){

        UserCacheInfo user = get(userId);
        if (user == null) return null;

        EaseUser easeUser = new EaseUser(userId);
        easeUser.setAvatar(user.getAvatarUrl());
        easeUser.setNickname(user.getNickName());

        return easeUser;
    }

    /**
     * 用户是否存在
     * @param userId 用户环信ID
     * @return
     */
    public static boolean isExisted(String userId){
        Dao<UserCacheInfo, Integer> dao = SqliteHelper.getInstance().getUserDao();
        try {
            long count = dao.queryBuilder().where().eq("userId", userId).countOf();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 用户不存在或已过期
     * @param userId 用户环信ID
     * @return
     */
    public static boolean notExistedOrExpired(String userId){
        Dao<UserCacheInfo, Integer> dao = SqliteHelper.getInstance().getUserDao();
        try {
            long count = dao.queryBuilder().where()
                    .eq("userId", userId).and()
                    .gt("expiredDate",new Date().getTime()).countOf();
            return count <= 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 缓存用户信息
     * @param userId 用户环信ID
     * @param avatarUrl 头像Url
     * @param nickName 昵称
     * @return
     */
    public static boolean save(String userId, String nickName, String avatarUrl){
        try {
            Dao<UserCacheInfo, Integer> dao = SqliteHelper.getInstance().getUserDao();

            UserCacheInfo user = getFromCache(userId);

            // 新增
            if (user == null){
                user = new UserCacheInfo();
            }

            user.setUserId(userId);
            user.setAvatarUrl(avatarUrl);
            user.setNickName(nickName);
            user.setExpiredDate(new Date().getTime() + 24*60*60*1000);// 一天过期，单位：毫秒

            Dao.CreateOrUpdateStatus status = dao.createOrUpdate(user);

            if(status.getNumLinesChanged() > 0){
                Log.i("UserCacheManager", "操作成功~");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("UserCacheManager", "操作异常~");
        }

        return false;
    }

    /**
     * 更新当前用户的昵称
     * @param nickName 昵称
     */
    public static void updateMyNick(String nickName){
        UserCacheInfo user = getMyInfo();
        if (user == null)  return;

        save(user.getUserId(), nickName, user.getAvatarUrl());
    }


    /**
     * 更新当前用户的头像
     * @param avatarUrl 头像Url（完成路径）
     */
    public static void updateMyAvatar(String avatarUrl){
        UserCacheInfo user = getMyInfo();
        if (user == null)  return;

        save(user.getUserId(), user.getNickName(), avatarUrl);
    }

    /**
     * 缓存用户信息
     * @param model 用户信息
     * @return
     */
    public static boolean save(UserCacheInfo model){

        if(model == null) return false;

        return save(model.getUserId(),model.getNickName(),model.getAvatarUrl());
    }

    /**
     * 缓存用户信息
     * @param ext 用户信息
     * @return
     */
    public static boolean save(String ext){
        if(ext == null) return false;

        UserCacheInfo user = UserCacheInfo.parse(ext);
        return save(user);
    }

    /**
     * 缓存用户信息
     * @param ext 消息的扩展属性
     * @return
     */
    public static void save(Map<String,Object> ext){

        if(ext == null) return;

        try {
            String userId = ext.get(kChatUserId).toString();
            String avatarUrl = ext.get(kChatUserPic).toString();;
            String nickName = ext.get(kChatUserNick).toString();;

            save(userId,nickName,avatarUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前环信用户信息
     * @return
     */
    public static UserCacheInfo getMyInfo(){
        return get(EMClient.getInstance().getCurrentUser());
    }

    /**
     * 获取用户昵称
     * @return
     */
    public static String getMyNickName(){
        UserCacheInfo user = getMyInfo();
        if(user == null) return EMClient.getInstance().getCurrentUser();

        return user.getNickName();
    }

    /**
     * 设置消息的扩展属性
     * @param msg 发送的消息
     */
    public static void setMsgExt(EMMessage msg){
        if(msg == null) return;

        UserCacheInfo user = getMyInfo();
        msg.setAttribute(kChatUserId, user.getUserId());
        msg.setAttribute(kChatUserNick, user.getNickName());
        msg.setAttribute(kChatUserPic, user.getAvatarUrl());
    }

    /**
     * 获取登录用户的昵称头像
     * @return
     */
    public static String getMyInfoStr(){

        Map<String,Object> map = new HashMap<>();

        UserCacheInfo user = getMyInfo();
        map.put(kChatUserId, user.getUserId());
        map.put(kChatUserNick, user.getNickName());
        map.put(kChatUserPic, user.getAvatarUrl());

        return new Gson().toJson(map);
    }
}