package com.ycbjie.music.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SearchMusic {

    private int result;

    private Song data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Song getData() {
        return data;
    }

    public void setData(Song data) {
        this.data = data;
    }
/*@SerializedName("song")
    private List<Song> song;
  public void setSong(List<Song> song) {
        this.song = song;
    }

    public List<Song> getSong() {
        return song;
    }*/


    public static class Song {
        @SerializedName("songname")
        private String songname;
        @SerializedName("singer")
        private List<Singer> singer;

        @SerializedName("songid")
        private String songid;
        @SerializedName("songmid")
        private String songmid;


        public List<Singer> getSinger() {
            return singer;
        }

        public void setSinger(List<Singer> singer) {
            this.singer = singer;
        }

        public String getSongmid() {
            return songmid;
        }

        public void setSongmid(String songmid) {
            this.songmid = songmid;
        }

        public String getSongname() {
            return songname;
        }

        public void setSongname(String songname) {
            this.songname = songname;
        }



        public String getSongid() {
            return songid;
        }

        public void setSongid(String songid) {
            this.songid = songid;
        }
    }

    public static class Singer {
        @SerializedName("id")
        private  int id;
        @SerializedName("mid")
        private  String  mid;

        @SerializedName("name")
        private  String name;
        @SerializedName("name_hilight")
        private  String name_hilight;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_hilight() {
            return name_hilight;
        }

        public void setName_hilight(String name_hilight) {
            this.name_hilight = name_hilight;
        }
    }
}
