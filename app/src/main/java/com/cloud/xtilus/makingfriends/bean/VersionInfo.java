package com.cloud.xtilus.makingfriends.bean;

/**
 * Created by cloud on 2016/4/20.
 */
public class VersionInfo extends BaseBean {
    //app版本号
    private String version;
    private String  downUrl;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownUrl() {
        return downUrl;
    }



    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }
}
