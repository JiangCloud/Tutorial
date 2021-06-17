package com.cloud.xtilus.makingfriends.activity;



import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cloud.xtilus.makingfriends.Fragment.NewsFragment;
import com.cloud.xtilus.makingfriends.Fragment.NoticeFragment;
import com.cloud.xtilus.makingfriends.R;

import com.google.android.material.tabs.TabLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity   implements TabLayout.OnTabSelectedListener  {

    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;

    @ViewInject(R.id.vp)
    private ViewPager mViewPager;

    public static final int TAG_DEFAULT=0;
    public static final int TAG_SALE=1;
    public static final int TAG_PRICE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ViewUtils.inject(this);
        initTab();
    }

    private void initTab(){
        TabLayout.Tab tab= mTablayout.newTab();
        //tab.setText("默认");
        tab.setTag(TAG_DEFAULT);

        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
       // tab.setText("价格");
        tab.setTag(TAG_PRICE);

        mTablayout.addTab(tab);

    /*    tab= mTablayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);*/

        //mTablayout.addTab(tab);
        //mTablayout.setOnTabSelectedListener(this);

        mTablayout.addOnTabSelectedListener(this);
       List<Fragment> fragments=new ArrayList<Fragment>();
        fragments.add(new NewsFragment());
        fragments.add(new NoticeFragment());
        List<String> titles=new ArrayList<String>();
        titles.add("公告");
        titles.add("消息");
        TabFragmentPagerAdapter adapter=new TabFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewPager.setAdapter(adapter);
        mTablayout.setupWithViewPager(mViewPager);
      //  mTablayout.setTabTextColors(Color.BLACK, Color.RED);//设置文本在选中和为选中时候的颜色

        mTablayout.setTabsFromPagerAdapter(adapter);



    }

  private class TabFragmentPagerAdapter  extends FragmentStatePagerAdapter {
      private List<Fragment>fragments;
      private List<String>titles;


      public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {


          super(fm);
          this.fragments=fragments;
          this.titles=titles;

      }

      @Override
      public Fragment getItem(int i) {
          return fragments.get(i);
      }

      @Override
      public int getCount() {
          return fragments.size();
      }

      @Override
      public CharSequence getPageTitle(int position) {
          return titles.get(position);
      }
  }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
