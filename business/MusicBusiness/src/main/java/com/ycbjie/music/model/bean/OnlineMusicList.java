package com.ycbjie.music.model.bean;

import java.util.List;


public class OnlineMusicList {

    private ResponseData data;
    private int result;

   // private BillboardBean billboard;

   // private List<SongListBean> song_list;
/*
    public BillboardBean getBillboard() {
        return billboard;
    }

    public void setBillboard(BillboardBean billboard) {
        this.billboard = billboard;
    }*/

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public ResponseData getData() {
        return data;
    }

    public void setData(ResponseData data) {
        this.data = data;
    }

  /*  public List<SongListBean> getSong_list() {
        return song_list;
    }

    public void setSong_list(List<SongListBean> song_list) {
        this.song_list = song_list;
    }*/

    public static class BillboardBean {
        /**
         * billboard_type : 2
         * billboard_no : 2614
         * update_date : 2018-08-10
         * billboard_songnum : 1489
         * havemore : 1
         * name : 热歌榜
         * comment : 该榜单是根据千千音乐平台歌曲每周播放量自动生成的数据榜单，统计范围为千千音乐平台上的全部歌曲，每日更新一次
         * pic_s192 : http://business.cdn.qianqian.com/qianqian/pic/bos_client_1452f36a8dc430ccdb8f6e57be6df2ee.jpg
         * pic_s640 : http://business.cdn.qianqian.com/qianqian/pic/bos_client_361aa8612dd9dd8474daf77040f7079d.jpg
         * pic_s444 : http://hiphotos.qianqian.com/ting/pic/item/c83d70cf3bc79f3d98ca8e36b8a1cd11728b2988.jpg
         * pic_s260 : http://hiphotos.qianqian.com/ting/pic/item/838ba61ea8d3fd1f1326c83c324e251f95ca5f8c.jpg
         * pic_s210 : http://business.cdn.qianqian.com/qianqian/pic/bos_client_734232335ef76f5a05179797875817f3.jpg
         * web_url : http://music.baidu.com/top/dayhot
         */

        private String billboard_type;
        private String billboard_no;
        private String update_date;
        private String billboard_songnum;
        private int havemore;
        private String name;
        private String comment;
        private String pic_s192;
        private String pic_s640;
        private String pic_s444;
        private String pic_s260;
        private String pic_s210;
        private String web_url;

        public String getBillboard_type() {
            return billboard_type;
        }

        public void setBillboard_type(String billboard_type) {
            this.billboard_type = billboard_type;
        }

        public String getBillboard_no() {
            return billboard_no;
        }

        public void setBillboard_no(String billboard_no) {
            this.billboard_no = billboard_no;
        }

        public String getUpdate_date() {
            return update_date;
        }

        public void setUpdate_date(String update_date) {
            this.update_date = update_date;
        }

        public String getBillboard_songnum() {
            return billboard_songnum;
        }

        public void setBillboard_songnum(String billboard_songnum) {
            this.billboard_songnum = billboard_songnum;
        }

        public int getHavemore() {
            return havemore;
        }

        public void setHavemore(int havemore) {
            this.havemore = havemore;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getPic_s192() {
            return pic_s192;
        }

        public void setPic_s192(String pic_s192) {
            this.pic_s192 = pic_s192;
        }

        public String getPic_s640() {
            return pic_s640;
        }

        public void setPic_s640(String pic_s640) {
            this.pic_s640 = pic_s640;
        }

        public String getPic_s444() {
            return pic_s444;
        }

        public void setPic_s444(String pic_s444) {
            this.pic_s444 = pic_s444;
        }

        public String getPic_s260() {
            return pic_s260;
        }

        public void setPic_s260(String pic_s260) {
            this.pic_s260 = pic_s260;
        }

        public String getPic_s210() {
            return pic_s210;
        }

        public void setPic_s210(String pic_s210) {
            this.pic_s210 = pic_s210;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }
    }

