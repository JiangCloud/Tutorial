package com.cloud.xtilus.makingfriends.activity.moments;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.activity.moments.widget.MomentsItemView;
import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.bean.MomentsMessage;
import com.cloud.xtilus.makingfriends.bean.MomentsMessageDao;
import com.cloud.xtilus.makingfriends.util.Constant;





import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MomentsAdapter extends BaseAdapter {

    private Activity context;
    private List<JSONObject> jsonArray;
    private MomentsMessageDao momentsMessageDao;
    private RelativeLayout re_unread;
    private ImageView iv_moment_bg;
    private String backgroud = "";
    private String serverTime;

    public MomentsAdapter(Activity context1, List<JSONObject> jsonArray, String backgroud) {
        this.context = context1;
        this.jsonArray = jsonArray;
        momentsMessageDao = new MomentsMessageDao(context1);
        this.backgroud = backgroud;
    }

    @Override
    public int getCount() {
        return jsonArray.size() + 1;
    }


    @Override
    public JSONObject getItem(int position) {
        if (position == 0) {
            return null;
        } else {
            return jsonArray.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        if (getItemViewType(position) == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_moments_header, null, false);
            ImageView iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            re_unread = (RelativeLayout) convertView.findViewById(R.id.re_unread);
            iv_moment_bg = (ImageView) convertView.findViewById(R.id.iv_moment_bg);
            initHeaderView();
            Glide.with(context).load(MakingFriendApplication.getInstance().getUserJson().getString(Constant.JSON_KEY_AVATAR)).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.default_avatar).error(R.mipmap.default_avatar).into(iv_avatar);
            iv_moment_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapterListener != null) {
                        adapterListener.onMomentTopBackGroundClock();
                    }
                }
            });
            setBackground(backgroud);
        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_moments, parent, false);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder();
                holder.momentsItemView = (MomentsItemView) convertView.findViewById( R.id.main);
                convertView.setTag(holder);
            }

            JSONObject json = jsonArray.get(position - 1);
            final int realPosition = position - 1;
            // = new MomentsItemView(context);
            holder.momentsItemView.initView(json, getServerTime());
            holder.momentsItemView.setOnMenuClickListener(new MomentsItemView.OnMenuClickListener() {
                @Override
                public void onUserClicked(String userId) {
                    if (adapterListener != null) {
                        adapterListener.onUserClicked(realPosition, userId);
                    }
                }

                @Override
                public void onGoodIconClicked(String aid,String fxId) {
                    if (adapterListener != null) {
                        adapterListener.onPraised(realPosition, aid,fxId);
                    }
                }

                @Override
                public void onCommentIconClicked(String aid,String fxid) {
                    if (adapterListener != null) {
                        adapterListener.onCommented(realPosition, aid,fxid);
                    }
                }

                @Override
                public void onCancelGoodClicked(String aid) {
                    if (adapterListener != null) {
                        adapterListener.onCancelPraised(realPosition, aid);
                    }
                }

                @Override
                public void onCommentDeleteCilcked(String cid) {
                    if (adapterListener != null) {
                        adapterListener.onCommentDelete(realPosition, cid);
                    }
                }

                @Override
                public void onDeleted(String aid) {
                    if (adapterListener != null) {
                        adapterListener.onDeleted(realPosition, aid);
                    }
                }

                @Override
                public void onImageListClicked(int index, ArrayList<String> images) {
                    if (adapterListener != null) {
                        adapterListener.onImageClicked(realPosition, index, images);
                    }
                }


            });


            setItemView(realPosition, (MomentsItemView) convertView);


        }

        return convertView;

    }

    private class ViewHolder {
        MomentsItemView momentsItemView;

    }

    public void setBackground(String url) {
        this.backgroud=url;
        if (iv_moment_bg == null) {
            return;
        }
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.bg_moments_header).error(R.mipmap.bg_moments_header).into(iv_moment_bg);
    }

    public void initHeaderView() {
        if (re_unread == null) {
            return;
        }
        int count = momentsMessageDao.getUnreadMoments();
        if (count > 0) {
            MomentsMessage momentsMessage = momentsMessageDao.getLastMomentsMessage();
            re_unread.setVisibility(View.VISIBLE);
            re_unread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, MomentsNoticeActivity.class));
                }
            });
            ImageView imageView = (ImageView) re_unread.findViewById(R.id.msg_avatar);
            TextView tvCount = (TextView) re_unread.findViewById(R.id.tv_count);
            tvCount.setText(count + context.getString(R.string.msg_count));
            Glide.with(context).load(momentsMessage.getUserAvatar()).placeholder(R.mipmap.default_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        } else {
            re_unread.setVisibility(View.GONE);
        }
    }

    public void hideHeaderView() {
        if (re_unread == null) {
            return;
        }
        re_unread.setVisibility(View.GONE);
    }

    private Map<Integer, MomentsItemView> views = new HashMap<>();

    public MomentsItemView getItemView(int position) {
        return views.get(position);
    }

    private void setItemView(int position, MomentsItemView momentsItemView) {
        views.put(position, momentsItemView);
    }


    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getServerTime() {
        return serverTime;
    }


    private AdapterListener adapterListener;

    public void setListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }
}
