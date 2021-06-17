package com.cloud.xtilus.makingfriends.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.activity.SettingActivity;
import com.cloud.xtilus.makingfriends.activity.moments.MomentsActivity;
import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.bean.User;
import com.cloud.xtilus.makingfriends.cache.UserCacheInfo;
import com.cloud.xtilus.makingfriends.cache.UserCacheManager;
import com.cloud.xtilus.makingfriends.im.AIMManager;
import com.cloud.xtilus.makingfriends.router.ARouter;
import com.cloud.xtilus.makingfriends.section.base.BaseInitFragment;
import com.cloud.xtilus.makingfriends.util.glide.ALoader;
import com.vmloft.develop.library.tools.picker.VMPickerLoader;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMStr;
import com.vmloft.develop.library.tools.utils.VMTheme;
import com.vmloft.develop.library.tools.widget.VMLineView;
import com.vmloft.develop.library.tools.widget.toast.VMToast;

import java.util.Objects;

/**
 * Create by lzan13 on 2019/04/12
 *
 * 我的界面
 */
public class MeFragment extends BaseInitFragment implements View.OnClickListener{


    private View mInfoContainer;
    private ImageView mAvatarView;
    private TextView mNameView;
    private TextView mSignatureView;
    private  MakingFriendApplication application;
    private Intent intent;
    private User user;

    private  VMLineView vmMeCollect;
    private  VMLineView vmMeMonment;
    private  VMLineView  vmMeSetting;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        mInfoContainer = findViewById(R.id.me_info_layout);
        mAvatarView = findViewById(R.id.me_avatar_img);
        mNameView = findViewById(R.id.me_name_tv);
        mSignatureView = findViewById(R.id.me_signature_tv);
        vmMeCollect=findViewById(R.id.me_collect);
        vmMeMonment=findViewById(R.id.me_monment);
        vmMeSetting=findViewById(R.id.me_setting);
    }


    @Override
    protected void initData() {
        application=(MakingFriendApplication) Objects.requireNonNull(getContext()).getApplicationContext();

        VMTheme.changeShadow(mInfoContainer);
        // refreshUI();
    }


    @Override
    protected void initListener() {
        super.initListener();
        mInfoContainer.setOnClickListener(this);
        mAvatarView.setOnClickListener(this);
        mNameView.setOnClickListener(this);
        mSignatureView.setOnClickListener(this);
        vmMeMonment.setOnClickListener(this);
        vmMeCollect.setOnClickListener(this);
        vmMeSetting.setOnClickListener(this);



    }


    /**
     * Fragment 的工厂方法，方便创建并设置参数
     */
    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    //  mAccount = ASignManager.getInstance().getCurrentAccount();
        refreshUI();
    }








    // @OnClick({ R.id.me_info_layout,R.id.me_friend_layout,R.id.me_fans_layout,R.id.me_follows_layout,R.id.me_collect, R.id.me_setting , R.id.me_monment})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.me_info_layout:
                 ARouter.goMeInfo(getActivity());
                break;
            case R.id.me_friend_layout:
                ARouter. getFriends(getActivity());

                break;
            case R.id.me_follows_layout:
                break;
            case R.id.me_fans_layout:
                break;
            case R.id.me_collect:
                VMToast.make(getActivity(), "还没有收藏呢").error();
                break;
            case R.id.me_setting:
                intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                break;
                //  ARouter.goSetting(getActivity());
            case R.id.me_monment:
                intent = new Intent(getContext(), MomentsActivity.class);
                startActivity(intent);
                break;

        }
    }


    /**
     * 刷新 UI
     */
    private void refreshUI() {

        JSONObject jsonObject= MakingFriendApplication.getInstance().getUserJson();

        if (jsonObject == null) {
            return;
        }
        if (VMStr.isEmpty(jsonObject.getString("username"))) {
            mNameView.setText(jsonObject.getString("username"));
        } else {
            mNameView.setText(jsonObject.getString("nick"));
        }

        if (VMStr.isEmpty(jsonObject.getString("makingfriendWord"))) {
            mSignatureView.setText(VMStr.byRes(R.string.me_signature_default));
        } else {
            mSignatureView.setText(jsonObject.getString("makingfriendWord"));
        }
        UserCacheInfo userCacheInfo= UserCacheManager.getMyInfo();
        String vatarUrl=null;

        if(userCacheInfo!=null){
            vatarUrl=userCacheInfo.getAvatarUrl();

        }else{
            vatarUrl= jsonObject.getString("avatar");

        }
        VMPickerLoader.Options options = new VMPickerLoader.Options(vatarUrl);

        if (AIMManager.getInstance().isCircleAvatar()) {
            options.isCircle = true;
        } else {
            options.isRadius = true;
            options.radiusSize = VMDimen.dp2px(4);
        }
        ALoader.load(getActivity(), options, mAvatarView);




    }


}
