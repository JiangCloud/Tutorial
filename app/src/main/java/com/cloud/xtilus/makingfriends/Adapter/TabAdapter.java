package com.cloud.xtilus.makingfriends.Adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.bean.LotteryTicket;
import com.cloud.xtilus.makingfriends.bean.User;
import com.cloud.xtilus.makingfriends.http.BaseCallback;
import com.cloud.xtilus.makingfriends.http.OkHttpHelper;
import com.cloud.xtilus.makingfriends.util.Constant;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by cloud on 2018/1/27.
 */

public class TabAdapter extends BaseAdapter {

    private Long[] times=new Long[]{60 * 1000L,70 * 1000L,80 * 1000L ,3660*1000L};
    private List<LotteryTicket> lotteries ;
    private Context context;
   //private  TextView   timeView;
    private OkHttpHelper httpHeper = OkHttpHelper.getInstance();
    private   MyCountDownTimer myCountDownTimer;
    public TabAdapter(Context context,List<LotteryTicket> lotteries){

        this.context=context;
        this.lotteries=lotteries;


    }

    @Override
    public int getCount() {
        return lotteries.size();
    }

    @Override
    public Object getItem(int position) {
        return lotteries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=  LayoutInflater.from(context);
        View view=  layoutInflater.inflate(R.layout.itern_tab_view,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.tab_imag);
        String url=lotteries.get(position).getImgUrl();
        //.resize(90,90).centerCrop().
        Picasso.with(context).load(url).into(imageView);
        TextView tv= (TextView) view.findViewById(R.id.tab_tv);
        TextView  TextView= (TextView) view.findViewById(R.id.tab_time);
        //彩种名称
        tv.setText(lotteries.get(position).getName());
        Toast.makeText(context,position+"",Toast.LENGTH_SHORT).show();

     //  myCountDownTimer=new MyCountDownTimer(times[position]+600,1000L,TextView);

      //  myCountDownTimer.start();
     //   CountDownTimer timer=
//        new CountDownTimer( times[position],1000L) {
//            Map params=new HashMap();
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//                long time =   millisUntilFinished / 1000;
//                timeView.setText(String.format("%02d:%02d:%02d", time / 3600%60,time/ 60%60, time % 60));
//
//            }
//
//            @Override
//            public void onFinish() {
//                cancel();
               // params.put("page","1");
               // params.put("size","4");
               // Toast.makeText(context,"倒计时完成开始拉取数据！",Toast.LENGTH_SHORT).show();
//                httpHeper.post(Constant.Get_UWERINFO,params, new BaseCallback<List<User>>() {
//                    @Override
//                    public void onBeforeRequest(Request request) {
//
//                        Toast.makeText(context,"倒计时完成开始拉取数据！",Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void onFailure(Request request, Exception e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Response response) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(Response response, List<User> users) {
//
//                        timeView.setText("00:00:15");
//                    }
//
//                    @Override
//                    public void onError(Response response, int code, Exception e) {
//
//                    }
//                });


         //   }
      //  }.start();



        return view;

    }

    public class MyCountDownTimer extends CountDownTimer {
        private TextView textView;
        public MyCountDownTimer(long millisInFuture, long countDownInterval,TextView textView) {


            super(millisInFuture, countDownInterval);
            this.textView=textView;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long time = millisUntilFinished / 1000;

            textView.setText(String.format("%02d:%02d:%02d", time / 3600%60,time/ 60%60, time % 60));
        }

        @Override
        public void onFinish() {

            textView.setText(" 00:00");

            //cancelTimer();
        }
    }




}
//public class MainActivity extends AppCompatActivity {
//
//    private TextView textView;
//    private MyCountDownTimer timer;
//    private final long TIME = 60 * 1000L;
//    private final long INTERVAL = 1000L;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_second);
//        textView = (TextView) findViewById(R.id.tv);
//        startTimer();
//    }
//
//    public void start(View view) {
//        startTimer();
//    }
//
//    public void cancel(View view) {
//        textView.setText("倒计时结束  00:00");
//        cancelTimer();
//    }
//
//    /**
//     * 开始倒计时
//     */
//    private void startTimer() {
//        if (timer == null) {
//            timer = new MyCountDownTimer(TIME, INTERVAL);
//        }
//        timer.start();
//    }
//
//    /**
//     * 取消倒计时
//     */
//    private void cancelTimer() {
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        cancelTimer();
//    }
//}


