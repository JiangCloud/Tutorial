package com.cloud.xtilus.makingfriends.bean;

/**
 * Created by Administrator on 2016/1/25.
 */
public class Tab {
    private int title;
    private Class fragmet;
    private int icon;

    public int getTitle() {
        return title;
    }

    public Class getFragmet() {
        return fragmet;
    }

    public void setFragmet(Class fragmet) {
        this.fragmet = fragmet;
    }

    public void setTitle(int title) {
        this.title = title;

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Tab(int title, Class fragmet, int icon) {
        this.title = title;
        this.fragmet = fragmet;
        this.icon = icon;
    }
}
