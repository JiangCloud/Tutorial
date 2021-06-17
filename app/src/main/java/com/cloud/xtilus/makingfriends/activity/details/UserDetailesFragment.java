package com.cloud.xtilus.makingfriends.activity.details;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.activity.moments.MomentsFriendActivity;
import com.cloud.xtilus.makingfriends.activity.showbigimage.ShowBigImageActivity;
import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.section.chat.activity.ChatActivity;
import com.cloud.xtilus.makingfriends.util.Constant;


//import com.htmessage.fanxinht.acitivity.addfriends.add.end.AddFriendsFinalActivity;
//import com.htmessage.fanxinht.acitivity.chat.ChatActivity;



/**
 * 项目名称：yichat0504
 * 类描述：UserDetailesFragment 描述:
 * 创建人：songlijie
 * 创建时间：2017/7/10 11:52
 * 邮箱:814326663@qq.com
 */
public class UserDetailesFragment extends Fragment implements UserDetailsView ,View.OnClickListener{
    private UserDetailsPrester prester;
    private Button btnMsg,btnAdd;
    private ImageView iv_avatar,iv_sex;
    private TextView tv_name,tv_yichat_number,tv_mobile,tv_region,tv_mixin;
    private RelativeLayout rl_region,rl_yichat_id,re_mobile,re_moments;
    private Dialog dialog;
    private JSONObject userJson;
    private String avatarUrl;
    private String yichatId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = MakingFriendApplication.getInstance().createLoadingDialog(getBaseActivity(),getString(R.string.now_refresh_msg));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View infoView = inflater.inflate(R.layout.activity_userinfo, container, false);
        initView(infoView);
        getData();
        setLstenter();
        return infoView;
    }

    private void getData() {
        if (!TextUtils.isEmpty(getFxid())){
            prester.refreshInfo(getFxid(),true);
            return;
        }
        if (getUserJson() == null){
            getBaseActivity().finish();
            return;
        }
        showUi(getUserJson());
        if (prester.isFriend(getUserJson().getString(Constant.JSON_KEY_HXID))){
            prester.refreshInfo(getUserJson().getString(Constant.JSON_KEY_FXID),false);
        }else{
            prester.refreshInfo(getUserJson().getString(Constant.JSON_KEY_FXID),true);
        }
    }

    private void setLstenter() {
        btnAdd.setOnClickListener(this);
        btnMsg.setOnClickListener(this);
        re_moments.setOnClickListener(this);
        iv_avatar.setOnClickListener(this);
    }

    private void initView(View infoView) {
        btnMsg = (Button) infoView.findViewById(R.id.btn_msg);
        btnAdd= (Button) infoView.findViewById(R.id.btn_add);
        iv_avatar = (ImageView) infoView.findViewById(R.id.iv_avatar);
        iv_sex = (ImageView) infoView.findViewById(R.id.iv_sex);
        tv_name = (TextView) infoView.findViewById(R.id.tv_name);
        tv_yichat_number = (TextView) infoView.findViewById(R.id.tv_yichat_number);
        tv_mobile = (TextView) infoView.findViewById(R.id.tv_mobile);
        tv_region = (TextView) infoView.findViewById(R.id.tv_region);
        tv_mixin = (TextView) infoView.findViewById(R.id.tv_mixin);
        rl_region = (RelativeLayout) infoView.findViewById(R.id.rl_region);
        rl_yichat_id = (RelativeLayout) infoView.findViewById(R.id.rl_yichat_id);
        re_mobile = (RelativeLayout) infoView.findViewById(R.id.re_mobile);
        re_moments= (RelativeLayout) infoView.findViewById(R.id.re_moments);
    }

    @Override
    public void onRefreshSuccess(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefreshFailed(String error) {
        Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getHxid() {

        return getBaseActivity().getIntent().getStringExtra(Constant.JSON_KEY_HXID);


    }


    public String getFxid() {

        return getBaseActivity().getIntent().getStringExtra(Constant.JSON_KEY_FXID);


    }


    @Override
    public JSONObject getUserJson() {
        String extra = getBaseActivity().getIntent().getStringExtra(Constant.KEY_USER_INFO);
        JSONObject object = JSONObject.parseObject(extra);
        return object;
    }

    @Override
    public void showDialog() {
        if (dialog!=null){
            dialog.show();
        }
    }

    @Override
    public void hintDialog() {
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    @Override
    public void showUi(JSONObject object) {
        this.userJson = object;
        String hxid = object.getString(Constant.JSON_KEY_HXID);
        yichatId = object.getString(Constant.JSON_KEY_FXID);
        String Tel = object.getString(Constant.JSON_KEY_TEL);
        String province = object.getString(Constant.JSON_KEY_PROVINCE);
        String city = object.getString(Constant.JSON_KEY_CITY);
        String sign = object.getString(Constant.JSON_KEY_SIGN);
        if (!TextUtils.isEmpty(yichatId)){
            tv_mixin.setText(getString(R.string.mixin_number)+yichatId);
        }else{
            tv_mixin.setText(getString(R.string.mixin_number)+getString(R.string.not_set));
        }
        if (!TextUtils.isEmpty(sign)) {
            tv_yichat_number.setText(sign);
        } else {
            tv_yichat_number.setText(R.string.not_set);
        }

        if (!TextUtils.isEmpty(Tel)){
            tv_mobile.setText(Tel);
        }else{
            tv_mobile.setText(R.string.not_set);
        }
        if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city)){
            tv_region.setText(province+" "+city);
            if (province.equals(city)) {
                tv_region.setText(city);
            }
        }else{
            tv_region.setText(R.string.not_set);
        }
        String nick = object.getString(Constant.JSON_KEY_NICK);
        avatarUrl =object.getString(Constant.JSON_KEY_AVATAR);

        if(!TextUtils.isEmpty(avatarUrl)){
            if (!avatarUrl.contains("http:")){
                avatarUrl = Constant.URL_AVATAR+avatarUrl;
            }
        }
        Glide.with(getContext()).load(avatarUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.default_avatar).into(iv_avatar);
        tv_name.setText(nick);
        String sex = object.getString(Constant.JSON_KEY_SEX);
        if (!TextUtils.isEmpty(sex)){
            if ("1".equals(sex) || sex.equals(getString(R.string.male))) {
                iv_sex.setImageResource(R.mipmap.icon_male);
            } else if ("0".equals(sex) || sex.equals(getString(R.string.female))){
                iv_sex.setImageResource(R.mipmap.icon_female);
            }
        }else{
            iv_sex.setImageResource(R.mipmap.icon_male);
        }

        //资料是自己
        if (prester.isMe(hxid)) {
            btnMsg.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
            return;
        }
        //不是好友的
        if (!prester.isFriend(hxid)) {
            //btnMsg.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
            return;
        }
    }

    @Override
    public void setPresenter(UserDetailsPrester presenter) {
        this.prester = presenter;
    }

    @Override
    public Context getBaseContext() {
        return getContext();
    }

    @Override
    public Activity getBaseActivity() {
        return getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                //  startActivity(new Intent(getBaseActivity(), AddFriendsFinalActivity.class).putExtra(Constant.KEY_USER_INFO, userJson.toJSONString()));
                break;
            case R.id.btn_msg:
                Intent intentChat = new Intent(getBaseActivity(), ChatActivity.class);

                if (!TextUtils.isEmpty(yichatId)){
                    intentChat.putExtra("userId", yichatId);
                    intentChat.putExtra("chatType", 1);//单人聊天
                    startActivity(intentChat);
                }

                // startActivity(new Intent(getBaseActivity(), ChatRoomActivity.class).putExtra("userId","cloud"));

                //46201835683841
                //startActivity(new Intent(getBaseActivity(), ChatActivity.class).putExtra("userId",userJson.getString(Constant.JSON_KEY_HXID)));
                break;
            case R.id.re_moments:
                Intent intent = new Intent(getBaseActivity(), MomentsFriendActivity.class);
                if (userJson != null){

                    intent.putExtra("userId", userJson.getString(Constant.JSON_KEY_HXID));
                    intent.putExtra("userNick", userJson.getString(Constant.JSON_KEY_NICK));
                    intent.putExtra("avatar", userJson.getString(Constant.JSON_KEY_AVATAR));
                }
                startActivity(intent);
                break;
            case R.id.iv_avatar:
                startActivity(new Intent(getActivity(), ShowBigImageActivity.class).putExtra("localPath", avatarUrl));
                break;
        }
    }

    @Override
    public void onDestroy() {
        prester.onDestory();
        super.onDestroy();
    }
}
