package com.cloud.xtilus.makingfriends.cache;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/10/8.
 */

public class UserWebInfo implements Serializable {

    private  String nick;
    private  String avatar;
    private  String   fxid;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFxid() {
        return fxid;
    }

    public void setFxid(String fxid) {
        this.fxid = fxid;
    }
}