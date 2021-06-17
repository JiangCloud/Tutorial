package com.cloud.xtilus.makingfriends.activity.moments.details;

import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.xtilus.makingfriends.R;


import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.http.OkHttpUtils;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.cloud.xtilus.makingfriends.util.Param;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangfangyi on 2017/7/22.
 * qq 84543217
 */

public class MomentsPresenter implements MomentsContract.Presenter {

    private  JSONObject data;
    private  MomentsContract.View detailView;
    private String userId;

    public MomentsPresenter(MomentsContract.View detailView){
        this.detailView=detailView;
        detailView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    public void initData(Bundle bundle){

        String dataStr=bundle.getString("data");
        if(dataStr==null){
            String mid=bundle.getString("mid");
             getDataFromServer(mid);
        }else {
            String timeStamp=bundle.getString("time");
            data=JSONObject.parseObject(dataStr);
            detailView.initMomentView(data,timeStamp);
        }

        userId  = MakingFriendApplication.getInstance().getUserJson().getString(Constant.JSON_KEY_HXID);

    }

    @Override
    public void setGood(String aid,String fxId) {

        // 更新后台
        List<Param> params = new ArrayList<>();
        params.add(new Param("userId", userId));
        params.add(new Param("mid", aid));
        new OkHttpUtils(detailView.getBaseContext()).post(params, Constant.URL_SOCIAL_GOOD, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("ret_code");
                switch (code) {
                    case 200:
                        JSONArray praises = jsonObject.getJSONArray("praises");
                        detailView.updateGoodView( praises);

                        break;
                    case -1:
                        Toast.makeText(detailView.getBaseContext(), R.string.praise_failed, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(detailView.getBaseContext(), R.string.praise_failed, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(detailView.getBaseContext(), R.string.praise_failed_msg + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public void cancelGood(String gid) {
        List<Param> params = new ArrayList<>();
        params.add(new Param("pid", gid));
        new OkHttpUtils(detailView.getBaseContext()).post(params, Constant.URL_SOCIAL_GOOD_CANCEL, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("ret_code");
                switch (code) {
                    case 200:
                        JSONArray praises = jsonObject.getJSONArray("praises");
                        detailView.updateGoodView( praises);
                        break;

                    default:
                        Toast.makeText(detailView.getBaseContext(), R.string.praise_cancle_fail, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(detailView.getBaseContext(), R.string.praise_cancle_fail_msg + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void deleteComment(String cid) {
        List<Param> params = new ArrayList<>();
        params.add(new Param("cid", cid));
        new OkHttpUtils(detailView.getBaseContext()).post(params, Constant.URL_SOCIAL_DELETE_COMMENT, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("ret_code");
                switch (code) {
                    case 200:
                        JSONArray comments = jsonObject.getJSONArray("comment");
                        detailView.updateCommentView( comments);
                        break;
                    default:
                        Toast.makeText(detailView.getBaseContext(), R.string.delete_comment_failed, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(detailView.getBaseContext(), R.string.delete_comment_failed_msg + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void delete(String aid) {
        final List<Param> params = new ArrayList<>();
        params.add(new Param("mid", aid));
        new OkHttpUtils(detailView.getBaseContext()).post(params, Constant.URL_SOCIAL_DELETE, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("ret_code");
                switch (code) {
                    case 200:
                        detailView.finish();

                        break;

                    default:
                        Toast.makeText(detailView.getBaseContext(), R.string.delete_dynamic_failed, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(detailView.getBaseContext(), R.string.delete_dynamic_failed_msg + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void comment(String content) {
        String aid=data.getString("mid");
        List<Param> params = new ArrayList<>();
        params.add(new Param("userId", userId));
        params.add(new Param("cotent", content));
        params.add(new Param("mid", aid));
        new OkHttpUtils(detailView.getBaseContext()).post(params, Constant.URL_SOCIAL_COMMENT, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("ret_code");
                switch (code) {
                    case 200:
                        JSONArray comments = jsonObject.getJSONArray("comment");
                        detailView.updateCommentView( comments);
                        break;
                    case -1:
                        Toast.makeText(detailView.getBaseContext(), R.string.service_not_response, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(detailView.getBaseContext(), R.string.service_not_response, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(detailView.getBaseContext(), R.string.service_not_response, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void  getDataFromServer(String mid){
        List<Param> params=new ArrayList<>();
        params.add(new Param("mid",mid));
        new OkHttpUtils(detailView.getBaseContext()).post(params, Constant.URL_SOCIAL_GET_DETAIL, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code=jsonObject.getInteger("ret_code");
                if(code==200){
                    JSONObject dataTemp=jsonObject.getJSONArray("ret_data").getJSONObject(0);
                    if(dataTemp!=null){
                        String serverTime=dataTemp.getString("time");
                        data=dataTemp;
                        detailView.initMomentView(data,serverTime);
                    }
                }
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });

    }
}
