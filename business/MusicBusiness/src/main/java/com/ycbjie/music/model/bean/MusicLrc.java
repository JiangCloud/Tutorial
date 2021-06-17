package com.ycbjie.music.model.bean;

import com.google.gson.annotations.SerializedName;

public class MusicLrc {
    private int result;

    private Lyric data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Lyric getData() {
        return data;
    }

    public void setData(Lyric data) {
        this.data = data;
    }

   /* @SerializedName("lrcContent")
    private String lrcContent;

    public String getLrcContent() {
        return lrcContent;
    }

    public void setLrcContent(String lrcContent) {
        this.lrcContent = lrcContent;
    }*/

    public static class Lyric {

        @SerializedName("lyric")
        private String lyric;
        private int retcode;
        private int code;
        private int subcode;

        public String getLyric() {
            return lyric;
        }

        public void setLyric(String lyric) {
            this.lyric = lyric;
        }

        public int getRetcode() {
            return retcode;
        }

        public void setRetcode(int retcode) {
            this.retcode = retcode;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getSubcode() {
            return subcode;
        }

        public void setSubcode(int subcode) {
            this.subcode = subcode;
        }

        public String getTrans() {
            return trans;
        }

        public void setTrans(String trans) {
            this.trans = trans;
        }

        private String trans;


    }
}
