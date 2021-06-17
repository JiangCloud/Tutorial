package com.cloud.xtilus.makingfriends.activity.moments;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.alibaba.fastjson.JSONArray;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.activity.details.UserDetailsActivity;
import com.cloud.xtilus.makingfriends.activity.moments.widget.MomentsItemView;
import com.cloud.xtilus.makingfriends.http.IMAction;
import com.cloud.xtilus.makingfriends.util.CommonUtils;
import com.cloud.xtilus.makingfriends.widget.HTAlertDialog;
import com.cloud.xtilus.makingfriends.widget.swipyrefresh.SwipyRefreshLayout;

import java.util.ArrayList;

import pub.devrel.easypermissions.EasyPermissions;

//import com.htmessage.fanxinht.acitivity.main.details.UserDetailsActivity;

/**
 * Created by huangfangyi on 2017/7/10.
 * qq 84543217
 */

public class MomentsFragment extends Fragment implements MomentsContract.View, SwipyRefreshLayout.OnRefreshListener {
    private SwipyRefreshLayout pullToRefreshListView;
    private ListView actualListView;
    private MomentsAdapter adapter;
    private MomentsContract.Presenter presenter;
    private AdapterListener adapterListener;
    private RelativeLayout reEdittext;
    private EditText etComment;
    private Button buttonSend;
    private InputMethodManager inputMethodManager;
    private MyBroadCastReceiver myBroadCastReceiver;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inputMethodManager = (InputMethodManager) getBaseActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        getBaseActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        adapter = new MomentsAdapter(getActivity(), presenter.getData(), presenter.getBackgroudMoment());
        presenter.getCacheTime();
        adapterListener = new AdapterListener() {
            @Override
            public void onUserClicked(int position, String fxId) {
                getActivity().startActivity(new Intent(getContext(), UserDetailsActivity.class).putExtra("fxid", fxId));
            }

            @Override
            public void onPraised(int position, String aid,String fxId) {
                presenter.setGood(position, aid,fxId);
            }

            @Override
            public void onCommented(int position, String aid,String fxId) {
                showInputMenu(position, aid,fxId);
            }

            @Override
            public void onCancelPraised(int position, String gid) {
                presenter.cancelGood(position, gid);

            }

            @Override
            public void onCommentDelete(int position, String cid) {
                showDelCommentDilog(position, cid);
            }

            @Override
            public void onDeleted(int position, String aid) {
                presenter.deleteItem(position, aid);
            }

            @Override
            public void onImageClicked(int position, int index, ArrayList<String> images) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), BigImageActivity.class);
                intent.putExtra("images", images.toArray(new String[images.size()]));
                intent.putExtra("page", index);
                getActivity().startActivity(intent);
            }

            @Override
            public void onMomentTopBackGroundClock() {
                showPicDialog(2, getString(R.string.change_moment_bg));
            }
        };
        adapter.setListener(adapterListener);

        actualListView.setAdapter(adapter);
        presenter.loadeData(0);
        myBroadCastReceiver = new MyBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IMAction.ACTION_MOMENTS);
        intentFilter.addAction(IMAction.ACTION_MOMENTS_READ);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(myBroadCastReceiver, intentFilter);
    }

    @Override
    public void onRefresh(int index) {
      //  index = 1;
        index=0;
        presenter.loadeData(index);
    }

    @Override
    public void onLoad(int index) {
       // index++;
        presenter.loadeData(index);
    }

    private class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (IMAction.ACTION_MOMENTS.equals(intent.getAction())) {

                adapter.initHeaderView();
            } else if (IMAction.ACTION_MOMENTS_READ.equals(intent.getAction())) {
                adapter.hideHeaderView();
            }
        }
    }


    private void showDelCommentDilog(final int position, final String cid) {
        HTAlertDialog HTAlertDialog = new HTAlertDialog(getActivity(), null, new String[]{getString(R.string.delete)});
        HTAlertDialog.init(new HTAlertDialog.OnItemClickListner() {
            @Override
            public void onClick(int itemIndex) {
                switch (itemIndex) {
                    case 0:
                        presenter.deleteComment(position, cid);
                        break;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_moments, container, false);
        pullToRefreshListView = (SwipyRefreshLayout) root.findViewById(R.id.pull_refresh_list);
        actualListView = (ListView) root.findViewById(R.id.refresh_list);
        pullToRefreshListView.setOnRefreshListener(this);
        reEdittext = (RelativeLayout) root.findViewById(R.id.re_edittext);
        etComment = (EditText) reEdittext.findViewById(R.id.et_comment);
        buttonSend = (Button) reEdittext.findViewById(R.id.btn_send);
        return root;
    }

    @Override
    public void setPresenter(MomentsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Context getBaseContext() {
        return getContext();
    }

    @Override
    public Activity getBaseActivity() {
        return getActivity();
    }


    @Override
    public void showInputMenu(final int position, final String aid,final String fxId) {
        reEdittext.setVisibility(View.VISIBLE);
        etComment.requestFocus();
        etComment.postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.showSoftInput(etComment, 0);
            }
        }, 400);

        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    CommonUtils.showToastShort(getContext(), R.string.input_talks);
                    return;
                }

                hideInputMenu();
                presenter.comment(position, aid, comment,fxId);
                etComment.setText("");

            }

        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                int listviewHeight = actualListView.getMeasuredHeight();
                MomentsItemView momentsItemView = adapter.getItemView(position);
                int itemHeight = momentsItemView.getMeasuredHeight();
                actualListView.setSelectionFromTop(position + 2, listviewHeight - itemHeight);

            }
        }, 1000);


    }

    @Override
    public void hideInputMenu() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.hideSoftInputFromWindow(getBaseActivity().getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                reEdittext.setVisibility(View.GONE);
            }
        }, 300);


    }


    public void updateCommentView(int position, JSONArray jsonArray) {
        MomentsItemView momentsItemView = adapter.getItemView(position);
        momentsItemView.updateCommentView(jsonArray);

    }

    @Override
    public void updateGoodView(int position, JSONArray jsonArray) {
        MomentsItemView momentsItemView = adapter.getItemView(position);
        momentsItemView.updateGoodView(jsonArray);
    }

    @Override
    public void showBackground(String url) {
        adapter.setBackground(url);
    }

    @Override
    public void showPicDialog(final int type, String title) {
        HTAlertDialog HTAlertDialog = new HTAlertDialog(getContext(), title, new String[]{getString(R.string.attach_take_pic), getString(R.string.image_manager)});
        HTAlertDialog.init(new HTAlertDialog.OnItemClickListner() {
            @Override
            public void onClick(int position) {

                getPermission();
                switch (position) {
                    case 0:
                        presenter.startToPhoto(type);
                        break;
                    case 1:
                        presenter.startToAlbum(type);
                        break;
                }
            }
        });
    }

    //获取权限
    private void getPermission() {


        if (EasyPermissions.hasPermissions(getBaseContext(), permissions)) {
            //已经打开权限
              Toast.makeText(getBaseContext(), "已经申请相关权限", Toast.LENGTH_SHORT).show();
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }


    @Override
    public void onRefreshComplete() {
        pullToRefreshListView.setRefreshing(false);
    }

    @Override
    public void refreshListView(String time) {
        if (time != null) {
            adapter.setServerTime(time);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {


        if (myBroadCastReceiver != null) {

            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(myBroadCastReceiver);
        }
        super.onDestroy();
    }
}
