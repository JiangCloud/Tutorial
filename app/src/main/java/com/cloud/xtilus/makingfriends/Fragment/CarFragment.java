package com.cloud.xtilus.makingfriends.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cloud.xtilus.makingfriends.Adapter.MineAdapter;
import com.cloud.xtilus.makingfriends.activity.LogingActivity;
import com.cloud.xtilus.makingfriends.activity.MapActivity;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.activity.PicturesActivity;
import com.cloud.xtilus.makingfriends.activity.moments.MomentsActivity;
import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.bean.ResponseBean;
import com.cloud.xtilus.makingfriends.bean.VersionInfo;
import com.cloud.xtilus.makingfriends.http.BaseCallback;
import com.cloud.xtilus.makingfriends.http.OkHttpHelper;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.hyphenate.chat.EMClient;
import com.lidroid.xutils.view.annotation.ViewInject;

import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.VideoActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.Request;
import okhttp3.Response;


public class CarFragment extends BaseFragment implements AdapterView.OnItemClickListener{


    @ViewInject(R.id.my_listView)
    private ListView mineListView;

    private Intent intent;
    private ProgressDialog  pd;
    @ViewInject(R.id.loginOut)
    private  Button loginOutBtn;
    private  MakingFriendApplication application;
    private String[] setting =new String[]{"进行认证","编辑资料","我的相册","我们的地址","关于我们","版权更新","问题反馈"};
    @Override
    public View CreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_car,container,false);
    }

    @Override
    public void init() {
        application=(MakingFriendApplication) Objects.requireNonNull(getContext()).getApplicationContext();
        initData();
        showLoginOutBtn();
    }
    private void initData(){

       List<String> s= Arrays.asList(setting);
        mineListView.setAdapter(new MineAdapter(getContext(), s, R.layout.mine_iterm));
        mineListView.setOnItemClickListener(this);

    }

    private void showLoginOutBtn(){
        if(application.isLoging()){

            loginOutBtn.setVisibility(View.VISIBLE);
            loginOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext()).setTitle("退出提醒").setMessage("是否真的要退出").setNegativeButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loginOutBtn.setVisibility(View.INVISIBLE);//隐藏登陆按钮
                            application.setIsLoging(false);//标记没登陆
                            EMClient.getInstance().logout(true);
                            Intent intent=new Intent(getContext(), LogingActivity.class);
                            startActivity(intent);
                           // SharedPreferencesHelper.getInstance().cleanData("username");
                          //  SharedPreferencesHelper.getInstance().cleanData("password");

                        }


                    }).setPositiveButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
                }
            });
        }
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){

            case 2:
                 intent = new Intent(getContext(), PicturesActivity.class);
                startActivity(intent);
                break;

            case 3:
                String url="http://v11-tt.ixigua.com/77ad23b54024ef8bbbfeadd51089f963/5b604323/video/m/220cf183298b2fb4d4196d266f26f68a2c41159c29900005d13161c799e/";
                TbsVideo.openVideo(getContext(),url,null);
                intent = new Intent(getContext(), VideoActivity.class);
                startActivity(intent);

                break;
            case 4:
                intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);
                break;



            case 5:
                checkUpate();

                break;
            case 6:
                intent = new Intent(getContext(), MomentsActivity.class);
                startActivity(intent);

                break;

        }

    }


    private void checkUpate(){

        OkHttpHelper.getInstance().get(Constant.CHECK_VERSION, new BaseCallback<ResponseBean<VersionInfo>>() {
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
            public void onSuccess(Response response, ResponseBean<VersionInfo> responseBean) {
                try {

                        final String apkUlr=responseBean.getRet_data().getDownUrl();




                    if (!responseBean.getRet_data().getVersion().equals(getClientVersion())) {
                        new AlertDialog.Builder(getActivity()).setTitle("升级提醒").setMessage("亲，有新的版本赶快升级").setCancelable(false)
                                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                        downApk(apkUlr);




                            }
                        }).show();


                    }else{

                        Toast.makeText(getContext(), "当前已是最新版本。" , Toast.LENGTH_SHORT).show();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }
    /*
     * 获取当前应用的版本号
     *
     */
    private String getClientVersion() throws PackageManager.NameNotFoundException {
        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo( getActivity().getPackageName(), 0);
        return packageInfo.versionName;
    }


    private void downApk(String apkUrl){

        pd = new ProgressDialog(getContext());
        pd.setTitle("正在下载");
        pd.setIcon(R.mipmap.ic_launcher);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.show();
        final String fileName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1);
        OkHttpHelper.getInstance().get(Constant.BASE_URL+apkUrl, new BaseCallback<String>() {
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
            public void onSuccess(final Response response, String o) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            File dir =new File(Environment.getExternalStorageDirectory(),"/makingFriends");
                            if(!dir.exists()){
                                dir.mkdir();

                            }

                            File file = new File( dir,fileName);
                            Log.d("path",file.getPath());
                            InputStream input = response.body().byteStream();
                            Long allBytes =   response.body().contentLength();
                            FileOutputStream fileOut = new FileOutputStream(file);
                            byte[] bt = new byte[1024];
                            int length;
                            int num = 0;
                            while ((length = input.read(bt)) != -1) {

                                fileOut.write(bt, 0, length);
                                num += length;
                               int progress= (int)(num*100/allBytes);
                                pd.setProgress(progress);

                            }
                            fileOut.flush();
                            fileOut.close();
                            input.close();
                            pd.dismiss();
                            intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

                            startActivity(intent);
                            getActivity().finish();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();









            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });



    }


}