    public static class SongListBean {
        /**
         * artist_id : 1483
         * language : 国语
         * pic_big : http://qukufile2.qianqian.com/data2/pic/a78755077b72153b3b7c084c9eed4e92/600618023/600618023.jpg@s_1,w_150,h_150
         * pic_small : http://qukufile2.qianqian.com/data2/pic/a78755077b72153b3b7c084c9eed4e92/600618023/600618023.jpg@s_1,w_90,h_90
         * country : 内地
         * area : 0
         * publishtime : 2018-07-12
         * album_no : 7
         * lrclink : http://qukufile2.qianqian.com/data2/lrc/c348fab967943262a05a5e1576c8a898/597857659/597857659.lrc
         * copy_type : 1
         * hot : 230924
         * all_artist_ting_uid : 1557
         * resource_type : 0
         * is_new : 1
         * rank_change : 0
         * rank : 4
         * all_artist_id : 1483
         * style :
         * del_status : 0
         * relate_status : 0
         * toneid : 0
         * all_rate : 96,128,224,320,flac
         * file_duration : 267
         * has_mv_mobile : 0
         * versions :
         * bitrate_fee : {"0":"0|0","1":"0|0"}
         * biaoshi : lossless
         * info :
         * has_filmtv : 0
         * si_proxycompany : 华宇世博音乐文化（北京）有限公司-普通代理
         * res_encryption_flag : 0
         * song_id : 597854185
         * title : 明智之举
         * ting_uid : 1557
         * author : 许嵩
         * album_id : 600618022
         * album_title : 寻宝游戏
         * is_first_publish : 0
         * havehigh : 2
         * charge : 0
         * has_mv : 1
         * learn : 0
         * song_source : web
         * piao_id : 0
         * korean_bb_song : 0
         * resource_type_ext : 0
         * mv_provider : 0000000000
         * artist_name : 许嵩
         * pic_radio : http://qukufile2.qianqian.com/data2/pic/a78755077b72153b3b7c084c9eed4e92/600618023/600618023.jpg@s_1,w_300,h_300
         * pic_s500 : http://qukufile2.qianqian.com/data2/pic/a78755077b72153b3b7c084c9eed4e92/600618023/600618023.jpg@s_1,w_500,h_500
         * pic_premium : http://qukufile2.qianqian.com/data2/pic/a78755077b72153b3b7c084c9eed4e92/600618023/600618023.jpg@s_1,w_500,h_500
         * pic_huge : http://qukufile2.qianqian.com/data2/pic/a78755077b72153b3b7c084c9eed4e92/600618023/600618023.jpg@s_1,w_1000,h_1000
         * album_500_500 : http://qukufile2.qianqian.com/data2/pic/a78755077b72153b3b7c084c9eed4e92/600618023/600618023.jpg@s_1,w_500,h_500
         * album_800_800 :
         * album_1000_1000 : http://qukufile2.qianqian.com/data2/pic/a78755077b72153b3b7c084c9eed4e92/600618023/600618023.jpg@s_1,w_1000,h_1000
         */


         private  int rank;
         private  int rankType;
         private String rankValue;
         private int recType;
         private int songId;
         private String vid;
         private String albumMid;//专辑id
         private String title;//歌名
         private String singerName;//歌手
         private String singerMid;///歌手id
         private  int songType;
         private int uuidCnt;
         private String cover;
         private int id;
         private int type;
         private String mid;
         private String name;
         private String subtitle;
         private List<Singer> singer;
         private Album album;
         private Mv mv;

         private int interval;
         private int isonly;
         private int language;
         private int genre;
         private  int index_cd;
         private int index_album;
         private String time_public;
         private int status;
         private int fnote;

