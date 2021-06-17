package com.cloud.xtilus.makingfriends.bean;

import android.content.Context;
import android.util.Log;

import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.db.DemoDBManager;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by huangfangyi on 2017/7/23.
 *
 */

public class MomentsMessageDao {

    public static final String TABLE_NAME = "moments_notice";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_USERID = "userid";
    public static final String COLUMN_NAME_USERNICK = "usernick";
    public static final String COLUMN_NAME_AVATAR = "avatar";
    public static final String COLUMN_NAME_TIME= "time";
    public static final String COLUMN_NAME_CONTENT = "content";
    public static final String COLUMN_NAME_IMAGEURL = "image_url";
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_NAME_MOMENTS_ID= "moments_id";
    public static final String COLUMN_NAME_STATUS= "status";


    public MomentsMessageDao(Context context){

    }

    public void sendMomentsCmd(String imageUrl,String momentsId,String content,int type,String sendTo){
        if(sendTo.equals(MakingFriendApplication.getInstance().getUsername())){
            return;

        }
        JSONObject data=new JSONObject();

        try {

            data.put("userId", MakingFriendApplication.getInstance().getUsername());

            data.put("nickname",MakingFriendApplication.getInstance().getUserJson().getString(Constant.JSON_KEY_NICK));
            data.put("avatar",MakingFriendApplication.getInstance().getUserJson().getString(Constant.JSON_KEY_AVATAR));
            data.put("imageUrl",imageUrl);
            if(content==null){
                content="not a comment";
            }
            data.put("content",content);
            data.put("type",type);
            data.put("mid",momentsId);

            JSONObject jsonObject=new JSONObject();


            jsonObject.put("data",data);
           //jsonObject.put("action",7000);


             EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

        //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
        //cmdMsg.setChatType(EMMessage.ChatType.GroupChat)

            String action="7000";//action可以自定义
            cmdMsg.setChatType(EMMessage.ChatType.Chat);
            EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);

            cmdMsg.setTo(sendTo);
            cmdMsg.addBody(cmdBody);
            cmdMsg.setAttribute("data",jsonObject.toString());
            cmdMsg.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d("cmdMsg","cmdMsg sent to susess====================");

            }

            @Override
            public void onError(int i, String s) {
                Log.d("sendMomentsCmd----->","onFailure");

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

        EMClient.getInstance().chatManager().sendMessage(cmdMsg);

        } catch (JSONException e) {
            e.printStackTrace();
        }

     }


    public void savaMomentsMessage(MomentsMessage momentsMessage){
        DemoDBManager.getInstance().saveMomentsNotice(momentsMessage);
    }


    public int getUnreadMoments(){

        return  DemoDBManager.getInstance().getMomentsUnReadCount();
    }

    public MomentsMessage getLastMomentsMessage(){

        return  DemoDBManager.getInstance().getLastMomentsMessage();
    }


    public List<MomentsMessage>  getMomentsMessageList(){

        return  DemoDBManager.getInstance().getMomentsMessageList();
    }

    public void clearMomentsUnread(){

        DemoDBManager.getInstance().clearMomentsUnReadCount();
    }

    public void deleteAllMomentsMessage(){

        DemoDBManager.getInstance().deleteAllMomentsMessage();
    }
}
