package com.cloud.xtilus.makingfriends.http;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cloud.xtilus.makingfriends.activity.im.MainActivity;


/**
 * Created by huangfangyi on 2017/3/3.
 * qq 84543217
 */

public class HTClientHelper {
    private static Context applicationContext;

    private static HTClientHelper htClientHelper;

    public static void init(Context context) {
        htClientHelper = new HTClientHelper(context);
    }

    public HTClientHelper(Context context) {
        this.applicationContext = context;
//        HTOptions options=new HTOptions();
//        options.setHost(HTConstant.HOST_IM);
//        options.setOssInfo(HTConstant.endpoint,HTConstant.bucket,HTConstant.accessKeyId,HTConstant.accessKeySecret);
//        options.setSinglePointUrl(HTConstant.DEVICE_URL_UPDATE,HTConstant.DEVICE_URL_GET);
//        options.setDebug(false);
//        options.setKeepAlive(false);

    }

    public static HTClientHelper getInstance() {

        if (htClientHelper == null) {
            throw new RuntimeException("please init first!");
        }
        return htClientHelper;
    }



    /**
     * user has logged into another device
     */
    public void notifyConflict(Context context) {
        Intent intent = new Intent(applicationContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(IMAction.ACTION_CONFLICT, true);
        context.startActivity(intent);
    }

    /**
     * user has logged into another device
     */
    protected void notifyConnection(boolean isConnected) {
        Intent intent = new Intent(IMAction.ACTION_CONNECTION_CHANAGED);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("state", isConnected);
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);
    }


}
