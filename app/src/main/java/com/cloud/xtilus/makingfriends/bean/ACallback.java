package com.cloud.xtilus.makingfriends.bean;

import com.vmloft.develop.library.tools.base.VMCallback;

public class ACallback<T> implements VMCallback<T> {

    @Override
    public void onSuccess(T t) {

    }

    @Override
    public void onError(int code, String desc) {

    }

    @Override
    public void onProgress(int progress, String desc) {

    }
}
