package com.cloud.xtilus.makingfriends.router;

import android.content.Context;

import com.cloud.xtilus.makingfriends.activity.AboutSettingActivity;
import com.cloud.xtilus.makingfriends.activity.LogingActivity;
import com.cloud.xtilus.makingfriends.activity.MapActivity;
import com.cloud.xtilus.makingfriends.activity.MeInfoActivity;
import com.cloud.xtilus.makingfriends.activity.RegisterActivity;
import com.cloud.xtilus.makingfriends.activity.SettingActivity;
import com.vmloft.develop.library.tools.router.VMRouter;

/**
 * Create by lzan13 on 2019/04/09
 *
 * 项目路由
 */
public class ARouter extends VMRouter {

    /**
     * 跳转到主界面
     */
    public static void goMain(Context context) {
        //forward(context, MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    /**
     * 跳转到引导页
     */
/*
    public static void goGuide(Context context) {
        forward(context, GuideActivity.class);
    }
*/

    /**
     * 跳转到登录界面
     */
    public static void goSignIn(Context context) {
        forward(context, LogingActivity.class);
    }

    /**
     * 跳转到注册界面
     */
    public static void goSignUp(Context context) {
        overlay(context, RegisterActivity.class);
    }

    /**
     * 个人信息
     */
    public static void goMeInfo(Context context) {
        overlay(context, MeInfoActivity.class);
    }

    /***
     *
     * 好友信息
     */

    public  static void getFriends(Context context){


       // overlay(context,ContactListActivity.class);
    }



    /**
     * 用户信息
     */
   /* public static void goUserDetail(Context context, String id) {
        VMParams params = new VMParams();
        params.str0 = id;
        overlay(context, UserDetailActivity.class, params);
    }*/

    /**
     * 去匹配
     */
/*
    public static void goMatch(Context context, VMParams params) {
        overlay(context, MatchActivity.class, params);
    }
*/

    /**
     * 设置界面
     */
    public static void goSetting(Context context) {
        overlay(context, SettingActivity.class);
    }

    /**
     * 设置隐私
     */
    public static void goSettingPrivacy(Context context) {
        //overlay(context, PrivacyActivity.class);

        //overlay(context, PicturesActivity.class);
    }

    /**
     * 设置通用
     */

    public static void goSettingGeneral(Context context) {

       // overlay(context, GeneralActivity.class);
     /*   String url="https://youku.cdn1-kubozy.com/share/18fc72d8b8aba03a4d84f66efabce82e";
        TbsVideo.openVideo(context,url,null);
        overlay(context, VideoActivity.class);*/

    }


    /**
     * 设置帮助与反馈
     */

    public static void goSettingHelp(Context context) {


        overlay(context, MapActivity.class);
    }


    /**
     * 设置通知界面
     */

    public static void goNotifySetting(Context context) {
        //overlay(context, NewNotifyActivity.class);
    }


    /**
     * 设置关于界面
     */
/*    public static void goChatSetting(Context context) {
        overlay(context, ChatSettingActivity.class);
    }*/

    /**
     * 设置关于界面
     */
    public static void goAboutSetting(Context context) {
        overlay(context, AboutSettingActivity.class);
    }
}
