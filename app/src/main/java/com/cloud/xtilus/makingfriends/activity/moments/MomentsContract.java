package com.cloud.xtilus.makingfriends.activity.moments;

import android.content.Intent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.xtilus.makingfriends.activity.BasePresenter;
import com.cloud.xtilus.makingfriends.activity.BaseView;


import java.util.List;

/**
 * Created by huangfangyi on 2017/7/11.
 * qq 84543217
 */

public interface MomentsContract {

    public interface View extends BaseView<Presenter> {
        void showInputMenu(int position, String aid,String fxId);
        void hideInputMenu();
        void showPicDialog(int type, String string);
        void onRefreshComplete();
        void refreshListView(String time);
        void updateCommentView(int position, JSONArray jsonArray);
        void updateGoodView(int position, JSONArray jsonArray);
        void showBackground(String url);
    }

    public interface Presenter extends BasePresenter {
        //获取数据
            List<JSONObject> getData();
            String getBackgroudMoment();
            void getCacheTime();
            void loadeData(int pageIndex);
            void setGood(int position, String aid,String fxId);
            void comment(int position, String aid, String content,String fxId);
            void cancelGood(int position, String gid);
            void deleteComment(int position, String cid);
            void onBarRightViewClicked();
            void deleteItem(int position, String aid);
            void startToPhoto(int type);
            void startToAlbum(int type);
            void onResult(int requestCode, int resultCode, Intent data);
            String getCurrentTime();
    }

}
