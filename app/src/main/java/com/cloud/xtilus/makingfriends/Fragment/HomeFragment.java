package com.cloud.xtilus.makingfriends.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cloud.xtilus.makingfriends.Adapter.GetUserInfoAdapter;
import com.cloud.xtilus.makingfriends.Adapter.MyAdapter;
import com.cloud.xtilus.makingfriends.Adapter.decoration.CardViewtemDecortion;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.activity.NewsActivity;
import com.cloud.xtilus.makingfriends.activity.WebViewActivity;
import com.cloud.xtilus.makingfriends.bean.Banner;
import com.cloud.xtilus.makingfriends.bean.HomeCampaign;
import com.cloud.xtilus.makingfriends.bean.User;
import com.cloud.xtilus.makingfriends.http.BaseCallback;
import com.cloud.xtilus.makingfriends.http.OkHttpHelper;
import com.cloud.xtilus.makingfriends.http.SpotsCallBack;
import com.cloud.xtilus.makingfriends.section.chat.activity.ChatActivity;
import com.cloud.xtilus.makingfriends.util.ACache;
import com.cloud.xtilus.makingfriends.widget.CustomLinearLayoutManager;
import com.cloud.xtilus.makingfriends.widget.MytoolBar;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

;


public class HomeFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener , TabLayout.OnTabSelectedListener  {

    @ViewInject(R.id.slider)
    private SliderLayout sliderLayout;

    @ViewInject(R.id.nav_gridview)
     private GridView navGridView;

    //@ViewInject(R.id.lv)
  //  private PullToRefreshListView listView;

   @ViewInject(R.id.grid)
   private GridView gridView;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;

    @ViewInject(R.id.vp)
    private ViewPager mViewPager;
    private OkHttpHelper httpHeper=OkHttpHelper.getInstance();
    private List<Banner> banners=new ArrayList<Banner>();

    private GetUserInfoAdapter getUserInfoAdapter;
    @ViewInject(R.id.myToolBar)
    private MytoolBar toolbar;

    @ViewInject(R.id.recylerView)
    private RecyclerView recyclerView;

    private int page = 0;  //当前页码
    private int size = 10; //每页显示10个

    public static final int TAG_CZ=0;
    public static final int TAG_TX=1;
    public static final int TAG_YHHD=2;
    public static final int TAG_DLZX=3;
    public static final int TAG_ONLINE=4;

    private List<User> listUser=new ArrayList<User>();//用来存用户信息列表的集合
    private ACache mCache;
    private Gson gson;
    private List<User> userInfoes;
    @Override
    public View CreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_home,container,false) ;
    }
   //点击广告图片
    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

        String extra=baseSliderView.getBundle().get("extra")+"";
        Intent intent=new Intent(getContext(),WebViewActivity.class);
        intent.putExtra("pa", extra);
        startActivity(intent);
      // Toast.makeText(getContext(), extra,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void initToolBar() {
        toolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewsActivity.class);
                startActivity(intent);

            }
        });
        super.initToolBar();
    }

    @Override
    public void init() {
        //实例化缓存对象
      // mCache=ACache.get(getActivity());
        requstImage();
        initNavView();
       // initView();

     // initRecyclerView();
        initTab();

    }

    private void initTab(){
        TabLayout.Tab tab= mTablayout.newTab();
        //tab.setText("默认");
        tab.setTag(TAG_CZ);

        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        // tab.setText("价格");
        tab.setTag(TAG_TX);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setTag(TAG_YHHD);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setTag(TAG_DLZX);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setTag(TAG_ONLINE);
        mTablayout.addTab(tab);

        mTablayout.setOnTabSelectedListener(this);

        List<Fragment> fragments=new ArrayList<Fragment>();
        fragments.add(new HotFragment());
        fragments.add(new HighLotteryFragment());
        fragments.add(new LowLotteryFragment());
        fragments.add(new AllLotteryFragment());
        List<String> titles=new ArrayList<String>();
        titles.add("热门");
        titles.add("高频彩");
        titles.add("低频彩");
        titles.add("全部");
        HomeFragment.TabFragmentPagerAdapter adapter=new HomeFragment.TabFragmentPagerAdapter(getFragmentManager(),fragments,titles);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);// //设置缓存page的个数 2*n+1;
        mTablayout.setupWithViewPager(mViewPager);
       // mTablayout.setTabTextColors(Color.BLACK, Color.RED);//设置文本在选中和为选中时候的颜色

        mTablayout.setTabsFromPagerAdapter(adapter);



    }


    private class TabFragmentPagerAdapter  extends FragmentPagerAdapter {
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







    //初始化 GridView 视图
    private void initNavView() {

        navGridView.setAdapter(new MyAdapter(getContext()));
        navGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent= new Intent(getContext(), ChatActivity.class);
                intent.putExtra("userId","133795009331201");
                intent.putExtra("chatType",3) ;//聊天室
                startActivity(intent);



               // Toast.makeText(getContext(), "id:" + id, Toast.LENGTH_SHORT).show();

            }
        });

