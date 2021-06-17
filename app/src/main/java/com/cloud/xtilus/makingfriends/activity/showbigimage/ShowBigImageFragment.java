package com.cloud.xtilus.makingfriends.activity.showbigimage;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloud.xtilus.makingfriends.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;



/**
 * 项目名称：yichat0718
 * 类描述：ShowBigImageFragment 描述:
 * 创建人：songlijie
 * 创建时间：2017/8/24 13:53
 * 邮箱:814326663@qq.com
 */
public class ShowBigImageFragment extends Fragment {
    private RelativeLayout title;
    private PhotoView image;
    private String localPath;
    private String path = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_show_big_image, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
        initView();
        initData();
    }

    private void initData() {
//        title.setVisibility(View.GONE);
        Glide.with(getContext()).asBitmap().load(path).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.mipmap.default_image).into(image);
    }

    private void initView() {
        image = (PhotoView) getView().findViewById(R.id.image);
        title = (RelativeLayout) getView().findViewById(R.id.title);
    }

    private void getData() {
        localPath = getArguments().getString("localPath");
        if (TextUtils.isEmpty(localPath)) {
            getActivity().finish();
            return;
        }
        if (localPath.equals("false")) {
            getActivity().finish();
            return;
        }
        if (localPath.contains("http://") || localPath.startsWith("http") || localPath.contains("https://")) {
            path = localPath;
        } else {
            Uri uri = Uri.fromFile(new File(localPath));
            path = uri.getPath();
        }
    }
}
