package com.cloud.xtilus.makingfriends.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.baidu.mapapi.SDKInitializer;
import com.cloud.xtilus.makingfriends.BuildConfig;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.common.interfaceOrImplement.UserActivityLifecycleCallbacks;
import com.cloud.xtilus.makingfriends.im.AIMManager;
import com.cloud.xtilus.makingfriends.manager.ContactsManager;
import com.cloud.xtilus.makingfriends.manager.LocalUserManager;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.util.EMLog;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;
import com.vmloft.develop.library.tools.VMTools;
import com.vmloft.develop.library.tools.utils.VMLog;
import com.ycbjie.library.base.app.LibApplication;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/2/6.
 */
public class MakingFriendApplication extends LibApplication implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "processAppName";

    public static Context appContext;
    private static MakingFriendApplication instance;
    private boolean isBackground;
    private UserActivityLifecycleCallbacks mLifecycleCallbacks = new UserActivityLifecycleCallbacks();
    private String userName = null;
    private String userSig = null;
    private String identifier = null;
    public boolean bHostRelaytionShip = true;
    public Activity currentActivity = null;
    private boolean bThirdIdLogin = false;
    private boolean isLoging = false;
    private List<Activity> activities = new ArrayList<>();
    private JSONObject userJson = null;
    private OSS oss;
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();

        appContext = this;
        instance=this;
        // SMSSDK.initSDK(this, "f5c4118a0737", "27b701ba70ddb0db4ece3b1d5c5132c3",true);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        DemoHelper.getInstance().init(appContext);
        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        SDKInitializer.initialize(this);
        //聊天初始化

        //创建默认的ImageLoader配置参数
        initImageLoader(getApplicationContext());
        LocalUserManager.init(this);

        ContactsManager.init(this);
        Fresco.initialize(this);

        VMTools.init(this, BuildConfig.isDebug?VMLog.Level.VERBOSE : VMLog.Level.ERROR);
        // 初始化 IM
        AIMManager.getInstance().initIM(this);
        //init demo helper
       initOSSClient(this);

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);

   /*     int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
     // 如果APP启用了远程的service，此application:onCreate会被调用2次
     // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
     // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EMOptions options = new EMOptions();
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
       // options.setAutoDownloadThumbnail(true);

        //EMClient.getInstance().init(this, options);

        EaseUI.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
     //   EMClient.getInstance().setDebugMode(true);

*/
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();

        StrictMode.setVmPolicy(builder.build());

        builder.detectFileUriExposure();
    }

    public  static MakingFriendApplication  getInstance() {
        return instance;
    }



    private void registerActivityLifecycleCallbacks() {
        this.registerActivityLifecycleCallbacks(mLifecycleCallbacks);
    }



    public UserActivityLifecycleCallbacks getLifecycleCallbacks() {
        return mLifecycleCallbacks;
    }

    public void setThirdIdLogin(boolean bThirdId) {
        bThirdIdLogin = bThirdId;
    }

    public boolean getThirdIdLogin() {
        return bThirdIdLogin;
    }


    public boolean isLoging() {
        return isLoging;
    }

    public void setIsLoging(boolean isLoging) {
        this.isLoging = isLoging;
    }




    public void saveActivity(Activity activity) {
        if (activity != null) {
            activities.add(activity);
        }

    }

    public void finishActivities() {
        for (Activity activity : activities) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }

    }

    public void setUserJson(JSONObject userJson) {
        this.userJson = userJson;

        LocalUserManager.getInstance().setUserJson(userJson);
    }

    public JSONObject getUserJson() {
        if (userJson == null) {
            userJson = LocalUserManager.getInstance().getUserJson();
        }
        return userJson;
    }
    public String getUsername() {
        String username = null;
        if (getUserJson() != null) {
            username = getUserJson().getString(Constant.JSON_KEY_HXID);
        }
        return username;
    }

    public OSS getOss() {
        return oss;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }


    //创建默认的ImageLoader配置参数
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "makingFriends/image/Cache");
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽

                .threadPoolSize(3) //线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                // .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                //.memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                //.diskCacheSize(50 * 1024 * 1024)  // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO) // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);


    }
    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    public Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        return loadingDialog;
    }

    protected void initOSSClient( Context context) {
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com/images/momentImage/";

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // connction time out default 15s
        conf.setSocketTimeout(15 * 1000); // socket timeout，default 15s
        conf.setMaxConcurrentRequest(5); // synchronous request number，default 5
        conf.setMaxErrorRetry(2); // retry，default 2
        OSSLog.enableLog(); //write local log file ,path is SDCard_path\OSSLog\logs.csv
      //  OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("9jYmzhfnjOEl8SUc",
            //    "GExyP3S9zUY0xPVSaL3HiJPLVgygKI");
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.AccessKeyId,
                Constant.SecretKeyId);


        // OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider("<StsToken.AccessKeyId>", "<StsToken.SecretKeyId>", "<StsToken.SecurityToken>");

        oss = new OSSClient(context, endpoint, credentialProvider, conf);






    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this) ;
    }
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }


    /**
     * 为了兼容5.0以下使用vector图标
     */
    static {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
    }


    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        EMLog.e("demoApp", e.getMessage());
    }

    /**
     * 解决androidP 第一次打开程序出现莫名弹窗
     * 弹窗内容“detected problems with api ”
     */
    private void closeAndroidPDialog(){
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
            try {
                Class aClass = Class.forName("android.content.pm.PackageParser$Package");
                Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
                declaredConstructor.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Class cls = Class.forName("android.app.ActivityThread");
                Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
                declaredMethod.setAccessible(true);
                Object activityThread = declaredMethod.invoke(null);
                Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
                mHiddenApiWarningShown.setAccessible(true);
                mHiddenApiWarningShown.setBoolean(activityThread, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
