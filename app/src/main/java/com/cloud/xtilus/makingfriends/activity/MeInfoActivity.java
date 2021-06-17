package com.cloud.xtilus.makingfriends.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.cache.UserCacheInfo;
import com.cloud.xtilus.makingfriends.cache.UserCacheManager;
import com.cloud.xtilus.makingfriends.http.OkHttpUtils;
import com.cloud.xtilus.makingfriends.im.AIMManager;
import com.cloud.xtilus.makingfriends.util.CommonUtils;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.cloud.xtilus.makingfriends.util.Param;
import com.cloud.xtilus.makingfriends.util.glide.ALoader;
import com.vmloft.develop.library.tools.base.VMConstant;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.picker.VMPickerLoader;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMStr;
import com.vmloft.develop.library.tools.widget.VMLineView;
import com.vmloft.develop.library.tools.widget.toast.VMToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by lzan13 on 2019/5/12 22:20
 *
 * 个人信息界面
 */
public class MeInfoActivity extends AppActivity {

    @BindView(R.id.me_avatar_line) VMLineView mAvatarLine;
    @BindView(R.id.me_nickname_line) VMLineView mNicknameLine;
    @BindView(R.id.me_username_line) VMLineView mUsernameLine;
    @BindView(R.id.me_qr_code) VMLineView mQRCodeLine;
    @BindView(R.id.me_signature_line) VMLineView mSignatureLine;
    @BindView(R.id.me_gender_line) VMLineView mGenderLine;
    @BindView(R.id.me_birthday) VMLineView mBirthdayLine;
    @BindView(R.id.me_address_line) VMLineView mAddressLine;
    // 个人用户
    //private AAccount mAccount;
    private OSS oss;
    private  String userId;
    @Override
    protected int layoutId() {
        return R.layout.activity_me_info;
    }

    @Override
    protected void initUI() {
        super.initUI();
    }

    @Override
    protected void initData() {
        setTopTitle(R.string.me_info);
        oss=MakingFriendApplication.getInstance().getOss();
        userId  = MakingFriendApplication.getInstance().getUserJson().getString(Constant.JSON_KEY_HXID);

       // mAccount = ASignManager.getInstance().getCurrentAccount();
    }
   // VMSPUtil
    @OnClick({R.id.me_avatar_line, R.id.me_nickname_line, R.id.me_qr_code, R.id.me_signature_line, R.id.me_gender_line, R.id.me_birthday,
        R.id.me_address_line
    })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.me_avatar_line:
            startPickAvatar();
            break;
        case R.id.me_nickname_line:
           // startActivity(new Intent(mActivity, MeNicknameActivity.class));
            break;
        case R.id.me_qr_code:
            break;
        case R.id.me_signature_line:
          //  startActivity(new Intent(mActivity, MeSignatureActivity.class));
            break;
        case R.id.me_gender_line:
            break;
        case R.id.me_birthday:
            break;
        case R.id.me_address_line:
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUI();
    }

    /**
     * 开启选择头像
     */
    private void startPickAvatar() {
        VMPicker.getInstance().setMultiMode(false).setShowCamera(true).startPicker(mActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        VMToast.make(mActivity,"requestCode"+requestCode).done();
        if (resultCode == RESULT_OK) {
            if (data != null && requestCode == VMConstant.VM_PICK_REQUEST_CODE) {
                List<VMPictureBean> pictures = VMPicker.getInstance().getResultData();

                String url = Constant.baseImgUrl + pictures.get(0).name;
                OSSAsyncTask  task= saveAvatar(pictures.get(0),url);

                try {
                    int code=task.getResult().getStatusCode();
                    Log.d("StatusCode","============="+code);
                    UserCacheManager.updateMyAvatar(url);


                    } catch (Exception e) {

                        e.printStackTrace();

                    }
            } else {
                VMToast.make(mActivity, "没有数据").error();
            }
        }
    }

    /**
     * 保存头像
     */
    public OSSAsyncTask saveAvatar(VMPictureBean bean,String url) {

        OSSAsyncTask task;

       // private void uploadImageback( final String fileName, String filePath, final Dialog progressDialog) {

            PutObjectRequest put = new PutObjectRequest("showshop", bean.name, bean.path);

            // You can set progress callback during asynchronous upload
            put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                    Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                }
            });

              task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {

                    //final String url = Constant.baseImgUrl + bean.name;
                          runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //uploadBackground(url,progressDialog);
                            uploadAvatar(url);
                        }
                    });
                    Log.d("PutObject", "UploadSuccess");
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                    // Request exception
                      runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // progressDialog.dismiss();
                        }
                    });

                    if (clientExcepion != null) {
                        // Local exception, such as a network exception
                        clientExcepion.printStackTrace();
                    }
                    if (serviceException != null) {

                        // Service exception

                        Log.e("ErrorCode", serviceException.getErrorCode());
                        Log.e("RequestId", serviceException.getRequestId());
                        Log.e("HostId", serviceException.getHostId());
                        Log.e("RawMessage", serviceException.getRawMessage());
                    }
                }
            });


              return task;