        /* private List<MusicFile> file;
         private List<Pay> pay;
         private  List<MusicAction> action;
         private List<Ksong> ksong;

         private List<Volume> volume;
*/
         private String label;
         private String url;
         private  int bpm;
         private int version;
         private String trace;
         private int data_type;
         private int modify_stamp;
         private String pingpong;
         private int aid;
         private String ppurl;
         private int tid;
         private int ov;
         private int sa;
         private String es;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public List<Singer> getSinger() {
            return singer;
        }

        public void setSinger(List<Singer> singer) {
            this.singer = singer;
        }

        public Album getAlbum() {
            return album;
        }

        public void setAlbum(Album album) {
            this.album = album;
        }

        public Mv getMv() {
            return mv;
        }

        public void setMv(Mv mv) {
            this.mv = mv;
        }

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public int getIsonly() {
            return isonly;
        }

        public void setIsonly(int isonly) {
            this.isonly = isonly;
        }

        public int getLanguage() {
            return language;
        }

        public void setLanguage(int language) {
            this.language = language;
        }

        public int getGenre() {
            return genre;
        }

        public void setGenre(int genre) {
            this.genre = genre;
        }

        public int getIndex_cd() {
            return index_cd;
        }

        public void setIndex_cd(int index_cd) {
            this.index_cd = index_cd;
        }

        public int getIndex_album() {
            return index_album;
        }

        public void setIndex_album(int index_album) {
            this.index_album = index_album;
        }

        public String getTime_public() {
            return time_public;
        }

