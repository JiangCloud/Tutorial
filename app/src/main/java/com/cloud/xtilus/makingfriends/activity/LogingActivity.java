package com.cloud.xtilus.makingfriends.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONObject;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.activity.im.MainActivity;
import com.cloud.xtilus.makingfriends.application.DemoHelper;
import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.http.OkHttpUtils;
import com.cloud.xtilus.makingfriends.runtimepermissions.PermissionsManager;
import com.cloud.xtilus.makingfriends.runtimepermissions.PermissionsResultAction;
import com.cloud.xtilus.makingfriends.util.CommonHelper;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.cloud.xtilus.makingfriends.util.MD5;
import com.cloud.xtilus.makingfriends.util.Param;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


public class LogingActivity extends AppCompatActivity implements View.OnClickListener,PlatformActionListener,Handler.Callback {

    @ViewInject(R.id.loginButton)
    private TextView loginButton;
    @ViewInject(R.id.registerBtn)
    private TextView registerBtn;
    @ViewInject(R.id.gv_thirdLogin)
    private GridView  thirdLoginGridView;

    @ViewInject(R.id.et_phoneNum)
    private EditText mAccount;
    @ViewInject(R.id.et_passWord)
    private EditText mPassword;


    @ViewInject(R.id.toolbars)
    private Toolbar toolbars;

    boolean mBLogin=false;//判断是否已经登陆

    private Context context=LogingActivity.this;
    private MakingFriendApplication application;

    private Handler handler;
    //private static final int MSG_SMSSDK_CALLBACK = 1;
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR= 3;
    private static final int MSG_AUTH_COMPLETE = 4;

    private static final String TAG = LogingActivity.class.getSimpleName();
    private  StringBuilder mStrRetMsg;
    private StringBuilder strUserSig;
    private String mStrUserName,mStrPassWord;
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "f5c4118a0739";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "27b701ba70ddb0db4ece3b1d5c5132c3";

    private boolean autoLogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // enter the main activity if already logged in
        if (DemoHelper.getInstance().isLoggedIn()) {
            autoLogin = true;
            MakingFriendApplication.getInstance().setIsLoging(true);//判断
            startActivity(new Intent(LogingActivity.this, MainActivity.class));

            return;
        }

