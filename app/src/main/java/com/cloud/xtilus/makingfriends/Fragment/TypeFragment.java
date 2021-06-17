package com.cloud.xtilus.makingfriends.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloud.xtilus.makingfriends.R;


public class TypeFragment extends BaseFragment {
    @Override
    public View CreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_type,container,false);
    }

    @Override
    public void init() {

    }

}
