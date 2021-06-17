package com.cloud.xtilus.makingfriends.bean;

/**
 * Created by cloud on 2016/1/26.
 */
public class Banner extends BaseBean {

    private  String name;
    private  String imgUrl;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


}