//        List<String > goods=new ArrayList<String>();
//        goods.add("aa");
//        goods.add("bb");
//        goods.add("cc");
//        goods.add("aa");
//        goods.add("bb");
//        goods.add("cc");
//
//        int size = goods.size();
//        int length = 100;
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        float density = dm.density;
//        int gridviewWidth = (int) (size * (length + 4) * density);
//        int itemWidth = (int) (length * density);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
//        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
//        gridView.setColumnWidth(itemWidth); // 设置列表项宽
//        gridView.setHorizontalSpacing(2); // 设置列表项水平间距
//        gridView.setStretchMode(GridView.NO_STRETCH);
//        gridView.setNumColumns(size); // 设置列数量=列表集合数
//        gridView.setAdapter(new goodBaseAdater(goods));
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),position+"",Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    private void initSlider() {

        if(banners!=null){
            for(Banner banner:banners){
                TextSliderView textSliderView=new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                //textSliderView.description(banner.getDescription());
                textSliderView.setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra", banner.getName());


                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                sliderLayout.addSlider(textSliderView);


             }
        }

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        sliderLayout.setDuration(3000);



        sliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {

                //Toast.makeText(this,getString(i),Toast.LENGTH_SHORT).show();

               // Log.d("Slider Demo", "Page Changed: " + position);


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void requstImage() {

        String url="http://112.124.22.238:8081/course_api/banner/query?type=1";
        httpHeper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {
            @Override
            public void onSuccess(Response response, List<Banner> banner) {

                banners = banner;
                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    private  void initRecyclerView(){
        OkHttpHelper.getInstance().get("http://112.124.22.238:8081/course_api/campaign/recommend", new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> HomeCampaigns) {
                Log.d("HomeCampaigns",HomeCampaigns.get(0).getCpOne().getImgUrl());
                initDatas(HomeCampaigns);



            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }

    private void initDatas(List<HomeCampaign> homeCampaigns) {
        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //recyclerView.setAdapter(new ViewPagerAdater(homeCampaigns) );
        recyclerView.addItemDecoration(new CardViewtemDecortion());
        recyclerView.setLayoutManager(layoutManager);
       recyclerView.setNestedScrollingEnabled(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        sliderLayout.stopAutoCycle();
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


    private  class ViewPagerAdater extends RecyclerView.Adapter<ViewHolder>{
        private List<HomeCampaign> homeCampaigns;


        public  ViewPagerAdater(List<HomeCampaign> homeCampaigns){
            this.homeCampaigns=homeCampaigns;

        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            LayoutInflater   mInflater = LayoutInflater.from(viewGroup.getContext());

            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview,null,false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {


            Picasso.with(getContext()).load(homeCampaigns.get(position).getCpOne().getImgUrl()).into(holder.imageViewBig);
            Picasso.with(getContext()).load(homeCampaigns.get(position).getCpThree().getImgUrl()).into( holder.imageViewSmallTop);
            Picasso.with(getContext()).load(homeCampaigns.get(position).getCpTwo().getImgUrl()).into( holder.imageViewSmallBottom);
        }



        @Override
        public int getItemCount() {
            return homeCampaigns.size();
        }
    }

   class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

       TextView textTitle;
       ImageView imageViewBig;
       ImageView imageViewSmallTop;
       ImageView imageViewSmallBottom;


       public ViewHolder(View itemView) {
           super(itemView);
           textTitle = (TextView) itemView.findViewById(R.id.text_title);
           imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
           imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
           imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);


           imageViewBig.setOnClickListener(this);
           imageViewSmallTop.setOnClickListener(this);
           imageViewSmallBottom.setOnClickListener(this);

       }


       @Override
       public void onClick(View v) {

          // if(mListener !=null){

//               anim(v);

           //}
       }
   }



























    private class goodBaseAdater extends BaseAdapter{
        int[] images= new int[]
                {R.mipmap.goodone,R.mipmap.goodtwo,R.mipmap.goodthree,R.mipmap.goodone,R.mipmap.goodtwo
                        ,R.mipmap.goodone
                };

        private List<String> goods;

      public goodBaseAdater(List<String> goods){

          this.goods=goods;
      }
        @Override
        public int getCount() {
            return goods.size();
        }

        @Override
        public Object getItem(int position) {
            return goods.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater=  LayoutInflater.from(getContext());
            View view=  layoutInflater.inflate(R.layout.good_gridview_itern, null);
            ImageView imageView= (ImageView) view.findViewById(R.id.imageOne);
            TextView textView= (TextView) view.findViewById(R.id.depict);
            textView.setText("时时野果野果霄非非圾肤浅非来龙去脉不顾一切");
            imageView.setImageResource(images[position]);
            return view;
        }
    }
}
