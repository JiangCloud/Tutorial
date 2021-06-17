package com.cloud.xtilus.makingfriends.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.activity.im.MainActivity;
import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.util.CommonHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.jpush.android.api.JPushInterface;

public class WelcomeActivity extends Activity {

    @ViewInject(R.id.welcome_version)
    private TextView  TextViewVerison;

    @ViewInject(R.id.progLoadProgressBar)
    private ProgressBar progressBar;
    private String version;
    private Context context=WelcomeActivity.this;
    private Intent intent;
    private  StringBuilder mStrRetMsg;
    private StringBuilder strUserSig;
    private String mStrUserName,mStrPassWord;
    private Handler mHandler;
    private MakingFriendApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ViewUtils.inject(this);
        initView();
        checkNetStatus();
    }

    private void initView(){
     //   application = (MakingFriendApplication) this.getApplicationContext();
        application= MakingFriendApplication. getInstance();
        try {
             version= getClientVersion();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TextViewVerison.setText(version);
        strUserSig=new StringBuilder();
        mStrRetMsg=new StringBuilder();
    }

    /***
     * 检查网络
     */
    private void  checkNetStatus(){
        if(!CommonHelper.GetNetWorkStatus(getBaseContext())) {
            Toast.makeText(context, "网络不可用，请检查网络设置!", Toast.LENGTH_LONG).show();
            intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            this.finish();
        }else{
            mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    login();

                }
            }, 2000);


        }
    }

    /***
     * 登陆
     */
    private  void login(){

        intent = new Intent();
        intent.setClass(context, LogingActivity.class);
        startActivity(intent);
        WelcomeActivity.this.finish();
            //  mStrUserName= (String) SharedPreferencesHelper.getInstance().getData("username",Constant.DEFAULT_USERNAME);
            //  mStrPassWord= (String) SharedPreferencesHelper.getInstance().getData("password",Constant.DEFAULT_PASSWORD);
           /* if (!mStrUserName.equals(Constant.DEFAULT_USERNAME) && !mStrPassWord.equals(Constant.DEFAULT_PASSWORD)) {
                new  Thread(new Runnable() {
                    @Override
                    public void run() {
                   final int iRet = HttpProcessor.doRequestLogin(mStrUserName, mStrPassWord, mStrRetMsg, strUserSig);
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             if(iRet == 0){
                                 application.setUserSig(strUserSig.toString());
                                 application.setIdentifier(mStrUserName);
                                 application.setIsLoging(true);
                                 intent = new Intent();
                                 intent.setClass(context, MainActivity.class);
                                 startActivity(intent);
                                 WelcomeActivity.this.finish();

                             }else{
                                 Toast.makeText(context, "自动登陆失败，请尝试手动登陆", Toast.LENGTH_SHORT).show();
                                 intent = new Intent(context, LogingActivity.class);
                                 startActivity(intent);
                                 WelcomeActivity.this.finish();

                             }

                         }
                     });

                    }
                }).start();

            } else {
                *//*    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);// 如果在ui线程,会阻塞ui线程
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            WelcomeActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Intent  intent=new Intent(context,MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }).start();*//*
                intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                this.finish();

            }
*/





    }




    private String getClientVersion() throws PackageManager.NameNotFoundException {
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packageInfo.versionName;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
}
