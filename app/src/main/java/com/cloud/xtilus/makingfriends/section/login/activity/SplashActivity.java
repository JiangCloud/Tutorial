package com.cloud.xtilus.makingfriends.section.login.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.activity.im.MainActivity;
import com.cloud.xtilus.makingfriends.common.interfaceOrImplement.OnResourceParseCallback;
import com.cloud.xtilus.makingfriends.section.base.BaseInitActivity;
import com.cloud.xtilus.makingfriends.section.login.viewmodels.SplashViewModel;
import com.hyphenate.util.EMLog;

import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends BaseInitActivity {
    private ImageView ivSplash;
    private TextView tvProduct;
    private SplashViewModel model;

    @Override
    protected int getLayoutId() {
        return R.layout.demo_splash_activity;
    }

    @Override
    protected void initSystemFit() {
        setFitSystemForTheme(false, R.color.transparent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ivSplash = findViewById(R.id.iv_splash);
        tvProduct = findViewById(R.id.tv_product);
    }

    @Override
    protected void initData() {
        super.initData();
        model = new ViewModelProvider(this).get(SplashViewModel.class);
        ivSplash.animate()
                .alpha(1)
                .setDuration(500)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loginSDK();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();

        tvProduct.animate()
                .alpha(1)
                .setDuration(500)
                .start();

    }

    private void loginSDK() {
        model.getLoginData().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<Boolean>(true) {
                @Override
                public void onSuccess(Boolean data) {
                    MainActivity.startAction(mContext);
                    finish();
                }

                @Override
                public void onError(int code, String message) {
                    super.onError(code, message);
                    EMLog.i("TAG", "error message = "+response.getMessage());
                    LoginActivity.startAction(mContext);
                    finish();
                }
            });

        });
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
