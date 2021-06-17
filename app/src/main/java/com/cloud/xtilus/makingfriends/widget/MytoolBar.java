package com.cloud.xtilus.makingfriends.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;


import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cloud.xtilus.makingfriends.R;

/**
 * Created by cloud on 2016/1/30.
 */
public class MytoolBar extends Toolbar {

    private LayoutInflater mInfater;
    private View mview;
    private TextView textView;
    private ImageButton imageButton;


    public MytoolBar(Context context) {
        this(context,null);
    }
    public MytoolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MytoolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        if(attrs!=null){
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.MytoolBar, defStyleAttr, 0);
           final Drawable rightIcon = a.getDrawable(R.styleable.MytoolBar_rightButtonIcon);
            if (rightIcon != null) {
                //setNavigationIcon(navIcon);
                setRightButtonIcon(rightIcon);
            }
           // boolean isShowSearchView = a.getBoolean(R.styleable.CNiaoToolBar_isShowSearchView,false);

            a.recycle();

        }


    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonIcon(Drawable rightIcon) {

        if(imageButton!=null){
           // imageButton.setBackground(rightIcon);
           // imageButton.setImageDrawable(rightIcon);
            imageButton.setBackground(rightIcon);
        }


    }
    public void setRightButtonIcon(int icon){

        setRightButtonIcon(getResources().getDrawable(icon));

    }
   public void setRightButtonOnClickListener(OnClickListener li){

       imageButton.setOnClickListener(li);

   }

//    public ImageButton getRightButton(){
//
//        return this.imageButton;
//    }



    private void initView() {
        //没判断view 是否为空，会导致toolbar变大
        if(mview==null){


            mInfater= LayoutInflater.from(getContext());
            mview =mInfater.inflate(R.layout.toolbar,null);

            textView=(TextView)mview.findViewById(R.id.title_toolbar);
            imageButton=(ImageButton)mview.findViewById(R.id.image_toolbar);

            LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

           addView(mview,lp);
        }
    }

    @Override
    public void setTitle(int resId) {

        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        if(textView!=null){
            textView.setText(title);

        }


    }


}
