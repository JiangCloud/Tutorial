package com.cloud.xtilus.makingfriends.manager;

import android.content.Context;
import android.util.Log;

import com.cloud.xtilus.makingfriends.db.UserDao;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;
import java.util.Map;

/**
 * Created by huangfangyi on 2016/12/8.
 * qq 84543217
 */

public class ContactsManager {

    private static ContactsManager contactsManager;
    private Context context;
    private UserDao userDao;
    private Map<String,EaseUser> contacts=null;


    synchronized public static void init(Context context) {
        if (contactsManager == null) {
            contactsManager = new ContactsManager(context);
        }
    }

    public ContactsManager(Context context) {
        this.context = context;
        userDao= new UserDao(context);
        initContacts();

    }

    public static ContactsManager getInstance(){

        if(contactsManager==null){

            throw  new RuntimeException("please init this first!");
        }

        return  contactsManager;

    }

    public boolean saveContactList(List<EaseUser> contactList) {
        Log.d("saveContactList----->",contactList.size()+"");
        contacts.clear();
        for (EaseUser user:contactList){
            contacts.put(user.getUsername(),user);
        }
        userDao.saveContactList(contactList);
        return true;

    }

    public Map<String, EaseUser> getContactList() {
        if(contacts==null){
             contacts=userDao.getContactList();
        }
       
        return contacts;
    }

    public void saveContact(EaseUser user){
               contacts.put(user.getUsername(),user);
        userDao.saveContact(user);
     }


   private void  initContacts(){
       contacts=userDao.getContactList();
   }


}
