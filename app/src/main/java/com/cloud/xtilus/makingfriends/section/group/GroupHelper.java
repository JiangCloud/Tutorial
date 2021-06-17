package com.cloud.xtilus.makingfriends.section.group;

import android.text.TextUtils;

import com.cloud.xtilus.makingfriends.application.DemoHelper;
import com.cloud.xtilus.makingfriends.bean.ResponseBean;
import com.cloud.xtilus.makingfriends.cache.UserWebInfo;
import com.cloud.xtilus.makingfriends.http.BaseCallback;
import com.cloud.xtilus.makingfriends.http.OkHttpHelper;
import com.cloud.xtilus.makingfriends.section.contact.adapter.ContactListAdapter;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.util.EMLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class GroupHelper {

    /**
     * 是否是群主
     * @return
     */
    public static boolean isOwner(EMGroup group) {
        if(group == null ||
                TextUtils.isEmpty(group.getOwner())) {
            return false;
        }
        return TextUtils.equals(group.getOwner(), DemoHelper.getInstance().getCurrentUser());
    }

    /**
     * 是否是聊天室创建者
     * @return
     */
    public static boolean isOwner(EMChatRoom room) {
        if(room == null ||
                TextUtils.isEmpty(room.getOwner())) {
            return false;
        }
        return TextUtils.equals(room.getOwner(), DemoHelper.getInstance().getCurrentUser());
    }

    /**
     * 是否是管理员
     * @return
     */
    public synchronized static boolean isAdmin(EMGroup group) {
        List<String> adminList = group.getAdminList();
        if(adminList != null && !adminList.isEmpty()) {
            return adminList.contains(DemoHelper.getInstance().getCurrentUser());
        }
        return false;
    }

    /**
     * 是否是管理员
     * @return
     */
    public synchronized static boolean isAdmin(EMChatRoom group) {
        List<String> adminList = group.getAdminList();
        if(adminList != null && !adminList.isEmpty()) {
            return adminList.contains(DemoHelper.getInstance().getCurrentUser());
        }
        return false;
    }

    /**
     * 是否有邀请权限
     * @return
     */
    public static boolean isCanInvite(EMGroup group) {
        return group != null && (group.isMemberAllowToInvite() || isOwner(group) || isAdmin(group));
    }

    /**
     * 在黑名单中
     * @param username
     * @return
     */
    public static boolean isInAdminList(String username, List<String> adminList) {
        return isInList(username, adminList);
    }

    /**
     * 在黑名单中
     * @param username
     * @return
     */
    public static boolean isInBlackList(String username, List<String> blackMembers) {
        return isInList(username, blackMembers);
    }

    /**
     * 在禁言名单中
     * @param username
     * @return
     */
    public static boolean isInMuteList(String username, List<String> muteMembers) {
        return isInList(username, muteMembers);
    }

    /**
     * 是否在列表中
     * @param name
     * @return
     */
    public static boolean isInList(String name, List<String> list) {
        if(list == null) {
            return false;
        }
        synchronized (GroupHelper.class) {
            for (String item : list) {
                if (TextUtils.equals(name, item)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取群名称
     * @param groupId
     * @return
     */
    public static String getGroupName(String groupId) {
        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
        if(group == null) {
            return groupId;
        }
        return TextUtils.isEmpty(group.getGroupName()) ? groupId : group.getGroupName();
    }



    /***
     *获取用户信息
     * @param userList
     */
    public static void getUserInfo(List<String> userList, ContactListAdapter adapter) {


        if (!userList.isEmpty()) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userNames", userList);
            params.put("requestType", Constant.REQEUSR_TYPE_list);
            List<EaseUser> easeUsers = new ArrayList<EaseUser>();
            List<String> finalAdminList = userList;
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

}
