package com.cloud.xtilus.makingfriends.http;

import okhttp3.Request;
import okhttp3.Response;

public abstract class RequestCallback<T> extends BaseCallback<T>{



    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

    }




}
