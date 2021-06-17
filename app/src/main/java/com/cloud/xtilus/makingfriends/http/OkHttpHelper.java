package com.cloud.xtilus.makingfriends.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cloud.xtilus.makingfriends.util.Constant;
import com.google.gson.Gson;
import com.hyphenate.util.EMLog;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpHelper {

    public static final String TAG = "OkHttpHelper";

    private static OkHttpHelper mInstance;
    private OkHttpClient mHttpClient;
    private Gson mGson;
    private String resultStr;
    private Handler mHandler;

    static {
        mInstance = new OkHttpHelper();
    }

    private OkHttpHelper() {

        mHttpClient = new OkHttpClient();
        mHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS);

        mHttpClient.newBuilder().readTimeout(10, TimeUnit.SECONDS);
        mHttpClient.newBuilder().writeTimeout(30, TimeUnit.SECONDS);
        mHttpClient.readTimeoutMillis();

        mGson = new Gson();

        mHandler = new Handler(Looper.getMainLooper());

    }

    ;

    public static OkHttpHelper getInstance() {
        return mInstance;
    }


    public void get(String url, BaseCallback callback) {


        Request request = buildGetRequest(url);
        Log.d("url", url);

        request(request, callback);

    }


    public void post(String url, Map<String, Object> param, BaseCallback callback) {

        Request request = buildPostRequest(url, param);
        request(request, callback);
    }


    public void request(final Request request, final BaseCallback callback) {

        callback.onBeforeRequest(request);

        mHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callbackFailure(callback, request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                callbackResponse(callback, response);

                if (response.isSuccessful()) {
                    //  int length= (int) response.body().contentLength();
                    //Log.d("length", "length:" + length);
                    // if(length==-1){
                    resultStr = response.body().string();
                    //  }


                    Log.d(TAG, "result=" + resultStr);

                    if (callback.mType == String.class) {
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {

                            Object obj = mGson.fromJson(resultStr, callback.mType);
                            callbackSuccess(callback, response, obj);
                        } catch (com.google.gson.JsonParseException e) { // Json解析的错误
                            callback.onError(response, response.code(), e);
                        }
                    }
                } else {
                    callbackError(callback, response, null);
                }

            }


        });


    }


    private void callbackSuccess(final BaseCallback callback, final Response response, final Object obj) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });
    }


    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }


    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }


    private void callbackResponse(final BaseCallback callback, final Response response) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }


    private Request buildPostRequest(String url, Map<String, Object> params) {

        return buildRequest(url, HttpMethodType.POST, params);
    }

    private Request buildGetRequest(String url) {

        return buildRequest(url, HttpMethodType.GET, null);
    }

    private Request buildRequest(String url, HttpMethodType methodType, Map<String, Object> params) {


        Request.Builder builder = new Request.Builder()
                .url(url);


        if (methodType == HttpMethodType.POST) {

            RequestBody body = builderFormData(params);

            builder.post(body);
        } else if (methodType == HttpMethodType.GET) {
            builder.get();
        }


        return builder.build();
    }


    private RequestBody builderFormData(Map<String, Object> params) {

        FormBody.Builder bodyBulder = new FormBody.Builder();

        if (Constant.REQEUSR_TYPE_list.equals(String.valueOf(params.get("requestType")))) {

            List<String> userNames  = (List<String>) params.get("userNames");
            assert userNames != null;
            for (String username : userNames) {


                bodyBulder.add("fxids", username);
                EMLog.d("getValue","======------"+username);
            }
            return bodyBulder.build();
        }

        if (Constant.REQEUSR_TYPE.equals(String.valueOf(params.get("requestType")))) {

            List<String> paths = (List<String>) params.get("imageUrl");
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //  MultipartBuilder builders=new MultipartBuilder().type(MultipartBuilder.FORM);
            for (String path : paths) {

                builder.addFormDataPart("files", path, RequestBody.create(MediaType.parse("image/png"), new File(path)));
            }
            return builder.build();
        } else {


            // FormEncodingBuilder builder = new FormEncodingBuilder(
            for (Map.Entry<String, Object> entry : params.entrySet()) {

                bodyBulder.add(entry.getKey(), String.valueOf(entry.getValue()));

            }

            return bodyBulder.build();
        }
    }


    enum HttpMethodType {

        GET,
        POST,

    }


}
