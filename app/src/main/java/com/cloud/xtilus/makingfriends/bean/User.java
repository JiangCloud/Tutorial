package com.cloud.xtilus.makingfriends.bean;

/**
 * Created by cloud on 2016/3/14.
 */
public class User  extends BaseBean{
    private String nickname;
    private  String username;
    private int age;
    private String makingfriendWord;
    private String headPicUrl;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMakingfriendWord() {
        return makingfriendWord;
    }

    public void setMakingfriendWord(String makingfriendWord) {
        this.makingfriendWord = makingfriendWord;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