        setContentView(R.layout.activity_loging);
        ViewUtils.inject(this);
        initToolBar();
        requestPermissions();
        onInit();

    }
    private void initToolBar(){
        toolbars.setTitle("登陆");
        setSupportActionBar(toolbars);
        toolbars.setNavigationIcon(R.drawable.icon_back_32px);




    }
    public void onInit() {
        application= MakingFriendApplication. getInstance();
        //初始化shareSDK
       // ShareSDK.initSDK(context);

        MobSDK.init(context,APPKEY,APPSECRET);
        ViewUtils.inject(this);
        registerBtn.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        handler=new Handler(this);

        mStrRetMsg = new StringBuilder();
        strUserSig = new StringBuilder();

        //String tmpName = MakingFriendApplication.getInstance().getUserName();
     /*   if(tmpName != null){
            mAccount.setText(tmpName);
        }*/

        thirdLoginGridView.setAdapter(new thirdLogingdapter());
        thirdLoginGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Platform qzone = ShareSDK.getPlatform(QQ.NAME);
                        authorize(qzone);
                        break;
                    case 1:
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        authorize(wechat);
                        break;
                    case 2:
                        //新浪微博
                        Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                        authorize(sina);
                        break;

                }

            }
        });

    }


    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = new Object[] {platform.getName(), res};
            handler.sendMessage(msg);
        }

    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
        }
        throwable.printStackTrace();

    }

    @Override
    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }

    }

    @Override
  public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                doLoging();

                break;
            case R.id.registerBtn:
                Intent intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;

        }



  }

    private void doLoging() {
        if(mBLogin){
            Toast.makeText(context, "已经在登陆中，请稍等！", Toast.LENGTH_SHORT).show();
            return;
        }
       // mBLogin=true;
        mStrUserName = mAccount.getText().toString().trim();

        mStrPassWord=mPassword.getText().toString().trim();
       // mStrPassWord =MD5.digest(mPassword.getText().toString().trim());
        if(!CommonHelper.GetNetWorkStatus(getBaseContext())){
            Toast.makeText(context,"网络不可用，请检查网络设置!", Toast.LENGTH_LONG).show();
            mBLogin=false;
            return;
        }
        if ( (mStrUserName.length() == 0)  || (mStrPassWord.length()==0) ) {
            Toast.makeText(context, "亲！帐号或密码不能为空哦", Toast.LENGTH_SHORT).show();
            mBLogin=false;
            return;
        }
        //DemoHelper.getInstance().setCurrentUserName(mStrUserName);
       // if (application.isClientStart()){
            LoginToAppServer();
       // }else{

         //   Toast.makeText(context, "亲！sdk暂未初始化", Toast.LENGTH_SHORT).show();
     //   }



    }

    public void LoginToAppServer() {

        List<Param> params = new ArrayList<>();
        params.add(new Param("username", mStrUserName));
        params.add(new Param("password", MD5.digest(mStrPassWord)));
        new OkHttpUtils(context).post(params, Constant.URL_LOGIN, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getInteger("ret_code");
                switch(code){

                    case 200:
                        JSONObject userJson = jsonObject.getJSONArray("ret_data").getJSONObject(0);
                        LoginToIMServer(userJson);
                        MakingFriendApplication.getInstance().setIsLoging(true);
                        break;
                    case 120:
                         String ret_msg = jsonObject.getString("ret_msg");
                         Toast.makeText(context,ret_msg, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(context,"系统出现未知错误!", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(context,"系统繁忙!", Toast.LENGTH_LONG).show();
            }
        });
    }



    private void LoginToIMServer(final JSONObject userJson) {
        EMClient.getInstance().login(mStrUserName, mStrPassWord, new EMCallBack() {
            @Override
            public void onSuccess() {



                MakingFriendApplication.getInstance().setUserJson(userJson);
                Log.d("UserInfo---------------",userJson.toString());
                Intent intent = new Intent();
                intent.setClass(context, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(int i,final String s) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "login failed"+s, Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });





        //发起到app c2c关系链后台验证是否有注册成功过的
      /*  new Thread( new Runnable(){
            @Override
            public void run() {

                Log.d(TAG,"log to app:" + mStrUserName +":" + mStrPassWord);
                final int iRet = HttpProcessor.doRequestLogin(mStrUserName, mStrPassWord, mStrRetMsg, strUserSig);
                Log.d(TAG, iRet + ":" + mStrRetMsg + ":" + strUserSig);


                runOnUiThread(new Runnable() {
                    public void run(){
                        if(iRet == 0){
                            //SharedPreferencesHelper sharedPreferencesHelper=  SharedPreferencesHelper.getInstance();

                          // sharedPreferencesHelper.saveData("username",mStrUserName);
                          // sharedPreferencesHelper.saveData("password", mStrPassWord);
                            // application.setUserSig(strUserSig.toString());
                           // application.setIdentifier(mStrUserName);
                           // application.setIsLoging(true);
                            Intent intent = new Intent();
                            intent.setClass(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                            //getContactsFromServer();
                        }else{
                            Toast.makeText(getBaseContext(), "登录失败:" + iRet + ":" + mStrRetMsg, Toast.LENGTH_SHORT).show();
                            mBLogin=false;

                        }
                    }
                });
            }

        }).start();
*/


    }



    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_AUTH_CANCEL:
                //取消授权
                Toast.makeText(this, "取消授权", Toast.LENGTH_SHORT).show();
                break;
            case MSG_AUTH_ERROR:

            //授权失败
            Toast.makeText(this,"授权失败", Toast.LENGTH_SHORT).show();
                break;
            //授权成功
           case MSG_AUTH_COMPLETE:
               Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
               Object[] objs = (Object[]) msg.obj;
               String platform = (String) objs[0];
               HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
               for(String key: res.keySet()){

                  Log.d("aa", res.get(key).toString());
               }

               break;

        }

        return false;

    }
    private void authorize(Platform plat) {
        if (plat == null) {
           // popupOthers();
            return;
        }

        plat.setPlatformActionListener(this);
        //关闭SSO授权
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class thirdLogingdapter extends BaseAdapter{


        private String name[]=new String[]{"QQ","微信","新浪微博"};

        private int iconArrary[]=new int[]{R.mipmap.signupqq,R.mipmap.signupweixin,R.mipmap.signupweibo};

         @Override
         public int getCount() {
             return iconArrary.length;
         }

         @Override
         public Object getItem(int position) {
             return position;
         }

         @Override
         public long getItemId(int position) {
             return position;
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {

             LayoutInflater inflater=   LayoutInflater.from(context);
             View view= inflater.inflate(R.layout.login_itern, null);
             ImageView loginImage=(ImageView) view.findViewById(R.id.iv_loginImage);
             TextView logingTitle= (TextView) view.findViewById(R.id.tv_title);
             loginImage.setImageResource(iconArrary[position]);
             logingTitle.setText(name[position]);

             return view;
         }
     }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent=getIntent();
        String phoneNum=intent.getStringExtra("phoneNum");
        if(phoneNum!=null) {
            mAccount.setText(phoneNum);
        }
        if (autoLogin) {
            return;
        }



    }


    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(String permission) {
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }




    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
