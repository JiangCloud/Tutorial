package com.cloud.xtilus.makingfriends.section.group.adapter;

import com.cloud.xtilus.makingfriends.section.contact.adapter.ContactListAdapter;
import com.cloud.xtilus.makingfriends.R;

public class GroupMemberAuthorityAdapter extends ContactListAdapter {

    @Override
    public int getEmptyLayoutId() {
        return R.layout.ease_layout_default_no_data;
    }
}
