package com.cloud.xtilus.makingfriends.bean;



/**
 * Created by Administrator on 2016/3/14.
 */
public class ResponseBean<T> {


    private int ret_code;
    private  String ret_msg;
    private T ret_data;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public T getRet_data() {
        return ret_data;
    }

    public void setRet_data(T ret_data) {
        this.ret_data = ret_data;
    }
}