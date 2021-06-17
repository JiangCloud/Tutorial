package com.cloud.xtilus.makingfriends.Fragment;


import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;


public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= CreateView( inflater,  container,  savedInstanceState);
        ViewUtils.inject(this,view);
        init();
        initToolBar();
        return view;
    }

    public abstract  View CreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState);
    public void  initToolBar(){

    }

    public abstract void init();

}
