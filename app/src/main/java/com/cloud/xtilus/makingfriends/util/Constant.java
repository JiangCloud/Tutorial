package com.cloud.xtilus.makingfriends.util;

import android.os.Environment;

import com.hyphenate.easeui.constants.EaseConstant;


public class Constant implements EaseConstant {



    public static final String BASE_URL = "http://193.112.117.238:8080/";
    public static final String baseImgUrl = "http://showshop.oss-cn-hangzhou.aliyuncs.com/";
   // public static final String BASE_URL = "http://192.168.42.37:8080/MakingFriend/";
    public static final String URL_LOGIN = BASE_URL + "user/phonelogin/";//登录
    public static final String URL_AVATAR = BASE_URL + "upload/";
    public static final String URL_Get_UserInfo = BASE_URL + "user/getUserInfo/";//获取详情

    public static final String URL_FIND_UserInfo = BASE_URL + "user/findUserInfo/";//获取用户头像 呢称

    public static final String URL_FIND_UserInfos = BASE_URL + "user/findUserInfos/";//获取用户头像 呢称

    public static final String URL_GET_UserInfo = BASE_URL + "user/getUserInfoByQueryString/";//获取用户信息

    public static final String URL_UPDATE_updateGroupName = BASE_URL + "group/updateGroupName/";//更新群的名称

    //    朋友圈接口
//     服务器端
    public static final String URL_PUBLISH = BASE_URL + "saveMonentInfo";//发布动态
    public static final String URL_SOCIAL = BASE_URL + "getMonentInfo";//获取动态列表
    public static final String URL_SOCIAL_DELETE = BASE_URL + "removeTimeline";//删除动态
    public static final String URL_SOCIAL_FRIEND = BASE_URL + "fetchOtherTimeline";//获取好友朋友圈列表
    public static final String URL_SOCIAL_COMMENT = BASE_URL + "commentTimeline";//朋友圈动态评论
    public static final String URL_SOCIAL_DELETE_COMMENT = BASE_URL + "deleteCommentTimeline";//删除朋友圈动态评论
    public static final String URL_SOCIAL_REPLY_COMMENT = BASE_URL + "replyCommentTimeline";//回复朋友圈动态评论
    public static final String URL_SOCIAL_DELETE_REPLY_COMMENT = BASE_URL + "deleteReplyCommentTimeline";//删除朋友圈动态评论回复
    public static final String URL_SOCIAL_GOOD = BASE_URL + "praiseTimeline";//点赞
    public static final String URL_SOCIAL_GOOD_CANCEL = BASE_URL + "deletePraiseTimeline";//取消点赞
    public static final String URL_SOCIAL_GET_PRAISELIST = BASE_URL + "fetchTimelineParises";//获取赞列表
    public static final String URL_SOCIAL_GET_COMMENTLIST = BASE_URL + "fetchTimelineComments";//获取评论列表
    public static final String URL_SOCIAL_GET_DETAIL = BASE_URL + "dynamicInfo";//获取评论列表
    public static final String JSON_KEY_AVATAR = "avatar";
    public static final String JSON_KEY_SESSION = "session";
    public static final String JSON_KEY_HXID = "hxid";
    public static final String JSON_KEY_NICK = "nick";
    public static final String baseImgUrl_set = "?x-oss-process=image/resize,m_fill,h_300,w_300";
    public static final String  BUCKET_NAME="showshop";
 //进入用户详情页传递json字符串
    public static final String KEY_USER_INFO = "userInfo";
    public static final String URL_UPLOAD_MOMENT_BACKGROUND = BASE_URL + "uploadpic";//上传朋友圈背景图片







 public static final String JSON_KEY_FXID = "fxid";
 public static final String JSON_KEY_SEX = "sex";
 public static final String JSON_KEY_CITY = "city";
 public static final String JSON_KEY_PROVINCE = "province";
 public static final String JSON_KEY_TEL = "tel";
 public static final String JSON_KEY_SIGN = "userSig";









    public static final  String AccessKeyId="9jYmzhfnjOEl8SUc";

    public static final  String SecretKeyId="GExyP3S9zUY0xPVSaL3HiJPLVgygKI";
    public static final int SDK_APPID = 1400007211;
    //1400006562;
    //	public static final int SDK_APPID = 1400004492;
    //	public static final int ACCOUNT_TYPE = 692;
    public static final String myDEMO_NEW_MESSAGE = "com.example.mydemo.new_message";



    public static final String Get_UWERINFO = BASE_URL + "user/getUserInfo/";
    public static final String Get_LatteryInfo = "http://39.108.148.167/LotteryTicket/showTicketInfo";

    public static final String Get_HeadPic = BASE_URL + "user/getHeadPicUrl/";
    public static final String UPLOAD_PIC = BASE_URL + "user/uploadPic/";
    public static final String CHECK_VERSION = BASE_URL + "userSetting/getVersion/";
    public static final String DEFAULT_USERNAME = "cloud";
    public static final String DEFAULT_PASSWORD = "cloud888";
    public static final String USER_REGISTER_URL = BASE_URL + "user/userRegister/";
    public final static String BASE_URL_LIST = "http://203.195.198.121/index.php/Home/List/";
    //public static String TH_IMG_CACHE_DIR  = MakingFriendApplication.getInstance().getFilesDir().getAbsolutePath() + "/TH_IMG/";
    //	public static String ORG_IMG_CACHE_DIR  = MakingFriendApplication.getInstance().getFilesDir().getAbsolutePath() + "/ORG_IMG/";
    public static String FILE_DIR = Environment.getExternalStorageDirectory() + "/tencent/IMSDK_DEMO/FILE/";
    public static String IMAG_DIR = Environment.getExternalStorageDirectory() + "/tencent/IMSDK_DEMO/IMG/";
    public static String VEDIO_DIR = Environment.getExternalStorageDirectory() + "/tencent/IMSDK_DEMO/VEDIO/";
    public static final int THUMB_IMG_MAX_HEIGHT = 198;
    public static final int THUMB_IMG_MAX_WIDTH = 66;
    public static final String REQEUSR_TYPE = "image_upload";
    public static final String REQEUSR_TYPE_list = "list";




    //环信
    public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
    public static final String GROUP_USERNAME = "item_groups";
    public static final String CHAT_ROOM = "item_chatroom";
    public static final String ACCOUNT_REMOVED = "account_removed";
    public static final String ACCOUNT_CONFLICT = "conflict";
    public static final String ACCOUNT_FORBIDDEN = "user_forbidden";
    public static final String ACCOUNT_KICKED_BY_CHANGE_PASSWORD = "kicked_by_change_password";
    public static final String ACCOUNT_KICKED_BY_OTHER_DEVICE = "kicked_by_another_device";
    public static final String CHAT_ROBOT = "item_robots";
    public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
    public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
    public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";
    public static final String EXTRA_CONFERENCE_ID = "confId";
    public static final String EXTRA_CONFERENCE_PASS = "password";
    public static final String EXTRA_CONFERENCE_IS_CREATOR = "is_creator";
    public static final String MSG_ATTR_CONF_ID = "conferenceId";
    public static final String MSG_ATTR_CONF_PASS = EXTRA_CONFERENCE_PASS;
    public static final String MSG_ATTR_EXTENSION = "msg_extension";
    public static final String EXTRA_CONFERENCE_INVITER = "inviter";
    public static final String EXTRA_CONFERENCE_GROUP_ID = "group_id";
}
