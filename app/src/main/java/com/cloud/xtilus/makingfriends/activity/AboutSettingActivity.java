package com.cloud.xtilus.makingfriends.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.bean.ResponseBean;
import com.cloud.xtilus.makingfriends.bean.VersionInfo;
import com.cloud.xtilus.makingfriends.http.BaseCallback;
import com.cloud.xtilus.makingfriends.http.OkHttpHelper;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.vmloft.develop.library.tools.widget.VMLineView;
import com.vmloft.develop.library.tools.widget.toast.VMToast;
import com.zenglb.downloadinstaller.DownloadInstaller;
import com.zenglb.downloadinstaller.DownloadProgressCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Create by lzan13 on 2019/05/14
 *
 * 设置关于界面
 */
public class AboutSettingActivity extends AppActivity implements EasyPermissions.PermissionCallbacks {


    @BindView(R.id.about_setting_check_new)
    VMLineView  checkNew;
    private Intent intent;
    private ProgressDialog  pd;
    @Override
    protected int layoutId() {
        return R.layout.activity_setting_about;
    }

    @Override
    protected void initUI() {
        super.initUI();
    }

    @Override
    protected void initData() {
        setTopTitle(R.string.about);
        checkNew.setCaption("V"+getClientVersion());
    }

    @OnClick({ R.id.about_setting_check_new })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.about_setting_check_new:
            checkUpdate();
            break;
        }
    }


    private void checkUpdate(){

        OkHttpHelper.getInstance().get(Constant.CHECK_VERSION, new BaseCallback<ResponseBean<List<VersionInfo>>>() {
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
            public void onSuccess(Response response, ResponseBean<List<VersionInfo>> responseBean) {
                //VMToast.make(mActivity, responseBean.getRet_data().getVersion()).done();
                    final String apkUlr=responseBean.getRet_data().get(0).getDownUrl();
                    if (!responseBean.getRet_data().get(0).getVersion().equals(getClientVersion())) {
                        showUpdateDialog("1.升级后App 会自动攒钱\n2.还可以做白日梦",false,apkUlr);

                        methodRequiresPermission();
                     /*   String title = VMStr.byRes(R.string.update_hint_title_);
                        String content = VMStr.byRes(R.string.update_hint_content);
                        String cancel = VMStr.byRes(R.string.im_cancel);
                        String ok = VMStr.byRes(R.string.im_ok);
                        IMDialog.showAlertDialog(mActivity, title, content, cancel, ok, (DialogInterface dialog, int which) -> {

                            downApk(apkUlr);
                        });*/


                    }else{
                        VMToast.make(mActivity, "已是最新版本").done();
                        //Toast.makeText(getBaseContext(), "当前已是最新版本。" , Toast.LENGTH_SHORT).show();
                    }


            }

            @Override
            public void onError(Response response, int code, Exception e) {

                Log.d("getVersion","获取新版本信息异常:===="+e.getMessage());



            }
        });


    }
    /*
     * 获取当前应用的版本号
     *
     */
    private String getClientVersion() {
        PackageInfo packageInfo=new PackageInfo();
        try {
            PackageManager packageManager = getPackageManager();
             packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return packageInfo.versionName;
    }

    /**
     * 显示下载的对话框,是否要强制的升级还是正常的升级
     *
     * @param UpdateMsg     升级信息
     * @param isForceUpdate 是否是强制升级
     * @param downloadUrl   APK 下载URL
     */
    private void showUpdateDialog(String UpdateMsg, boolean isForceUpdate, String downloadUrl) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View updateView = inflater.inflate(R.layout.update_layout, null);
        NumberProgressBar progressBar = updateView.findViewById(R.id.tips_progress);
        TextView updateMsg = updateView.findViewById(R.id.update_mess_txt);
        updateMsg.setText(UpdateMsg);
        builder.setTitle("发现新版本");
        String negativeBtnStr = "以后再说";

        if (isForceUpdate) {
            builder.setTitle("强制升级");
            negativeBtnStr = "退出应用";
        }

        builder.setView(updateView);
        builder.setNegativeButton(negativeBtnStr, null);
        builder.setPositiveButton(R.string.apk_update_yes, null);

        AlertDialog downloadDialog = builder.create();
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.setCancelable(false);
        downloadDialog.show();
        downloadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (isForceUpdate) {
                progressBar.setVisibility(View.VISIBLE);

                //新加 isForceGrantUnKnowSource 参数

                //如果是企业内部应用升级，肯定是要这个权限，其他情况不要太流氓，TOAST 提示
                //这里演示要强制安装
                new DownloadInstaller(this, downloadUrl, true, new DownloadProgressCallBack() {
                    @Override
                    public void downloadProgress(int progress) {
                        runOnUiThread(() -> progressBar.setProgress(progress));
                        if (progress==100){
                            downloadDialog.dismiss();
                        }
                    }

                    @Override
                    public void downloadException(Exception e) {
                    }

                    @Override
                    public void onInstallStart() {
                        downloadDialog.dismiss();
                    }
                }).start();

                //升级按钮变灰色
                downloadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);

            } else {
                new DownloadInstaller(this, downloadUrl).start();
                downloadDialog.dismiss();
            }
        });

        downloadDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            if (isForceUpdate) {
                downloadDialog.dismiss();
                AboutSettingActivity.this.finish();
            } else {
                downloadDialog.dismiss();
            }
        });

    }
    /**
     * 请求权限,创建目录的权限
     */
    private void methodRequiresPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(AboutSettingActivity.this, "App 升级需要储存权限", 10086, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
