package com.cloud.xtilus.makingfriends.common.repositories;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cloud.xtilus.makingfriends.application.DemoHelper;
import com.cloud.xtilus.makingfriends.bean.ResponseBean;
import com.cloud.xtilus.makingfriends.cache.UserWebInfo;
import com.cloud.xtilus.makingfriends.common.db.entity.EmUserEntity;
import com.cloud.xtilus.makingfriends.common.interfaceOrImplement.ResultCallBack;
import com.cloud.xtilus.makingfriends.common.net.ErrorCode;
import com.cloud.xtilus.makingfriends.common.net.Resource;
import com.cloud.xtilus.makingfriends.http.BaseCallback;
import com.cloud.xtilus.makingfriends.http.OkHttpHelper;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.manager.EaseThreadManager;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class EMContactManagerRepository extends BaseEMRepository{

    public LiveData<Resource<Boolean>> addContact(String username, String reason) {
        return new NetworkOnlyResource<Boolean>() {

            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<Boolean>> callBack) {
                if(getCurrentUser().equalsIgnoreCase(username)) {
                    callBack.onError(ErrorCode.EM_ADD_SELF_ERROR);
                    return;
                }
                List<String> users = null;
                if(getUserDao() != null) {
                    users = getUserDao().loadAllUsers();
                }
                if(users != null && users.contains(username)) {
                    if(getContactManager().getBlackListUsernames().contains(username)) {
                        callBack.onError(ErrorCode.EM_FRIEND_BLACK_ERROR);
                        return;
                    }
                    callBack.onError(ErrorCode.EM_FRIEND_ERROR);
                    return;
                }
                getContactManager().aysncAddContact(username, reason, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        callBack.onSuccess(new MutableLiveData<>(true));
                    }

                    @Override
                    public void onError(int code, String error) {
                        callBack.onError(code, error);
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
            }

        }.asLiveData();
    }

    public LiveData<Resource<List<EaseUser>>> getContactList() {
        return new NetworkBoundResource<List<EaseUser>, List<EaseUser>>() {

            @Override
            protected boolean shouldFetch(List<EaseUser> data) {
                return true;
            }

            @Override
            protected LiveData<List<EaseUser>> loadFromDb() {
                return getUserDao().loadUsers();
            }

            @Override
            protected void createCall(ResultCallBack<LiveData<List<EaseUser>>> callBack) {
                if(!isLoggedIn()) {
                    callBack.onError(ErrorCode.EM_NOT_LOGIN);
                    return;
                }
                runOnIOThread(()-> {
                    try {
                        List<String> usernames = getContactManager().getAllContactsFromServer();
                        List<String> ids = getContactManager().getSelfIdsOnOtherPlatform();
                        if(usernames == null) {
                            usernames = new ArrayList<>();
                        }
                        if(ids != null && !ids.isEmpty()) {
                            usernames.addAll(ids);
                        }

                        Map<String,Object> params = new HashMap<String,Object>();
                        params.put("userNames", usernames);
                        params.put("requestType", Constant.REQEUSR_TYPE_list);
                        List<String> finalUsernames = usernames;
                        List<EaseUser> easeUsers = new ArrayList<EaseUser>();
                        List<String> blackListFromServer = null;
                        blackListFromServer = getContactManager().getBlackListFromServer();
                        List<String> finalBlackListFromServer = blackListFromServer;
                        OkHttpHelper.getInstance().post(Constant.URL_FIND_UserInfos, params, new BaseCallback<ResponseBean<List<UserWebInfo>>>(){
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
                                        if (listResponseBean.getRet_code()==200) {
                                            List<UserWebInfo> users = listResponseBean.getRet_data();

                                            for (int i = 0; i < users.size(); i++) {
                                                EaseUser user = new EaseUser(finalUsernames.get(i));
                                                user.setNickname(users.get(i).getNick());
                                                user.setAvatar(users.get(i).getAvatar());
                                                EMLog.d("Nick","======"+users.get(i).getNick());
                                                EMLog.d("Avatar","======"+users.get(i).getAvatar());
                                                EMLog.d("finalUsernames","======"+finalUsernames.get(i));

                                               // EaseCommonUtils.setUserInitialLetter(user);
                                                easeUsers.add(user);

                                            }
                                            // save the contact list to cache
                                            //getContactList().clear();
                                           // getContactList().putAll(userlist);
                                            // save the contact list to database
                                            //UserDao dao = new UserDao(appContext);
                                            EMLog.d("easeUsers","======"+easeUsers.size());
                                          //  dao.saveContactList(easeUsers);
                                           // demoModel.setContactSynced(true);
                                            // EMLog.d(TAG, "set contact syn status to true");
                                            if(easeUsers != null && !easeUsers.isEmpty()) {


                                                    for (EaseUser user : easeUsers) {
                                                        if(finalBlackListFromServer != null && !finalBlackListFromServer.isEmpty()) {
                                                            if(finalBlackListFromServer.contains(user.getUsername())) {
                                                                user.setContact(1);
                                                            }
                                                        }
                                                    }
                                                    sortData(easeUsers);
                                                    callBack.onSuccess(createLiveData(easeUsers));


                                          }

                                        }

                                    }

                                    @Override
                                    public void onError(Response response, int code, Exception e) {

                                    }
                                });


                      //  List<EaseUser> easeUsers = EmUserEntity.parse(usernames);


                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        callBack.onError(e.getErrorCode(), e.getDescription());
                    }
                });
            }

            @Override
            protected void saveCallResult(List<EaseUser> items) {
                if(getUserDao() != null) {
                    getUserDao().clearUsers();
                    getUserDao().insert(EmUserEntity.parseList(items));
                }
            }

        }.asLiveData();
    }

    private void sortData(List<EaseUser> data) {
        if(data == null || data.isEmpty()) {
            return;
        }
        Collections.sort(data, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if(lhs.getInitialLetter().equals(rhs.getInitialLetter())){
                    return lhs.getNickname().compareTo(rhs.getNickname());
                }else{
                    if("#".equals(lhs.getInitialLetter())){
                        return 1;
                    }else if("#".equals(rhs.getInitialLetter())){
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }

            }
        });
    }

    /**
     * 获取黑名单
     * @return
     */
    public LiveData<Resource<List<EaseUser>>> getBlackContactList() {
        return new NetworkBoundResource<List<EaseUser>, List<EaseUser>>() {
            @Override
            protected boolean shouldFetch(List<EaseUser> data) {
                return true;
            }

            @Override
            protected LiveData<List<EaseUser>> loadFromDb() {
                return getUserDao().loadBlackUsers();
            }

            @Override
            protected void createCall(ResultCallBack<LiveData<List<EaseUser>>> callBack) {
                if(!isLoggedIn()) {
                    callBack.onError(ErrorCode.EM_NOT_LOGIN);
                    return;
                }
                getContactManager().aysncGetBlackListFromServer(new EMValueCallBack<List<String>>() {
                    @Override
                    public void onSuccess(List<String> value) {
                        List<EaseUser> users = EmUserEntity.parse(value);
                        if(users != null && !users.isEmpty()) {
                            for (EaseUser user : users) {
                                user.setContact(1);
                            }
                        }
                        callBack.onSuccess(createLiveData(users));
                    }

                    @Override
                    public void onError(int error, String errorMsg) {
                        callBack.onError(error, errorMsg);
                    }
                });
            }

            @Override
            protected void saveCallResult(List<EaseUser> items) {
                if(getUserDao() != null) {
                    getUserDao().clearBlackUsers();
                    getUserDao().insert(EmUserEntity.parseList(items));
                }
            }

        }.asLiveData();
    }

    /**
     * 删除联系人
     * @param username
     * @return
     */
    public LiveData<Resource<Boolean>> deleteContact(String username) {
        return new NetworkOnlyResource<Boolean>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<Boolean>> callBack) {
                DemoHelper.getInstance().getModel().deleteUsername(username, true);
                getContactManager().aysncDeleteContact(username, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        DemoHelper.getInstance().deleteContact(username);
                        callBack.onSuccess(createLiveData(true));
                    }

                    @Override
                    public void onError(int code, String error) {
                        callBack.onError(code, error);
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
            }
        }.asLiveData();
    }

    /**
     * 添加到黑名单
     * @param username
     * @param both 把用户加入黑民单时，如果是both双方发消息时对方都收不到；如果不是，
     *             则我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
     * @return
     */
    public LiveData<Resource<Boolean>> addUserToBlackList(String username, boolean both) {
        return new NetworkOnlyResource<Boolean>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<Boolean>> callBack) {
                getContactManager().aysncAddUserToBlackList(username, both, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        callBack.onSuccess(createLiveData(true));
                    }

                    @Override
                    public void onError(int code, String error) {
                        callBack.onError(code, error);
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
            }
        }.asLiveData();
    }

    /**
     * 移出黑名单
     * @param username
     * @return
     */
    public LiveData<Resource<Boolean>> removeUserFromBlackList(String username) {
        return new NetworkOnlyResource<Boolean>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<Boolean>> callBack) {
                getContactManager().aysncRemoveUserFromBlackList(username, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        callBack.onSuccess(createLiveData(true));
                    }

                    @Override
                    public void onError(int code, String error) {
                        callBack.onError(code, error);
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<EaseUser>>> getSearchContacts(String keyword) {
        return new NetworkOnlyResource<List<EaseUser>>() {
            @Override
            protected void createCall(@NonNull ResultCallBack<LiveData<List<EaseUser>>> callBack) {
                EaseThreadManager.getInstance().runOnIOThread(()-> {
                    List<EaseUser> easeUsers = null;
                    if(getUserDao() != null) {
                        easeUsers = getUserDao().loadContacts();
                    }
                    List<EaseUser> list = new ArrayList<>();
                    if(easeUsers != null && !easeUsers.isEmpty()) {
                        for (EaseUser user : easeUsers) {
                            if(user.getUsername().contains(keyword) || (!TextUtils.isEmpty(user.getNickname()) && user.getNickname().contains(keyword))) {
                                list.add(user);
                            }
                        }
                    }
                    callBack.onSuccess(createLiveData(list));
                });

            }
        }.asLiveData();
    }
}
