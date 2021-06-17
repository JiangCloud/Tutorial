package com.cloud.xtilus.makingfriends.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.application.DemoHelper;
import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.common.widget.ArrowItemView;
import com.cloud.xtilus.makingfriends.section.base.BaseInitActivity;
import com.cloud.xtilus.makingfriends.section.login.activity.LoginActivity;
import com.cloud.xtilus.makingfriends.section.me.activity.AccountSecurityActivity;
import com.cloud.xtilus.makingfriends.section.me.activity.CommonSettingsActivity;
import com.cloud.xtilus.makingfriends.section.me.activity.MessageReceiveSetActivity;
import com.cloud.xtilus.makingfriends.section.me.activity.PrivacyIndexActivity;
import com.cloud.xtilus.makingfriends.util.IMDialog;
import com.hyphenate.EMCallBack;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.vmloft.develop.library.tools.utils.VMStr;


/**
 * Create by lzan13 on 2019/05/14
 * <p>
 * 设置界面
 */
public class SettingActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener, View.OnClickListener {


    private MakingFriendApplication application;
    private Button btnLogout;
    private ArrowItemView meInfo;
    private ArrowItemView notify;
    private ArrowItemView about;
    private ArrowItemView general;
    private ArrowItemView help;
    private ArrowItemView privacy;
    private EaseTitleBar titleBar;

    private ArrowItemView security;
    private Intent intent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        meInfo = findViewById(R.id.setting_me_info);
        notify = findViewById(R.id.setting_notify);
        about = findViewById(R.id.setting_about);
        general = findViewById(R.id.setting_general);
        help = findViewById(R.id.setting_help);
        privacy = findViewById(R.id.setting_privacy);
        btnLogout = findViewById(R.id.btn_logout);
        security = findViewById(R.id.setting_security);

    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        meInfo.setOnClickListener(this);
        notify.setOnClickListener(this);
        about.setOnClickListener(this);
        general.setOnClickListener(this);
        security.setOnClickListener(this);
        help.setOnClickListener(this);
        privacy.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }


    @Override
    protected void initData() {

        application = MakingFriendApplication.getInstance();
    }

    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.setting_me_info:
                intent = new Intent(this, MeInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_notify:
                MessageReceiveSetActivity.actionStart(this);
                break;
            case R.id.setting_security:
                AccountSecurityActivity.actionStart(this);//账号与安全
                break;

            case R.id.setting_privacy:

                PrivacyIndexActivity.actionStart(this);//隐私
                break;

            case R.id.setting_general:
                CommonSettingsActivity.actionStart(this);//通用
                break;

            case R.id.setting_help:
                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                //ARouter.goSettingHelp(mActivity);
                break;
            case R.id.setting_about:
                intent = new Intent(this, AboutSettingActivity.class);
                startActivity(intent);
                //ARouter.goAboutSetting(mActivity);
                break;
            case R.id.btn_logout:
                signOut();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void signOut() {
        String title = VMStr.byRes(R.string.sign_out_hint_title);
        String content = VMStr.byRes(R.string.sign_out_hint_content);
        String cancel = VMStr.byRes(R.string.im_cancel);
        String ok = VMStr.byRes(R.string.im_ok);
        IMDialog.showAlertDialog(this, title, content, cancel, ok, (DialogInterface dialog, int which) -> {

            // loginOutBtn.setVisibility(View.INVISIBLE);//隐藏登陆按钮
            application.setIsLoging(false);//标记没登陆
            btnLogout.setVisibility(View.INVISIBLE);   //隐藏登陆按钮
            logout();


          /* DemoHelper.getInstance().logout(false,null);
           // EMClient.getInstance().logout(true);
            Intent intent=new Intent(this, LogingActivity.class);
            //ASignManager.getInstance().signOut();
            startActivity(intent);*/
        });
    }


    void logout() {

/*
        final ProgressDialog pd = new ProgressDialog(getBaseContext());
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();*/
        DemoHelper.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                // show login screen
                finishOtherActivities();
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                finish();
            }



            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {


                Toast.makeText(getBaseContext(), "unbind devicetokens failed", Toast.LENGTH_SHORT).show();


            }
        });
    }
}