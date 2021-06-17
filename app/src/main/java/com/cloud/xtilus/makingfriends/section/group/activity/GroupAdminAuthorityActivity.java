package com.cloud.xtilus.makingfriends.section.group.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.bean.ResponseBean;
import com.cloud.xtilus.makingfriends.cache.UserWebInfo;
import com.cloud.xtilus.makingfriends.common.constant.DemoConstant;
import com.cloud.xtilus.makingfriends.common.interfaceOrImplement.OnResourceParseCallback;
import com.cloud.xtilus.makingfriends.common.livedatas.LiveDataBus;
import com.cloud.xtilus.makingfriends.http.BaseCallback;
import com.cloud.xtilus.makingfriends.http.OkHttpHelper;
import com.cloud.xtilus.makingfriends.section.group.GroupHelper;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.util.EMLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class GroupAdminAuthorityActivity extends GroupMemberAuthorityActivity {

    public static void actionStart(Context context, String groupId) {
        Intent starter = new Intent(context, GroupAdminAuthorityActivity.class);
        starter.putExtra("groupId", groupId);
        context.startActivity(starter);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar.setTitle(getString(R.string.em_authority_menu_admin_list));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void getData() {
        viewModel.getRefreshObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<String>() {
                @Override
                public void onSuccess(String message) {
                    refreshData();
                    LiveDataBus.get().with(DemoConstant.GROUP_CHANGE).postValue(EaseEvent.create(DemoConstant.GROUP_CHANGE, EaseEvent.TYPE.GROUP));
                }
            });
        });
        viewModel.getGroupObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<EMGroup>() {
                @Override
                public void onSuccess(EMGroup group) {
                    List<String> adminList = group.getAdminList();
                    if(adminList == null) {
                        adminList = new ArrayList<>();
                    }
                    adminList.add(group.getOwner());

                    getAdminUserInfo(adminList);


                }

                @Override
                public void hideLoading() {
                    super.hideLoading();
                    finishRefresh();
                }
            });
        });
        viewModel.getMessageChangeObservable().with(DemoConstant.GROUP_CHANGE, EaseEvent.class).observe(this, event -> {
            if(event == null) {
                return;
            }
            if(event.isGroupChange()) {
                refreshData();
            }else if(event.isGroupLeave() && TextUtils.equals(groupId, event.message)) {
                finish();
            }
        });
        viewModel.getTransferOwnerObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    LiveDataBus.get().with(DemoConstant.GROUP_CHANGE).postValue(EaseEvent.create(DemoConstant.GROUP_OWNER_TRANSFER, EaseEvent.TYPE.GROUP));
                    finish();
                }
            });
        });
        refreshData();
    }


    /***
     *
     * @param adminList
     */
    private void getAdminUserInfo(List<String> adminList) {


        if (!adminList.isEmpty()) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userNames", adminList);
            params.put("requestType", Constant.REQEUSR_TYPE_list);
            List<EaseUser> easeUsers = new ArrayList<EaseUser>();
            List<String> finalAdminList = adminList;
            OkHttpHelper.getInstance().post(Constant.URL_FIND_UserInfos, params, new BaseCallback<ResponseBean<List<UserWebInfo>>>() {
                @Override
                public void onBeforeRequest(Request request) {

                }

                @Override
                public void onFailure(Request request, Exception e) {

                }

                @Override
                public void onResponse(Response response) {

                }

                @Override
                public void onSuccess(Response response, ResponseBean<List<UserWebInfo>> listResponseBean) {

                    if (listResponseBean.getRet_code() == 200) {
                        List<UserWebInfo> users = listResponseBean.getRet_data();

                        if (users != null && !users.isEmpty()) {

                            for (int i = 0; i < users.size(); i++) {
                                EaseUser user = new EaseUser(finalAdminList.get(i));
                                user.setNickname(users.get(i).getNick());
                                user.setAvatar(users.get(i).getAvatar());
                                EMLog.d("nick", "======" + users.get(i).getNick());
                                EMLog.d("avatar", "======" + users.get(i).getAvatar());
                                EMLog.d("finalAdminList", "======" + finalAdminList.get(i));

                                // EaseCommonUtils.setUserInitialLetter(user);
                                easeUsers.add(user);

                            }
                            adapter.setData(easeUsers);
                        }


                    }


                }

                @Override
                public void onError(Response response, int code, Exception e) {

                }
            });
        }


    }
            protected void refreshData() {
        viewModel.getGroup(groupId);
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        String username = adapter.getItem(position).getUsername();
        //不能操作群主
        if(TextUtils.equals(group.getOwner(), username)) {
            return false;
        }
        //管理员不能操作
        if(GroupHelper.isAdmin(group)) {
            return false;
        }
        return super.onItemLongClick(view, position);
    }
}
