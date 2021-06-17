package com.cloud.xtilus.makingfriends.common.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.cloud.xtilus.makingfriends.common.db.converter.DateConverter;
import com.cloud.xtilus.makingfriends.common.db.dao.AppKeyDao;
import com.cloud.xtilus.makingfriends.common.db.dao.EmUserDao;
import com.cloud.xtilus.makingfriends.common.db.dao.InviteMessageDao;
import com.cloud.xtilus.makingfriends.common.db.dao.MsgTypeManageDao;
import com.cloud.xtilus.makingfriends.common.db.entity.AppKeyEntity;
import com.cloud.xtilus.makingfriends.common.db.entity.EmUserEntity;
import com.cloud.xtilus.makingfriends.common.db.entity.InviteMessage;
import com.cloud.xtilus.makingfriends.common.db.entity.MsgTypeManageEntity;

@Database(entities = {EmUserEntity.class,
        InviteMessage.class,
        MsgTypeManageEntity.class,
        AppKeyEntity.class},
        version = 16)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EmUserDao userDao();

    public abstract InviteMessageDao inviteMessageDao();

    public abstract MsgTypeManageDao msgTypeManageDao();

    public abstract AppKeyDao appKeyDao();
}
