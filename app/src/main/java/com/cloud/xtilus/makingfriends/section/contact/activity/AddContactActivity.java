package com.cloud.xtilus.makingfriends.section.contact.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.bean.ResponseBean;
import com.cloud.xtilus.makingfriends.cache.UserWebInfo;
import com.cloud.xtilus.makingfriends.common.db.DemoDbHelper;
import com.cloud.xtilus.makingfriends.common.enums.SearchType;
import com.cloud.xtilus.makingfriends.common.interfaceOrImplement.OnResourceParseCallback;
import com.cloud.xtilus.makingfriends.http.BaseCallback;
import com.cloud.xtilus.makingfriends.http.OkHttpHelper;
import com.cloud.xtilus.makingfriends.section.contact.adapter.AddContactAdapter;
import com.cloud.xtilus.makingfriends.section.contact.viewmodels.AddContactViewModel;
import com.cloud.xtilus.makingfriends.section.search.SearchActivity;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.hyphenate.easeui.adapter.EaseBaseRecyclerViewAdapter;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.vmloft.develop.library.tools.widget.toast.VMToast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class AddContactActivity extends SearchActivity implements EaseTitleBar.OnBackPressListener, AddContactAdapter.OnItemAddClickListener {
    private AddContactViewModel mViewModel;
    private SearchType mType;
    public static void startAction(Context context, SearchType type) {
        Intent intent = new Intent(context, AddContactActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        mType = (SearchType) getIntent().getSerializableExtra("type");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar.setTitle(getString(R.string.em_search_add_contact));
        query.setHint(getString(R.string.em_search_add_contact_hint));
    }

    @Override
    protected void initData() {
        super.initData();
        mViewModel = new ViewModelProvider(mContext).get(AddContactViewModel.class);
        mViewModel.getAddContact().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    showToast(getResources().getString(R.string.em_add_contact_send_successful));
                }
            });

        });
        //获取本地的好友列表
        List<String> localUsers = null;
        if(DemoDbHelper.getInstance(mContext).getUserDao() != null) {
            localUsers = DemoDbHelper.getInstance(mContext).getUserDao().loadAllUsers();
        }
        ((AddContactAdapter)adapter).addLocalContacts(localUsers);

        ((AddContactAdapter)adapter).setOnItemAddClickListener(this);
    }

    @Override
    protected EaseBaseRecyclerViewAdapter getAdapter() {
        return new AddContactAdapter();
    }

    @Override
    public void searchMessages(String query) {


        // you can search the user from your app server here.
        if(!TextUtils.isEmpty(query)) {
            if (adapter.getData() != null && !adapter.getData().isEmpty()) {
                adapter.clearData();
            }

            Map<String,Object> params = new HashMap<String,Object>();
            params.put("queryString", query);

            OkHttpHelper.getInstance().post(Constant.URL_GET_UserInfo, params, new BaseCallback<ResponseBean<UserWebInfo>>(){

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
                        public void onSuccess(Response response, ResponseBean<UserWebInfo> listResponseBean) {

                                if (listResponseBean.getRet_code()==200) {


                                    UserWebInfo  users = listResponseBean.getRet_data();
                                    adapter.addData(users);
                                }


                            }

                        @Override
                        public void onError(Response response, int code, Exception e) {
                            VMToast.make(getParent(),R.string.select_user_info_fail);

                        }
                    });




        }
    }

    @Override
    protected void onChildItemClick(View view, int position) {
        //跳转到好友页面

        UserWebInfo userWebInfo= (UserWebInfo)adapter.getItem(position);
       // String item = (String) adapter.getItem(position);
        EaseUser user = new EaseUser(userWebInfo.getFxid());
        user.setNickname(userWebInfo.getNick());
        user.setAvatar(userWebInfo.getAvatar());
        ContactDetailActivity.actionStart(mContext, user, false);
    }

    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    @Override
    public void onItemAddClick(View view, int position) {
        // 添加好友

        UserWebInfo userWebInfo= (UserWebInfo)adapter.getItem(position) ;
        mViewModel.addContact(userWebInfo.getFxid(), getResources().getString(R.string.em_add_contact_add_a_friend));
    }
}
