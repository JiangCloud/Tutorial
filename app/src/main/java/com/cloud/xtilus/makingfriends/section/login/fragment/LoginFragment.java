package com.cloud.xtilus.makingfriends.section.login.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSONObject;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.activity.im.MainActivity;
import com.cloud.xtilus.makingfriends.application.DemoHelper;
import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.common.db.DemoDbHelper;
import com.cloud.xtilus.makingfriends.common.interfaceOrImplement.OnResourceParseCallback;
import com.cloud.xtilus.makingfriends.common.utils.ToastUtils;
import com.cloud.xtilus.makingfriends.http.OkHttpUtils;
import com.cloud.xtilus.makingfriends.section.base.BaseInitFragment;
import com.cloud.xtilus.makingfriends.section.login.viewmodels.LoginFragmentViewModel;
import com.cloud.xtilus.makingfriends.section.login.viewmodels.LoginViewModel;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.cloud.xtilus.makingfriends.util.MD5;
import com.cloud.xtilus.makingfriends.util.Param;
import com.hyphenate.EMError;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseEditTextUtils;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends BaseInitFragment implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener, TextView.OnEditorActionListener {
    private EditText mEtLoginName;
    private EditText mEtLoginPwd;
    private TextView mTvLoginRegister;
    private TextView mTvLoginToken;
    private TextView mTvLoginServerSet;
    private Button mBtnLogin;
    private CheckBox cbSelect;
    private TextView tvAgreement;
    private String mUserName;
    private String mPwd;
    private LoginViewModel mViewModel;
    private boolean isTokenFlag;//是否是token登录
    private LoginFragmentViewModel mFragmentViewModel;
    private Drawable clear;
    private Drawable eyeOpen;
    private Drawable eyeClose;

    @Override
    protected int getLayoutId() {
        return R.layout.demo_fragment_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mEtLoginName = findViewById(R.id.et_login_name);
        mEtLoginPwd = findViewById(R.id.et_login_pwd);
        mTvLoginRegister = findViewById(R.id.tv_login_register);
        mTvLoginToken = findViewById(R.id.tv_login_token);
        mTvLoginServerSet = findViewById(R.id.tv_login_server_set);
        mBtnLogin = findViewById(R.id.btn_login);
        tvAgreement = findViewById(R.id.tv_agreement);
        cbSelect = findViewById(R.id.cb_select);
        // 保证切换fragment后相关状态正确
        boolean enableTokenLogin = DemoHelper.getInstance().getModel().isEnableTokenLogin();
        mTvLoginToken.setVisibility(enableTokenLogin ? View.VISIBLE : View.GONE);
        if(!TextUtils.isEmpty(DemoHelper.getInstance().getCurrentLoginUser())) {
            mEtLoginName.setText(DemoHelper.getInstance().getCurrentLoginUser());
        }
        if(isTokenFlag) {
            switchLogin();
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mEtLoginName.addTextChangedListener(this);
        mEtLoginPwd.addTextChangedListener(this);
        mTvLoginRegister.setOnClickListener(this);
        mTvLoginToken.setOnClickListener(this);
        mTvLoginServerSet.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        cbSelect.setOnCheckedChangeListener(this);
        mEtLoginPwd.setOnEditorActionListener(this);
        EaseEditTextUtils.clearEditTextListener(mEtLoginName);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mFragmentViewModel = new ViewModelProvider(this).get(LoginFragmentViewModel.class);
        mFragmentViewModel.getLoginObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<EaseUser>(true) {
                @Override
                public void onSuccess(EaseUser data) {
                    DemoHelper.getInstance().setAutoLogin(true);
                    //跳转到主页
                    MainActivity.startAction(mContext);
                    mContext.finish();
                }

                @Override
                public void onError(int code, String message) {
                    super.onError(code, message);
                    if(code == EMError.USER_AUTHENTICATION_FAILED) {
                        ToastUtils.showToast(R.string.demo_error_user_authentication_failed);
                    }else {
                        ToastUtils.showToast(message);
                    }
                }

                @Override
                public void onLoading(EaseUser data) {
                    super.onLoading(data);
                    showLoading();
                }

                @Override
                public void hideLoading() {
                    super.hideLoading();
                    LoginFragment.this.dismissLoading();
                }
            });

        });
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel = new ViewModelProvider(mContext).get(LoginViewModel.class);
        mViewModel.getRegisterObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<String>(true) {
                @Override
                public void onSuccess(String data) {
                    mEtLoginName.setText(TextUtils.isEmpty(data)?"":data);
                    mEtLoginPwd.setText("");
                }
            });

        });
        DemoDbHelper.getInstance(mContext).getDatabaseCreatedObservable().observe(getViewLifecycleOwner(), response -> {
            if(response != null && !TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd)) {


                LoginToAppServer();
               // mFragmentViewModel.login(mUserName, mPwd, isTokenFlag);
            }
        });
    }

    public void LoginToAppServer() {

        List<Param> params = new ArrayList<>();
        params.add(new Param("username", mUserName));
        params.add(new Param("password", MD5.digest(mPwd)));
        new OkHttpUtils(getContext()).post(params, Constant.URL_LOGIN, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getInteger("ret_code");
                switch(code){

                    case 200:
                        JSONObject userJson = jsonObject.getJSONArray("ret_data").getJSONObject(0);
                        //LoginToIMServer(userJson);
                        mFragmentViewModel.login(mUserName, mPwd, isTokenFlag,userJson);
                        MakingFriendApplication.getInstance().setIsLoging(true);
                        break;
                    case 120:
                        String ret_msg = jsonObject.getString("ret_msg");
                        Toast.makeText(getContext(),ret_msg, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getContext(),"系统出现未知错误!", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(getContext(),"系统繁忙!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        tvAgreement.setText(getSpannable());
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        //切换密码可见不可见的两张图片
        eyeClose = getResources().getDrawable(R.drawable.d_pwd_hide);
        eyeOpen = getResources().getDrawable(R.drawable.d_pwd_show);
        clear = getResources().getDrawable(R.drawable.d_clear);
        EaseEditTextUtils.showRightDrawable(mEtLoginName, clear);
        EaseEditTextUtils.changePwdDrawableRight(mEtLoginPwd, eyeClose, eyeOpen, null, null, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_register :
                mViewModel.clearRegisterInfo();
                mViewModel.setPageSelect(1);
                break;
            case R.id.tv_login_token:
                isTokenFlag = !isTokenFlag;
                switchLogin();
//                TestActivity.startAction(mContext);
                break;
            case R.id.tv_login_server_set:
                mViewModel.setPageSelect(2);
                break;
            case R.id.btn_login:
                loginToServer();
                break;
        }
    }

    /**
     * 切换登录方式
     */
    private void switchLogin() {
        mEtLoginPwd.setText("");
        if(isTokenFlag) {
            mEtLoginPwd.setHint(R.string.em_login_token_hint);
            mTvLoginToken.setText(R.string.em_login_tv_pwd);
            mEtLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        }else {
            mEtLoginPwd.setHint(R.string.em_login_password_hint);
            mTvLoginToken.setText(R.string.em_login_tv_token);
            mEtLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    private void loginToServer() {
        if(TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPwd)) {
            ToastUtils.showToast(R.string.em_login_btn_info_incomplete);
            return;
        }
        //先初始化数据库
        DemoDbHelper.getInstance(mContext).initDb(mUserName);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mUserName = mEtLoginName.getText().toString().trim();
        mPwd = mEtLoginPwd.getText().toString().trim();
        EaseEditTextUtils.showRightDrawable(mEtLoginName, clear);
        EaseEditTextUtils.showRightDrawable(mEtLoginPwd, isTokenFlag ? null : eyeClose);
        setButtonEnable(!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_select :
                setButtonEnable(!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd) && isChecked);
                break;
        }
    }

    private void setButtonEnable(boolean enable) {
        mBtnLogin.setEnabled(enable);
        if(mEtLoginPwd.hasFocus()) {
            mEtLoginPwd.setImeOptions(enable ? EditorInfo.IME_ACTION_DONE : EditorInfo.IME_ACTION_PREVIOUS);
        }else if(mEtLoginName.hasFocus()) {
            mEtLoginPwd.setImeOptions(enable ? EditorInfo.IME_ACTION_DONE : EditorInfo.IME_ACTION_NEXT);
        }

        //同时需要修改右侧drawalbeRight对应的资源
//        Drawable rightDrawable;
//        if(enable) {
//            rightDrawable = ContextCompat.getDrawable(mContext, R.drawable.demo_login_btn_right_enable);
//        }else {
//            rightDrawable = ContextCompat.getDrawable(mContext, R.drawable.demo_login_btn_right_unable);
//        }
//        mBtnLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
    }

    private SpannableString getSpannable() {
        SpannableString spanStr = new SpannableString(getString(R.string.em_login_agreement));
        //设置下划线
        //spanStr.setSpan(new UnderlineSpan(), 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new MyClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showToast("跳转到服务条款");
            }
        }, 2, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //spanStr.setSpan(new UnderlineSpan(), 10, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new MyClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showToast("跳转到隐私协议");
            }
        }, 11, spanStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE) {
            if(!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd)) {
                hideKeyboard();
                loginToServer();
                return true;
            }
        }
        return false;
    }

    private abstract class MyClickableSpan extends ClickableSpan {

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.bgColor = Color.TRANSPARENT;
        }
    }
}