        public void setTime_public(String time_public) {
            this.time_public = time_public;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getFnote() {
            return fnote;
        }

        public void setFnote(int fnote) {
            this.fnote = fnote;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getBpm() {
            return bpm;
        }

        public void setBpm(int bpm) {
            this.bpm = bpm;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getTrace() {
            return trace;
        }

        public void setTrace(String trace) {
            this.trace = trace;
        }

        public int getData_type() {
            return data_type;
        }

        public void setData_type(int data_type) {
            this.data_type = data_type;
        }

        public int getModify_stamp() {
            return modify_stamp;
        }

        public void setModify_stamp(int modify_stamp) {
            this.modify_stamp = modify_stamp;
        }

        public String getPingpong() {
            return pingpong;
        }

        public void setPingpong(String pingpong) {
            this.pingpong = pingpong;
        }

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public String getPpurl() {
            return ppurl;
        }

        public void setPpurl(String ppurl) {
            this.ppurl = ppurl;
        }

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public int getOv() {
            return ov;
        }

        public void setOv(int ov) {
            this.ov = ov;
        }

        public int getSa() {
            return sa;
        }

        public void setSa(int sa) {
            this.sa = sa;
        }

        public String getEs() {
            return es;
        }

        public void setEs(String es) {
            this.es = es;
        }
/*
        private String artist_id;
        private String language;
        private String pic_big;
        private String pic_small;
        private String country;
        private String area;
        private String publishtime;
        private String album_no;
        private String lrclink;
        private String copy_type;
        private String hot;
        private String all_artist_ting_uid;
        private String resource_type;
        private String is_new;
        private String rank_change;
        private String all_artist_id;
        private String style;
        private String del_status;
        private String relate_status;
        private String toneid;
        private String all_rate;
        private int file_duration;
        private int has_mv_mobile;
        private String versions;
        private String bitrate_fee;
        private String biaoshi;
        private String info;
        private String has_filmtv;
        private String si_proxycompany;
        private String res_encryption_flag;
        private String song_id;
        private String ting_uid;
        private String author;
        private String album_id;
        private String album_title;
        private int is_first_publish;
        private int havehigh;
        private int charge;
        private int has_mv;
        private int learn;
        private String song_source;
        private String piao_id;
        private String korean_bb_song;
        private String resource_type_ext;
        private String mv_provider;
        private String artist_name;
        private String pic_radio;
        private String pic_s500;
        private String pic_premium;
        private String pic_huge;
        private String album_500_500;
        private String album_800_800;
        private String album_1000_1000;*/


        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getRankType() {
            return rankType;
        }

        public void setRankType(int rankType) {
            this.rankType = rankType;
        }

        public String getRankValue() {
            return rankValue;
        }

        public void setRankValue(String rankValue) {
            this.rankValue = rankValue;
        }

        public int getRecType() {
            return recType;
        }

        public void setRecType(int recType) {
            this.recType = recType;
        }

        public int getSongId() {
            return songId;
        }

        public void setSongId(int songId) {
            this.songId = songId;
        }

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getAlbumMid() {
            return albumMid;
        }

        public void setAlbumMid(String albumMid) {
            this.albumMid = albumMid;
        }

        public String getSingerName() {
            return singerName;
        }

        public void setSingerName(String singerName) {
            this.singerName = singerName;
        }

        public String getSingerMid() {
            return singerMid;
        }

        public void setSingerMid(String singerMid) {
            this.singerMid = singerMid;
        }

        public int getSongType() {
            return songType;
        }

        public void setSongType(int songType) {
            this.songType = songType;
        }

        public int getUuidCnt() {
            return uuidCnt;
        }

        public void setUuidCnt(int uuidCnt) {
            this.uuidCnt = uuidCnt;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public static class Album {
            private int id;
            private String mid;
            private String name;
            private String title;
            private String subtitle;
            private String time_public;
            private String pmid;

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            public String getTime_public() {
                return time_public;
            }

            public void setTime_public(String time_public) {
                this.time_public = time_public;
            }

            public String getPmid() {
                return pmid;
            }

            public void setPmid(String pmid) {
                this.pmid = pmid;
            }



        }

    /*    public String getArtist_id() {
            return artist_id;
        }

        public void setArtist_id(String artist_id) {
            this.artist_id = artist_id;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getPic_big() {
            return pic_big;
        }

        public void setPic_big(String pic_big) {
            this.pic_big = pic_big;
        }

        public String getPic_small() {
            return pic_small;
        }

        public void setPic_small(String pic_small) {
            this.pic_small = pic_small;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getPublishtime() {
            return publishtime;
        }

        public void setPublishtime(String publishtime) {
            this.publishtime = publishtime;
        }

        public String getAlbum_no() {
            return album_no;
        }

        public void setAlbum_no(String album_no) {
            this.album_no = album_no;
        }

        public String getLrclink() {
            return lrclink;
        }

        public void setLrclink(String lrclink) {
            this.lrclink = lrclink;
        }

        public String getCopy_type() {
            return copy_type;
        }

        public void setCopy_type(String copy_type) {
            this.copy_type = copy_type;
        }

        public String getHot() {
            return hot;
        }

        public void setHot(String hot) {
            this.hot = hot;
        }

        public String getAll_artist_ting_uid() {
            return all_artist_ting_uid;
        }

        public void setAll_artist_ting_uid(String all_artist_ting_uid) {
            this.all_artist_ting_uid = all_artist_ting_uid;
        }

        public String getResource_type() {
            return resource_type;
        }

        public void setResource_type(String resource_type) {
            this.resource_type = resource_type;
        }

        public String getIs_new() {
            return is_new;
        }

        public void setIs_new(String is_new) {
            this.is_new = is_new;
        }

        public String getRank_change() {
            return rank_change;
        }

        public void setRank_change(String rank_change) {
            this.rank_change = rank_change;
        }



        public String getAll_artist_id() {
            return all_artist_id;
        }

        public void setAll_artist_id(String all_artist_id) {
            this.all_artist_id = all_artist_id;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getDel_status() {
            return del_status;
        }

        public void setDel_status(String del_status) {
            this.del_status = del_status;
        }

        public String getRelate_status() {
            return relate_status;
        }

        public void setRelate_status(String relate_status) {
            this.relate_status = relate_status;
        }

        public String getToneid() {
            return toneid;
        }

        public void setToneid(String toneid) {
            this.toneid = toneid;
        }

        public String getAll_rate() {
            return all_rate;
        }

        public void setAll_rate(String all_rate) {
            this.all_rate = all_rate;
        }

        public int getFile_duration() {
            return file_duration;
        }

        public void setFile_duration(int file_duration) {
            this.file_duration = file_duration;
        }

        public int getHas_mv_mobile() {
            return has_mv_mobile;
        }

        public void setHas_mv_mobile(int has_mv_mobile) {
            this.has_mv_mobile = has_mv_mobile;
        }

        public String getVersions() {
            return versions;
        }

        public void setVersions(String versions) {
            this.versions = versions;
        }

        public String getBitrate_fee() {
            return bitrate_fee;
        }

        public void setBitrate_fee(String bitrate_fee) {
            this.bitrate_fee = bitrate_fee;
        }

        public String getBiaoshi() {
            return biaoshi;
        }

        public void setBiaoshi(String biaoshi) {
            this.biaoshi = biaoshi;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getHas_filmtv() {
            return has_filmtv;
        }

        public void setHas_filmtv(String has_filmtv) {
            this.has_filmtv = has_filmtv;
        }

        public String getSi_proxycompany() {
            return si_proxycompany;
        }

        public void setSi_proxycompany(String si_proxycompany) {
            this.si_proxycompany = si_proxycompany;
        }

        public String getRes_encryption_flag() {
            return res_encryption_flag;
        }

        public void setRes_encryption_flag(String res_encryption_flag) {
            this.res_encryption_flag = res_encryption_flag;
        }

        public String getSong_id() {
            return song_id;
        }

        public void setSong_id(String song_id) {
            this.song_id = song_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTing_uid() {
            return ting_uid;
        }

        public void setTing_uid(String ting_uid) {
            this.ting_uid = ting_uid;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(String album_id) {
            this.album_id = album_id;
        }

        public String getAlbum_title() {
            return album_title;
        }

        public void setAlbum_title(String album_title) {
            this.album_title = album_title;
        }

        public int getIs_first_publish() {
            return is_first_publish;
        }

        public void setIs_first_publish(int is_first_publish) {
            this.is_first_publish = is_first_publish;
        }

        public int getHavehigh() {
            return havehigh;
        }

        public void setHavehigh(int havehigh) {
            this.havehigh = havehigh;
        }

        public int getCharge() {
            return charge;
        }

        public void setCharge(int charge) {
            this.charge = charge;
        }

        public int getHas_mv() {
            return has_mv;
        }

        public void setHas_mv(int has_mv) {
            this.has_mv = has_mv;
        }

        public int getLearn() {
            return learn;
        }

        public void setLearn(int learn) {
            this.learn = learn;
        }

        public String getSong_source() {
            return song_source;
        }

        public void setSong_source(String song_source) {
            this.song_source = song_source;
        }

        public String getPiao_id() {
            return piao_id;
        }

        public void setPiao_id(String piao_id) {
            this.piao_id = piao_id;
        }

        public String getKorean_bb_song() {
            return korean_bb_song;
        }

        public void setKorean_bb_song(String korean_bb_song) {
            this.korean_bb_song = korean_bb_song;
        }

        public String getResource_type_ext() {
            return resource_type_ext;
        }

        public void setResource_type_ext(String resource_type_ext) {
            this.resource_type_ext = resource_type_ext;
        }

        public String getMv_provider() {
            return mv_provider;
        }

        public void setMv_provider(String mv_provider) {
            this.mv_provider = mv_provider;
        }

        public String getArtist_name() {
            return artist_name;
        }

        public void setArtist_name(String artist_name) {
            this.artist_name = artist_name;
        }

        public String getPic_radio() {
            return pic_radio;
        }

        public void setPic_radio(String pic_radio) {
            this.pic_radio = pic_radio;
        }

        public String getPic_s500() {
            return pic_s500;
        }

        public void setPic_s500(String pic_s500) {
            this.pic_s500 = pic_s500;
        }

        public String getPic_premium() {
            return pic_premium;
        }

        public void setPic_premium(String pic_premium) {
            this.pic_premium = pic_premium;
        }

        public String getPic_huge() {
            return pic_huge;
        }

        public void setPic_huge(String pic_huge) {
            this.pic_huge = pic_huge;
        }

        public String getAlbum_500_500() {
            return album_500_500;
        }

        public void setAlbum_500_500(String album_500_500) {
            this.album_500_500 = album_500_500;
        }

        public String getAlbum_800_800() {
            return album_800_800;
        }

        public void setAlbum_800_800(String album_800_800) {
            this.album_800_800 = album_800_800;
        }

        public String getAlbum_1000_1000() {
            return album_1000_1000;
        }

        public void setAlbum_1000_1000(String album_1000_1000) {
            this.album_1000_1000 = album_1000_1000;
        }*/
    }



    public static class ResponseData{

        private List<SongListBean> list;
        private  SongListInfo info;

        private int total;
        private int  listenNum;
        private int id;
        private String period;
        private String update;
        private int pageNo;
        private int pageSize;

        public List<SongListBean> getList() {
            return list;
        }

        public void setList(List<SongListBean> list) {
            this.list = list;
        }

        public SongListInfo getInfo() {
            return info;
        }

        public void setInfo(SongListInfo info) {
            this.info = info;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getListenNum() {
            return listenNum;
        }

        public void setListenNum(int listenNum) {
            this.listenNum = listenNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
/* private String title;
        private List<RankingList>  list;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<RankingList> getList() {
            return list;
        }

        public void setList(List<RankingList> list) {
            this.list = list;
        }
*/

    }

    public static class Singer{

        private int id ;
        private String  mid;
        private String name;
        private String title;
        private int type;
        private int uin;
        private String pmid;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUin() {
            return uin;
        }

        public void setUin(int uin) {
            this.uin = uin;
        }

        public String getPmid() {
            return pmid;
        }

        public void setPmid(String pmid) {
            this.pmid = pmid;
        }
    }


    public static class SongListInfo{
        private String title;
        private String subTitle;
        private String titleDetail;
        private String picUrl;
        private String desc;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getTitleDetail() {
            return titleDetail;
        }

        public void setTitleDetail(String titleDetail) {
            this.titleDetail = titleDetail;
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
    }

    public static class Mv {

        private int id;
        private String  vid;
        private String name;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getVt() {
            return vt;
        }

        public void setVt(int vt) {
            this.vt = vt;
        }

        private int vt;


    }



    private static class Pay {
        private  int pay_month;
        private int   price_track;
        private int   price_album;
        private int   pay_play;
        private int   pay_down;
        private int   pay_status;
        private int   time_free;

        public int getPay_month() {
            return pay_month;
        }

        public void setPay_month(int pay_month) {
            this.pay_month = pay_month;
        }

        public int getPrice_track() {
            return price_track;
        }

        public void setPrice_track(int price_track) {
            this.price_track = price_track;
        }

        public int getPrice_album() {
            return price_album;
        }

        public void setPrice_album(int price_album) {
            this.price_album = price_album;
        }

        public int getPay_play() {
            return pay_play;
        }

        public void setPay_play(int pay_play) {
            this.pay_play = pay_play;
        }

        public int getPay_down() {
            return pay_down;
        }

        public void setPay_down(int pay_down) {
            this.pay_down = pay_down;
        }

        public int getPay_status() {
            return pay_status;
        }

        public void setPay_status(int pay_status) {
            this.pay_status = pay_status;
        }

        public int getTime_free() {
            return time_free;
        }

        public void setTime_free(int time_free) {
            this.time_free = time_free;
        }
    }

}
