package com.cloud.xtilus.makingfriends.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cloud.xtilus.makingfriends.Adapter.TabAdapter;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.bean.LotteryTicket;
import com.cloud.xtilus.makingfriends.bean.ResponseBean;
import com.cloud.xtilus.makingfriends.bean.User;
import com.cloud.xtilus.makingfriends.http.BaseCallback;
import com.cloud.xtilus.makingfriends.http.OkHttpHelper;
import com.cloud.xtilus.makingfriends.section.chat.activity.ChatActivity;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.hyphenate.easeui.constants.EaseConstant;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;


public class HotFragment extends BaseFragment {

    private OkHttpHelper httpHeper = OkHttpHelper.getInstance();
    private TabAdapter tabAdapter;
    private List<User> listUser;//用来存用户信息列表的集合
    @ViewInject(R.id.grid_tab)
    private GridView tabGrid;


    @Override
    public View CreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_common_tab, container, false);
    }

    @Override
    public void init() {
        //loadData();
    }







    private void loadData() {
        Map params = new HashMap();
        params.put("page", "1"); //默认显示第一页
        params.put("size", "4");//每页显示10个
        httpHeper.post(Constant.Get_LatteryInfo, params, new BaseCallback<ResponseBean<List<LotteryTicket>>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

                Toast.makeText(getContext(),"系统繁忙请稍后再试！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, ResponseBean<List<LotteryTicket>>  responseBean) {

                if (responseBean.getRet_code()==200) {
                    //listUser = users;
                    List<LotteryTicket> tickets= responseBean.getRet_data();
                    tabAdapter=new TabAdapter(getContext(),tickets);
                   // getUserInfoAdapter = new GetUserInfoAdapter(getContext(), listUser, R.layout.itern_listview);
                   // listView.setAdapter(getUserInfoAdapter);
                    tabGrid.setAdapter(tabAdapter);
                    tabGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                         //   String name = listUser.get((int) id).getNickname();
                           // User user = listUser.get((int) id);
                            Intent intent = new Intent();
                            //intent.putExtra(EaseConstant.EXTRA_USER_ID, "131542828515329");
                            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, "131542828515329");

                            intent.setClass(getContext(), ChatActivity.class);
                            startActivity(intent);
                           // Toast.makeText(getContext(), id + "a" + position + "name:" + name, Toast.LENGTH_SHORT).show();


                        }
                    });


                }


            }

            @Override
            public void onError(Response response, int code, Exception e) {

                Toast.makeText(getContext(),"请求超时，请稍后再试！", Toast.LENGTH_SHORT).show();
            }
        });


    }

}