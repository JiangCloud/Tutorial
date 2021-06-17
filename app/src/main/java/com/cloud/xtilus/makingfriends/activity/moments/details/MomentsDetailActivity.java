package com.cloud.xtilus.makingfriends.activity.moments.details;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.section.base.BaseActivity;


/**
 * Created by huangfangyi on 2017/7/22.
 * qq 84543217
 */

public class MomentsDetailActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_base);
        setTitle(R.string.moments);


        MomentsDetailFragment momentsDetailFragment = (MomentsDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (momentsDetailFragment == null) {

            momentsDetailFragment=new MomentsDetailFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.contentFrame,momentsDetailFragment);
            fragmentTransaction.commit();
        }

        momentsDetailFragment.setArguments(getIntent().getExtras());

        MomentsPresenter momentsPresenter=new MomentsPresenter(momentsDetailFragment);
    }


}
