package com.ycbjie.music.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ycbjie.music.R;
import com.ycbjie.music.api.OnLineMusicModel;
import com.ycbjie.music.inter.OnMoreClickListener;
import com.ycbjie.music.model.bean.Album;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.model.bean.OnlineMusicList;
import com.ycbjie.music.utils.FileMusicUtils;
import com.ycbjie.webviewlib.utils.OkHttpUtils;
import com.ycbjie.webviewlib.utils.X5LogUtils;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class LineMusicAdapter extends RecyclerArrayAdapter<OnlineMusicList.SongListBean> {



    public LineMusicAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }


    private class ViewHolder extends BaseViewHolder<OnlineMusicList.SongListBean> {
        String picUrl=null;
        View v_playing , v_divider;
        TextView tv_title , tv_artist ;
        ImageView iv_cover , iv_more;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_line_music);
            v_playing = $(R.id.v_playing);
            iv_cover = $(R.id.iv_cover);
            tv_title = $(R.id.tv_title);
            tv_artist = $(R.id.tv_artist);
            iv_more = $(R.id.iv_more);
            v_divider = $(R.id.v_divider);

            iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMoreClick(getAdapterPosition());
                }
            });
        }

        @Override
        public void setData(OnlineMusicList.SongListBean data) {
            super.setData(data);

           String bumMid= data.getAlbumMid();

            getAlbumPic(bumMid);

            tv_title.setText(data.getTitle());
            String artist = FileMusicUtils.getArtistAndAlbum(data.getSingerName(), data.getTitle());
            tv_artist.setText(artist);
            v_divider.setVisibility(isShowDivider(getAdapterPosition()) ? View.VISIBLE : View.GONE);


        }
        //
        @SuppressLint("CheckResult")
        private void  getAlbumPic(String albumMid) {
            OnLineMusicModel model = OnLineMusicModel.getInstance();
            model.getMusicAlbumInfo(albumMid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Album>() {

                        @Override
                        public void accept(Album album) throws Exception {

                             picUrl="http:"+album.getData().getPicUrl();
                            Glide.with(getContext())
                                    .load(picUrl)
                                    .placeholder(R.drawable.default_cover)
                                    .error(R.drawable.default_cover)
                                    .into(iv_cover);

                        }


                    },new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {

                }
            }, new Action() {
                @Override
                public void run() throws Exception {

                }
            });





        }



        private boolean isShowDivider(int position) {
            return position != getAllData().size() - 1;
        }

    }

    private OnMoreClickListener mListener;
    public void setOnMoreClickListener(OnMoreClickListener listener) {
        mListener = listener;
    }


}
