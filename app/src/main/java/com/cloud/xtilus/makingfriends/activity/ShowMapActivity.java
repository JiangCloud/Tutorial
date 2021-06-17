package com.cloud.xtilus.makingfriends.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.cloud.xtilus.makingfriends.R;

/**
 * Created by cloud on 2016/2/20.
 */
public class ShowMapActivity extends Activity {

    MapView mapview=null;
   public MyLocationListenner myListener = new MyLocationListenner();
    private BaiduMap mBaiduMap;
    LocationClient mLocClient;

    boolean isFirstLoc = true; // 是否首次定位
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {

        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.map);
        //ViewUtils.inject(this);
        // 注册 SDK 广播监听者
      /*  IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);*/


       MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

       mBaiduMap=  mapview.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        mLocClient= new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }


    /**
     * 定位SDK监听函数
     */
   public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapview == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {


        }
    }
    @Override
    protected void onPause() {

        super.onPause();
        mapview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();

    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
     //   mBaiduMap.setMyLocationEnabled(false);
        mapview.onDestroy();
        mapview = null;
        super.onDestroy();
    }

}
