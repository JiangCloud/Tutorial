package com.cloud.xtilus.makingfriends.activity.details;

import com.alibaba.fastjson.JSONObject;
import com.cloud.xtilus.makingfriends.activity.BaseView;


/**
 * 项目名称：yichat0504
 * 类描述：UserDetailsView 描述:
 * 创建人：songlijie
 * 创建时间：2017/7/10 11:45
 * 邮箱:814326663@qq.com
 */
public interface UserDetailsView extends BaseView<UserDetailsPrester> {
    void onRefreshSuccess(String msg);
    void onRefreshFailed(String error);
    String getHxid();
    JSONObject getUserJson();
    void showDialog();
    void hintDialog();
    void showUi(JSONObject object);
}
