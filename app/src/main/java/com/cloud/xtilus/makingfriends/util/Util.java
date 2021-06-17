package com.cloud.xtilus.makingfriends.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;


import com.cloud.xtilus.makingfriends.R;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by dgy on 15/7/10.
 * 包含一些辅助方法
 */
public class Util {

    /**
     * @function 将国家码和手机号拼接成86-15112345678的形式
     * @param countryCode 国家码
     * @param phoneNumber 手机号
     * @return 返回拼接后的字符串
     * */
    public static String getWellFormatMobile(String countryCode, String phoneNumber) {
        return countryCode + "-" + phoneNumber;
    }

    /**
     * @function 判断手机号是否有效
     * @param phoneNumber 手机号
     * @return 有效则返回true, 无效则返回false
     * */
    public static boolean validPhoneNumber(String countryCode, String phoneNumber) {
        if (countryCode.equals("86"))
            return phoneNumber.length() == 11 && phoneNumber.matches("[0-9]{1,}");
        else
            return phoneNumber.matches("[0-9]{1,}");
    }

    /**
     * @function 在按钮上启动一个定时器
     * @param button 按钮控件
     * @param defaultString 按钮上默认的字符串
     * @param tmpString 计时时显示的字符串
     * @param max 失效时间（单位：s）
     * @param interval 更新间隔（单位：s）
     * */
    public static void startTimer(Context context,
                                  final Button button,
                                  String defaultString,
                                  String tmpString,
                                  int max,
                                  int interval)
    {
        CountDownButtonHelper timer = new CountDownButtonHelper(button,
                defaultString, tmpString, max, interval);

        final int textColor = button.getCurrentTextColor();

        timer.setOnFinishListener(new CountDownButtonHelper.OnFinishListener(){
            @Override
            public void finish() {
                button.setTextColor(textColor);
            }
        });
        button.setTextColor(context.getResources().getColor(R.color.resend_btn_textcolor));
        timer.start();
    }

    /**
     * @function: 对屏幕中间显示一个Toast，其内容为msg
     * */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @function: 显示使用TLSSDK过程中发生的错误信息
     * */


    /**
     * @function: 判断用户名是否有效，有效的用户名的长度为4-24个字符，包含字母、数字、下划线，
     * 不能为纯数字
     * @param username 用户名
     * */
    public static boolean validUsername(Context context, String username) {
        int len = username.length();
        if (len == 0) {
            showToast(context, "用户名不能为空");
            return false;
        }
        if (len < 4 || len > 24) {
            showToast(context, "用户名的长度必须在4-24个字符之间");
            return false;
        }

        int c_num = 0;
        for (int i = 0; i < len; ++i) {
            char c = username.charAt(i);
            if (Character.isDigit(c)) {
                ++c_num;
            } else if (!Character.isLetter(c) && c != '_')  {
                showToast(context, "用户名只能包含字母、数字、下划线");
                return false;
            }
        }

        if (c_num < len) {
            return true;
        } else {
            showToast(context, "用户名不能为纯数字");
            return false;
        }
    }

    /**
     * @function: 判断密码是否有效，有效的密码的长度为8-16个字符
     * */
    public static boolean validPassword(Context context, String password) {
        int len = password.length();

        if (len == 0) {
            showToast(context, "密码不能为空");
            return false;
        }

        if ( (len >= 8) && (len <= 16) ) {
            return true;
        } else {
            showToast(context, "密码的长度必须在8-16个字符之间");
            return false;
        }
    }



    public static String getDuration(Context context, String rel_time, String now_time) {

        if (TextUtils.isEmpty(now_time)) {
            if (!TextUtils.isEmpty(rel_time)) {
                String showTime = rel_time.substring(0, rel_time.lastIndexOf(":"));

                return showTime;
            }

            return "时间错误";
        }

        String backStr = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(rel_time);
            d2 = format.parse(now_time);

            // 毫秒ms
            long diff = d2.getTime() - d1.getTime();

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays != 0) {
                if (diffDays < 30) {
                    if (1 < diffDays && diffDays < 2) {
                        backStr = context.getString(R.string.yesterday);
                    } else if (1 < diffDays && diffDays < 2) {
                        backStr = context.getString(R.string.The_day_before_yesterday);
                    } else {
                        backStr = String.valueOf(diffDays) + context.getString(R.string.Days_ago);
                    }
                } else {
                    backStr = context.getString(R.string.long_long_ago);
                }

            } else if (diffHours != 0) {
                backStr = String.valueOf(diffHours) + context.getString(R.string.An_hour_ago);

            } else if (diffMinutes != 0) {
                backStr = String.valueOf(diffMinutes) + context.getString(R.string.minutes_ago);

            } else {

                backStr = context.getString(R.string.just);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return backStr;

    }
}
