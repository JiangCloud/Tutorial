package com.ycbjie.music.model.bean;

import java.util.List;

public class Album {
    private int result;
    private   AlbumInfo data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public AlbumInfo getData() {
        return data;
    }

    public void setData(AlbumInfo data) {
        this.data = data;
    }

    public static class AlbumInfo {
        private String publishTime;
        private  String company;
        private String  name;
        private String subTitle;
        private  int id ;
        private String mid;
        private String picUrl;
        private String desc;

        private List<Artist> ar;

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<Artist> getAr() {
            return ar;
        }

        public void setAr(List<Artist> ar) {
            this.ar = ar;
        }
    }

    public static class Artist {

        private String id;
        private String name;
        private String mid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }
    }
}
