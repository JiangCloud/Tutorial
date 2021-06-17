package com.cloud.xtilus.makingfriends.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.bean.MomentsMessage;
import com.cloud.xtilus.makingfriends.bean.MomentsMessageDao;
import com.cloud.xtilus.makingfriends.domain.InviteMessage;
import com.cloud.xtilus.makingfriends.domain.RobotUser;



import com.cloud.xtilus.makingfriends.domain.InviteMessage.InviteMessageStatus;


import com.cloud.xtilus.makingfriends.util.Constant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.HanziToPinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DemoDBManager {
    static private DemoDBManager dbMgr = new DemoDBManager();
    private DbOpenHelper dbHelper;
    
    private DemoDBManager(){
        dbHelper = DbOpenHelper.getInstance(MakingFriendApplication.getInstance().getApplicationContext());
    }
    
    public static synchronized DemoDBManager getInstance(){
        if(dbMgr == null){
            dbMgr = new DemoDBManager();
        }
        return dbMgr;
    }
    
    /**
     * save contact list
     * 
     * @param contactList
     */
    synchronized public void saveContactList(List<EaseUser> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(UserDao.TABLE_NAME, null, null);
            for (EaseUser user : contactList) {
                ContentValues values = new ContentValues();
                values.put(UserDao.COLUMN_NAME_ID, user.getUsername());
                if(user.getNickname() != null)
                    values.put(UserDao.COLUMN_NAME_NICK, user.getNickname());
                if(user.getAvatar() != null)
                    values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
                db.replace(UserDao.TABLE_NAME, null, values);
            }
        }
    }

    /**
     * get contact list
     * 
     * @return
     */
    synchronized public Map<String, EaseUser> getContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, EaseUser> users = new Hashtable<String, EaseUser>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + UserDao.TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_ID));
                String nick = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_NICK));
                String avatar = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_AVATAR));
                EaseUser user = new EaseUser(username);
                user.setNickname(nick);
                user.setAvatar(avatar);
                if (username.equals(Constant.NEW_FRIENDS_USERNAME) || username.equals(Constant.GROUP_USERNAME)
                        || username.equals(Constant.CHAT_ROOM)|| username.equals(Constant.CHAT_ROBOT)) {
                        user.setInitialLetter("");
                } else {
                    EaseCommonUtils.setUserInitialLetter(user);
                }
                users.put(username, user);
            }
            cursor.close();
        }
        return users;
    }
    
    /**
     * delete a contact
     * @param username
     */
    synchronized public void deleteContact(String username){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete(UserDao.TABLE_NAME, UserDao.COLUMN_NAME_ID + " = ?", new String[]{username});
        }
    }
    
    /**
     * save a contact
     * @param user
     */
    synchronized public void saveContact(EaseUser user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.COLUMN_NAME_ID, user.getUsername());
        if(user.getNickname() != null)
            values.put(UserDao.COLUMN_NAME_NICK, user.getNickname());
        if(user.getAvatar() != null)
            values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
        if(db.isOpen()){
            db.replace(UserDao.TABLE_NAME, null, values);
        }
    }
    
    public void setDisabledGroups(List<String> groups){
        setList(UserDao.COLUMN_NAME_DISABLED_GROUPS, groups);
    }
    
    public List<String>  getDisabledGroups(){       
        return getList(UserDao.COLUMN_NAME_DISABLED_GROUPS);
    }
    
    public void setDisabledIds(List<String> ids){
        setList(UserDao.COLUMN_NAME_DISABLED_IDS, ids);
    }
    
    public List<String> getDisabledIds(){
        return getList(UserDao.COLUMN_NAME_DISABLED_IDS);
    }
    
    synchronized private void setList(String column, List<String> strList){
        StringBuilder strBuilder = new StringBuilder();
        
        for(String hxid:strList){
            strBuilder.append(hxid).append("$");
        }
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(column, strBuilder.toString());

            db.update(UserDao.PREF_TABLE_NAME, values, null,null);
        }
    }
    
    synchronized private List<String> getList(String column){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + column + " from " + UserDao.PREF_TABLE_NAME,null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String strVal = cursor.getString(0);
        if (strVal == null || strVal.equals("")) {
            return null;
        }
        
        cursor.close();
        
        String[] array = strVal.split("$");
        
        if(array.length > 0){
            List<String> list = new ArrayList<String>();
            Collections.addAll(list, array);
            return list;
        }
        
        return null;
    }
    
    /**
     * save a message
     * @param message
     * @return  return cursor of the message
     */
    public synchronized Integer saveMessage(InviteMessage message){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int id = -1;
        if(db.isOpen()){
            ContentValues values = new ContentValues();
            values.put(InviteMessgeDao.COLUMN_NAME_FROM, message.getFrom());
            values.put(InviteMessgeDao.COLUMN_NAME_GROUP_ID, message.getGroupId());
            values.put(InviteMessgeDao.COLUMN_NAME_GROUP_Name, message.getGroupName());
            values.put(InviteMessgeDao.COLUMN_NAME_REASON, message.getReason());
            values.put(InviteMessgeDao.COLUMN_NAME_TIME, message.getTime());
            values.put(InviteMessgeDao.COLUMN_NAME_STATUS, message.getStatus().ordinal());
            values.put(InviteMessgeDao.COLUMN_NAME_GROUPINVITER, message.getGroupInviter());
            db.insert(InviteMessgeDao.TABLE_NAME, null, values);
            
            Cursor cursor = db.rawQuery("select last_insert_rowid() from " + InviteMessgeDao.TABLE_NAME,null); 
            if(cursor.moveToFirst()){
                id = cursor.getInt(0);
            }
            
            cursor.close();
        }
        return id;
    }

    /**
     * update message
     * @param msgId
     * @param values
     */
    synchronized public void updateMessage(int msgId,ContentValues values){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.update(InviteMessgeDao.TABLE_NAME, values, InviteMessgeDao.COLUMN_NAME_ID + " = ?", new String[]{String.valueOf(msgId)});
        }
    }
    
    /**
     * get messges
     * @return
     */
    synchronized public List<InviteMessage> getMessagesList(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<InviteMessage> msgs = new ArrayList<InviteMessage>();
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select * from " + InviteMessgeDao.TABLE_NAME + " desc",null);
            while(cursor.moveToNext()){
                InviteMessage msg = new InviteMessage();
                int id = cursor.getInt(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_ID));
                String from = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_FROM));
                String groupid = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUP_ID));
                String groupname = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUP_Name));
                String reason = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_REASON));
                long time = cursor.getLong(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_TIME));
                int status = cursor.getInt(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_STATUS));
                String groupInviter = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUPINVITER));
                
                msg.setId(id);
                msg.setFrom(from);
                msg.setGroupId(groupid);
                msg.setGroupName(groupname);
                msg.setReason(reason);
                msg.setTime(time);
                msg.setGroupInviter(groupInviter);
                msg.setStatus(InviteMessageStatus.values()[status]);
                msgs.add(msg);
            }
            cursor.close();
        }
        return msgs;
    }
    
    /**
     * delete invitation message
     * @param from
     */
    synchronized public void deleteMessage(String from){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete(InviteMessgeDao.TABLE_NAME, InviteMessgeDao.COLUMN_NAME_FROM + " = ?", new String[]{from});
        }
    }

    /**
     * delete invitation message
     * @param groupId
     */
    synchronized public void deleteGroupMessage(String groupId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete(InviteMessgeDao.TABLE_NAME, InviteMessgeDao.COLUMN_NAME_GROUP_ID + " = ?", new String[]{groupId});
        }
    }

    /**
     * delete invitation message
     * @param groupId
     */
    synchronized public void deleteGroupMessage(String groupId, String from){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete(InviteMessgeDao.TABLE_NAME, InviteMessgeDao.COLUMN_NAME_GROUP_ID + " = ? AND " + InviteMessgeDao.COLUMN_NAME_FROM + " = ? ",
                    new String[]{groupId, from});
        }
    }
    
    synchronized int getUnreadNotifyCount(){
        int count = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select " + InviteMessgeDao.COLUMN_NAME_UNREAD_MSG_COUNT + " from " + InviteMessgeDao.TABLE_NAME, null);
            if(cursor.moveToFirst()){
                count = cursor.getInt(0);
            }
            cursor.close();
        }
         return count;
    }
    
    synchronized void setUnreadNotifyCount(int count){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            ContentValues values = new ContentValues();
            values.put(InviteMessgeDao.COLUMN_NAME_UNREAD_MSG_COUNT, count);

            db.update(InviteMessgeDao.TABLE_NAME, values, null,null);
        }
    }
    
    synchronized public void closeDB(){
        if(dbHelper != null){
            dbHelper.closeDB();
        }
        dbMgr = null;
    }


    public synchronized int getMomentsUnReadCount() {
        int count = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + MomentsMessageDao.TABLE_NAME + " desc where "+ MomentsMessageDao.COLUMN_NAME_STATUS+" = ?", new String[]{MomentsMessage.Status.UNREAD.ordinal()+""});
            while (cursor.moveToNext()) {
                count++;
            }
            cursor.close();
        }
        return count;
    }


    public synchronized void saveMomentsNotice(MomentsMessage momentsMessage) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            boolean notice = checkMomentsNotice(db,momentsMessage);
            values.put(MomentsMessageDao.COLUMN_NAME_USERID, momentsMessage.getUserId());
            values.put(MomentsMessageDao.COLUMN_NAME_USERNICK, momentsMessage.getUserNick());
            values.put(MomentsMessageDao.COLUMN_NAME_AVATAR, momentsMessage.getUserAvatar());
            values.put(MomentsMessageDao.COLUMN_NAME_CONTENT, momentsMessage.getContent());
            values.put(MomentsMessageDao.COLUMN_NAME_IMAGEURL, momentsMessage.getImageUrl());
            values.put(MomentsMessageDao.COLUMN_NAME_TIME, momentsMessage.getTime());
            values.put(MomentsMessageDao.COLUMN_NAME_TYPE, momentsMessage.getType().ordinal());
            values.put(MomentsMessageDao.COLUMN_NAME_STATUS, momentsMessage.getStatus().ordinal());
            values.put(MomentsMessageDao.COLUMN_NAME_MOMENTS_ID, momentsMessage.getMid());
            if (notice){
                db.update(MomentsMessageDao.TABLE_NAME,values,MomentsMessageDao.COLUMN_NAME_TIME +" = ?",new String[]{momentsMessage.getTime()+""});
            }else{
                db.insert(MomentsMessageDao.TABLE_NAME, null, values);
            }
        }
    }


    public synchronized boolean checkMomentsNotice(SQLiteDatabase db,MomentsMessage momentsMessage) {
        String id = momentsMessage.getUserId();
        String mid = momentsMessage.getMid();
        String sql = "select * from "+MomentsMessageDao.TABLE_NAME+" where "+MomentsMessageDao.COLUMN_NAME_USERID+" = "+id
                + " and "+MomentsMessageDao.COLUMN_NAME_TYPE+" = "+ MomentsMessage.Type.GOOD.ordinal()+" and "+MomentsMessageDao.COLUMN_NAME_MOMENTS_ID+" = " +mid;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            String userId = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_USERID));
            String momentsId = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_MOMENTS_ID));
            if (id.equals(userId) && mid.equals(momentsId) && momentsMessage.getType().ordinal() ==MomentsMessage.Type.GOOD.ordinal()){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }


    public synchronized MomentsMessage getLastMomentsMessage() {
        MomentsMessage momentsMessage = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + MomentsMessageDao.TABLE_NAME + " order by "+MomentsMessageDao.COLUMN_NAME_ID +" desc limit 1", null);
            while (cursor.moveToNext()) {
                momentsMessage = new MomentsMessage();
                int id = cursor.getInt(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_ID));
                String userId = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_USERID));
                String userNick = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_USERNICK));
                String userAvatar = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_AVATAR));
                String momentsId = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_MOMENTS_ID));
                long time = cursor.getLong(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_TIME));
                int status = cursor.getInt(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_STATUS));
                int type = cursor.getInt(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_TYPE));
                String content = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_CONTENT));
                String imageUrl = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_IMAGEURL));
                momentsMessage.setContent(content);
                momentsMessage.setId(id);
                momentsMessage.setImageUrl(imageUrl);
                momentsMessage.setMid(momentsId);
                momentsMessage.setUserAvatar(userAvatar);
                momentsMessage.setUserId(userId);
                momentsMessage.setUserNick(userNick);
                momentsMessage.setTime(time);

                if (status == MomentsMessage.Status.READ.ordinal())
                    momentsMessage.setStatus(MomentsMessage.Status.READ);
                else if (status == MomentsMessage.Status.UNREAD.ordinal())
                    momentsMessage.setStatus(MomentsMessage.Status.UNREAD);

                if (type == MomentsMessage.Type.GOOD.ordinal())
                    momentsMessage.setType(MomentsMessage.Type.GOOD);
                else if (type == MomentsMessage.Type.COMMENT.ordinal())
                    momentsMessage.setType(MomentsMessage.Type.COMMENT);
                else if (type == MomentsMessage.Type.REPLY_COMMENT.ordinal())
                    momentsMessage.setType(MomentsMessage.Type.REPLY_COMMENT);
                cursor.close();
                return momentsMessage;


            }
            cursor.close();
        }
        return momentsMessage;
    }


    public synchronized List<MomentsMessage> getMomentsMessageList() {
        List<MomentsMessage> momentsMessages = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + MomentsMessageDao.TABLE_NAME + " order by "+MomentsMessageDao.COLUMN_NAME_TIME+" desc", null);

            while (cursor.moveToNext()) {
                MomentsMessage momentsMessage = new MomentsMessage();
                int id = cursor.getInt(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_ID));
                String userId = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_USERID));
                String userNick = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_USERNICK));
                String userAvatar = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_AVATAR));
                String momentsId = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_MOMENTS_ID));
                long time = cursor.getLong(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_TIME));
                int status = cursor.getInt(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_STATUS));
                int type = cursor.getInt(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_TYPE));
                String content = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_CONTENT));
                String imageUrl = cursor.getString(cursor.getColumnIndex(MomentsMessageDao.COLUMN_NAME_IMAGEURL));
                momentsMessage.setContent(content);
                momentsMessage.setId(id);
                momentsMessage.setImageUrl(imageUrl);
                momentsMessage.setMid(momentsId);
                momentsMessage.setUserAvatar(userAvatar);
                momentsMessage.setUserId(userId);
                momentsMessage.setUserNick(userNick);
                momentsMessage.setTime(time);

                if (status == MomentsMessage.Status.READ.ordinal())
                    momentsMessage.setStatus(MomentsMessage.Status.READ);
                else if (status == MomentsMessage.Status.UNREAD.ordinal())
                    momentsMessage.setStatus(MomentsMessage.Status.UNREAD);

                if (type == MomentsMessage.Type.GOOD.ordinal())
                    momentsMessage.setType(MomentsMessage.Type.GOOD);
                else if (type == MomentsMessage.Type.COMMENT.ordinal())
                    momentsMessage.setType(MomentsMessage.Type.COMMENT);
                else if (type == MomentsMessage.Type.REPLY_COMMENT.ordinal())
                    momentsMessage.setType(MomentsMessage.Type.REPLY_COMMENT);

                momentsMessages.add(momentsMessage);

            }
            cursor.close();
        }


        return momentsMessages;

    }

    public synchronized void  deleteAllMomentsMessage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        if (db.isOpen()) {

            db.delete(MomentsMessageDao.TABLE_NAME,null,null);

        }

    }

    public synchronized void clearMomentsUnReadCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(MomentsMessageDao.COLUMN_NAME_STATUS, MomentsMessage.Status.READ.ordinal());
            db.update(MomentsMessageDao.TABLE_NAME, values, MomentsMessageDao.COLUMN_NAME_STATUS + " = ?", new String[]{MomentsMessage.Status.UNREAD.ordinal()+""});
        }
    }
    /**
     * Save Robot list
     */
	synchronized public void saveRobotList(List<RobotUser> robotList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(UserDao.ROBOT_TABLE_NAME, null, null);
			for (RobotUser item : robotList) {
				ContentValues values = new ContentValues();
				values.put(UserDao.ROBOT_COLUMN_NAME_ID, item.getUsername());
				if (item.getNickname() != null)
					values.put(UserDao.ROBOT_COLUMN_NAME_NICK, item.getNickname());
				if (item.getAvatar() != null)
					values.put(UserDao.ROBOT_COLUMN_NAME_AVATAR, item.getAvatar());
				db.replace(UserDao.ROBOT_TABLE_NAME, null, values);
			}
		}
	}
    
    /**
     * load robot list
     */
	synchronized public Map<String, RobotUser> getRobotList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Map<String, RobotUser> users = null;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + UserDao.ROBOT_TABLE_NAME, null);
			if(cursor.getCount()>0){
				users = new Hashtable<String, RobotUser>();
			}
            while (cursor.moveToNext()) {
				String username = cursor.getString(cursor.getColumnIndex(UserDao.ROBOT_COLUMN_NAME_ID));
				String nick = cursor.getString(cursor.getColumnIndex(UserDao.ROBOT_COLUMN_NAME_NICK));
				String avatar = cursor.getString(cursor.getColumnIndex(UserDao.ROBOT_COLUMN_NAME_AVATAR));
				RobotUser user = new RobotUser(username);
				user.setNickname(nick);
				user.setAvatar(avatar);
				String headerName = null;
				if (!TextUtils.isEmpty(user.getNickname())) {
					headerName = user.getNickname();
				} else {
					headerName = user.getUsername();
				}
				if(Character.isDigit(headerName.charAt(0))){
					user.setInitialLetter("#");
				}else{
					user.setInitialLetter(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target
							.substring(0, 1).toUpperCase());
					char header = user.getInitialLetter().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setInitialLetter("#");
					}
				}

                try {
                    users.put(username, user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
			}
			cursor.close();
		}
		return users;
	}
}