// task.cancel(); // Cancel the task

// task.waitUntilFinished(); // Wait till the task is finished


        }










       /* AUMSManager.getInstance().saveAvatar(bean, new ACallback<AAccount>() {
            @Override
            public void onSuccess(AAccount account) {
                VMToast.make(mActivity, "头像设置成功").done();
                //ASignManager.getInstance().setCurrentAccount(account);
                // 发送消息通知其他人更新了信息
                //IMChatManager.getInstance().sendContactInfoChange();
            }

            @Override
            public void onError(int code, String desc) {
                VMToast.make(mActivity, "头像设置失败 %d %s", code, desc).done();
            }
        });
    }
*/


    private void uploadAvatar(final String url) {
        List<Param> params = new ArrayList<Param>();
        params.add(new Param("avatarUrl", url));
        params.add(new Param("userId", userId));
        new OkHttpUtils(this).post(params, Constant.URL_UPLOAD_MOMENT_BACKGROUND, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //progressDialog.dismiss();
                int code = jsonObject.getIntValue("ret_code");
                switch (code) {
                    case 200:
                       // monmentsView.showBackground(url);
                       // backgroudMoment = url;
                      //  ACache.get(getBaseContext()).put(cacheKeyBg,url);
                        VMToast.make(mActivity,R.string.update_success).done();

                       // CommonUtils.showToastShort(getBaseContext(),R.string.update_success);
                        break;
                    default:
                        VMToast.make(mActivity,R.string.update_failed).done();
                        //CommonUtils.showToastShort(getBaseContext(),R.string.update_failed);
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                CommonUtils.showToastShort(getBaseContext(),R.string.update_failed);
               // CommonUtils.showToastShort(getBaseContext(),R.string.update_failed);
               // progressDialog.dismiss();
            }
        });
    }

    /**
     * 刷新 UI
     */
    private void refreshUI() {
        JSONObject jsonObject= MakingFriendApplication.getInstance().getUserJson();

        if (jsonObject == null) {
            return;
        }

        // 加载头像

        UserCacheInfo userCacheInfo= UserCacheManager.getMyInfo();
        ImageView mAvatarView=new ImageView(this);
        if(userCacheInfo!=null){

            VMPickerLoader.Options options = new VMPickerLoader.Options(userCacheInfo.getAvatarUrl());
            if (AIMManager.getInstance().isCircleAvatar()) {
                options.isCircle = true;
            } else {
                options.isRadius = true;
                options.radiusSize = VMDimen.dp2px(2);
            }
            ALoader.load(this, options, mAvatarView);
        }


/*
        Glide.with(this).load(avatarUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.default_avatar)
              .circleCrop(). crossFade( VMDimen.dp2px(4)) .into(mAvatarView);*/



   //     imageView.setImageResource(R.mipmap.default_avatar);

        mAvatarLine.setRightView(mAvatarView);

        mUsernameLine.setCaption(jsonObject.getString("username"));
        if (VMStr.isEmpty(jsonObject.getString("nick"))) {
            mNicknameLine.setCaption(jsonObject.getString("username"));
        } else {
            mNicknameLine.setCaption(jsonObject.getString("nick"));
        }

        if (VMStr.isEmpty(jsonObject.getString("makingfriendWord"))) {
            mSignatureLine.setCaption(VMStr.byRes(R.string.me_signature_default));
        } else {
            mSignatureLine.setCaption(jsonObject.getString("makingfriendWord"));
        }

        if (jsonObject.getInteger("sex") == 0) {
            mGenderLine.setCaption(VMStr.byRes(R.string.me_gender_woman));
        } else if (jsonObject.getInteger("sex")== 1) {
            mGenderLine.setCaption(VMStr.byRes(R.string.me_gender_man));
        } else if (jsonObject.getInteger("sex") == 2) {
            mGenderLine.setCaption(VMStr.byRes(R.string.me_gender_unknown));
        }


        mBirthdayLine.setCaption(jsonObject.getString("bornDate"));
        mAddressLine.setCaption(jsonObject.getString("address"));
    }
}
