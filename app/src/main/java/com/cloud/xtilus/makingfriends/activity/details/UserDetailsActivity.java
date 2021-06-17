package com.cloud.xtilus.makingfriends.activity.details;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.section.base.BaseActivity;


/**
 * Created by huangfangyi on 2016/7/6.\
 * QQ:84543217
 */
public class UserDetailsActivity extends BaseActivity {
    /**
     *
     * 用户详情页接收两种传值
     * 1：用户完整资料的JSON字符串-userInfo-这种情况如果是好友进行刷新
     * 2：只传用户的hxid，这种情况直接从网络取数据显示-如果是好友，刷新资料
     *
     */

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_base);
        setTitle(R.string.Detailed_information);
        UserDetailesFragment fragment = (UserDetailesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null){
            fragment = new UserDetailesFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame,fragment);
            transaction.commit();
        }
        new UserDetailsPrester(fragment);
    }
}