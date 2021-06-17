package com.cloud.xtilus.makingfriends.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.bean.ResponseBean;
import com.cloud.xtilus.makingfriends.bean.User;
import com.cloud.xtilus.makingfriends.http.BaseCallback;
import com.cloud.xtilus.makingfriends.http.OkHttpHelper;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.cloud.xtilus.makingfriends.util.MD5;
import com.cloud.xtilus.makingfriends.util.Util;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity  implements View.OnClickListener {


    @ViewInject(R.id.etPhoneNum)
    private EditText phoneText;

    @ViewInject(R.id.checkPhoneNum)
    private EditText checkPhoneText;
    @ViewInject(R.id.sendBtn)
    private Button  getCodeBtn;

    @ViewInject(R.id.registerSubBtn)
    private Button registerSubBtn;

    @ViewInject(R.id.registerPassword)
    private EditText passwordEt;

    @ViewInject(R.id.registerRePassword)
    private EditText rePasswordEt;

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    // 填写从短信SDK应用后台注册得到的APPKEY
  //  private static String APPKEY = "f5c4118a0739";
    // 填写从短信SDK应用后台注册得到的APPSECRET
   // private static String APPSECRET = "27b701ba70ddb0db4ece3b1d5c5132c3";
    public String phoneNum;
    public static  final  int reaskDuration=30;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);
        getCodeBtn.setOnClickListener(this);
        registerSubBtn.setOnClickListener(this);

        initToolBar();
        initSDK();


    }

    private void initToolBar(){
        toolbar.setTitle("注册");
         setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back_32px);




    }

    private void initSDK(){

     //   MobSDK.init(getApplication(),APPKEY,APPSECRET);
        //MobSDK.init(context, "你的AppKey", "你的AppSecret");
         EventHandler eventHandler=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message=new Message();

                message.arg1=event;
                message.arg2=result;
                message.obj=data;

                mhandler.sendMessage(message);

            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.sendBtn:
                if(!TextUtils.isEmpty(phoneText.getText().toString())){
                    SMSSDK.getVerificationCode("86",phoneText.getText().toString());
                    phoneNum=phoneText.getText().toString();

                    // 在获取验证码按钮上显示重新获取验证码的时间间隔
                    Util.startTimer(this, getCodeBtn, "获取验证码", "重新获取", reaskDuration, 1);


                }else{

                    Toast.makeText(this,"手机号不能为空",Toast.LENGTH_SHORT).show();


                }
                break;
            case R.id.registerSubBtn:

                if(TextUtils.isEmpty(phoneText.getText().toString())){

                    Toast.makeText(this, "手机号码不能为空 ", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    phoneNum=phoneText.getText().toString();
                    if(!Util.validPhoneNumber("", phoneNum)) {
                        Toast.makeText(this, "手机号码不正确 ", Toast.LENGTH_SHORT).show();
                        return;

                    }

                }
                if(TextUtils.isEmpty(checkPhoneText.getText().toString())){

                    Toast.makeText(this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                    return;

                }
                if(TextUtils.isEmpty(passwordEt.getText().toString())||TextUtils.isEmpty(rePasswordEt.getText().toString())){

                    Toast.makeText(this, "密码不能为空 ", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(!passwordEt.getText().toString().equals(rePasswordEt.getText().toString())) {

                    Toast.makeText(this, "两次输入的密码不一致 ", Toast.LENGTH_SHORT).show();
                    return;

                }

                SMSSDK.submitVerificationCode("86",phoneNum,checkPhoneText.getText().toString());
                break;
            case R.id.tv_reLongin:
                Intent intent=getIntent();
                intent.setClass(this,LogingActivity.class);
                startActivity(intent);
                break;




        }

    }

    Handler mhandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            Log.e("event", "event=" + event);
            if(result==SMSSDK.RESULT_COMPLETE){



                if(event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                   // flag=true;
                    Toast.makeText(getApplication(),"提交验证码成功",Toast.LENGTH_SHORT).show();

                    Map<String,Object > map=new HashMap<>();
                    map.put("phone",phoneNum);
                    map.put("password", MD5.digest(rePasswordEt.getText().toString()));
                    OkHttpHelper.getInstance().post(Constant.USER_REGISTER_URL, map, new BaseCallback<ResponseBean<User>>() {
                        @Override
                        public void onBeforeRequest(Request request) {

                        }

                        @Override
                        public void onFailure(Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(Response response) {

                        }

                        @Override
                        public void onSuccess(Response response, ResponseBean responseBean) {

                            int code= responseBean.getRet_code();
                            String msg=responseBean.getRet_msg();


                          //  Toast.makeText(getApplication(),code,Toast.LENGTH_SHORT).show();
                            if(code==200){
                                Toast.makeText(getApplication(),msg,Toast.LENGTH_SHORT).show();
                                Intent  intent =new Intent(RegisterActivity.this,LogingActivity.class);
                                intent.putExtra("phoneNum",phoneNum);
                                startActivity(intent);


                            }else{

                                Toast.makeText(getApplication(),msg,Toast.LENGTH_SHORT).show();
                            }






                        }

                        @Override
                        public void onError(Response response, int code, Exception e) {

                        }
                    });



                }else if(event==SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    Toast.makeText(getApplication(),"验证码已发送",Toast.LENGTH_SHORT).show();

                }


          }else{
                try {
                    int status = 0;
                    ((Throwable)data).getStackTrace();
                    Throwable throwable = (Throwable) data;


                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    SMSLog.getInstance().w(e);
                }
            }
        };

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }



}
