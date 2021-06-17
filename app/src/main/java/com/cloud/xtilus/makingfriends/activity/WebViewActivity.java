package com.cloud.xtilus.makingfriends.activity;

import android.graphics.PixelFormat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.widget.MytoolBar;
import com.cloud.xtilus.makingfriends.widget.X5WebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.sdk.WebViewClient;

import cn.sharesdk.onekeyshare.OnekeyShare;


public class WebViewActivity extends AppCompatActivity implements View.OnClickListener{
   // @ViewInject(R.id.baseweb_webView)
  //  private WebView webView;
    private ProgressBar pb;

    @ViewInject(R.id.toolBar)
    private MytoolBar mtoolbar;


    /** 视频全屏参数 */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient customViewCallback;

    private X5WebView mWebView;
    private ViewGroup mViewParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }
        setContentView(R.layout.activity_web_view);
        ViewUtils.inject(this);
        initToolBar();
        mViewParent = (ViewGroup) findViewById(R.id.webView1);
        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setMax(100);

        mWebView = new X5WebView(this, null);

        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));



        WebSettings webSettings = mWebView.getSettings();


        webSettings.setAllowFileAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        webSettings.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSettings.setDatabasePath(this.getDir("databases", 0).getPath());
        webSettings.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());

        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);



        mWebView.setWebChromeClient(new WebViewClients());
        mWebView.setWebViewClient(new WebViewClient());

        mWebView.loadUrl("http://193.112.117.238/");
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();

        }

        private class WebViewClients extends WebChromeClient {

            CustomViewCallback callback;
            View myVideoView;
            View myNormalView;
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pb.setProgress(newProgress);
                if(newProgress==100){
                    pb.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }


           // 全屏播放配置

            @Override
            public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
              FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
               viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }
            
            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
              if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }

            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2,
                                     JsResult arg3) {
                /**
                 * 这里写入你自定义的window alert
                 */
                return super.onJsAlert(null, arg1, arg2, arg3);
            }


        }










  //初始化toolBar
   private void  initToolBar(){
       mtoolbar.setRightButtonIcon(R.drawable.icon_back_32px);
       mtoolbar.setNavigationIcon(R.drawable.icon_back_32px);
       mtoolbar.setTitle("cloud");
      // setSupportActionBar(mtoolbar);
       mtoolbar.setNavigationOnClickListener(this);

       mtoolbar.setRightButtonOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showShare();


           }
       });

   }


    @Override
    public void onClick(View v) {

        this.finish();

    }


     private void showShare() {
      //  ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.cniao5.com");
        oks.setTitle(getString(R.string.share));
        //oks.setText();
        oks.setUrl("http://www.cniao5.com");
        // 启动分享GUI
        oks.show(this);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
        if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()){

            mWebView.goBack();

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
