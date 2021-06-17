package com.cloud.xtilus.makingfriends.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloud.xtilus.makingfriends.Adapter.PicturesAdapter;
import com.cloud.xtilus.makingfriends.BuildConfig;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.bean.ResponseBean;
import com.cloud.xtilus.makingfriends.http.BaseCallback;
import com.cloud.xtilus.makingfriends.http.OkHttpHelper;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;



import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class PicturesActivity extends AppCompatActivity{

    @ViewInject(R.id.gv_pic)
    private GridView picGridView;
    private PicturesAdapter  picturesAdapter;
    private final static int FOR_START_CAMERA = 2;

    private List<String> saveUrl;
    private String mStrPhotoPath;//相机拍照后保存的路径
    private List<String> imageUrls=new ArrayList<String>();
    @ViewInject(R.id.menuToolBar)
    private Toolbar mytoolBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);
        ViewUtils.inject(this);
        initToolBar();
        init();

    }




    private  void initToolBar(){
        mytoolBar.setNavigationIcon(R.drawable.icon_back_32px);
       // mytoolBar.setLogo(R.mipmap.ic_drawer_home);
        mytoolBar.inflateMenu(R.menu.menu_image_pager);

        mytoolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // item.setVisible(false);
                int menuItemId = item.getItemId();

                if (menuItemId == R.id.action_search) {
                    startCamera();

                } else if (menuItemId == R.id.action_notification) {
                    if(imageUrls.size()>0){
                        uploadPic(imageUrls);

                    }else{
                        Toast.makeText(PicturesActivity.this, "亲，你还没有选择要上传的图片", Toast.LENGTH_SHORT).show();
                    }


                }
                return true;
            }


        });
    }


    /***
     * 初始化
     */
    private  void init(){
        saveUrl=getImgPathList();
        if(saveUrl.size()>0){
            Log.d("ImageUrl", saveUrl.size()+"");
        picturesAdapter= new PicturesAdapter(this,saveUrl);
        picGridView.setAdapter(picturesAdapter);
        picGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //parent.get
                imageUrls.add(saveUrl.get(position));
                ImageView imageView = (ImageView) view.findViewById(R.id.picMark);
                imageView.setVisibility(View.VISIBLE);
              // MenuItem item1= (MenuItem) findViewById(R.id.action_notification);
              //item1.setVisible(true);
                //invalidateOptionsMenu();
               // mytoolBar.inflateMenu(R.menu.menu_loging);

            }
        });
            picGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getApplication(),ImagePagerActivity.class);
                    intent.putExtra("position",position);
                    startActivity(intent);
                    return false;
                }
            });


        }else{
            Toast.makeText(this,"SD卡中没有图片",Toast.LENGTH_SHORT).show();

        }

    }

   // 获取手机图片地址列表
    private ArrayList<String> getImgPathList() {
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { "_id", "_data" }, null, null,  MediaStore.Images.Media._ID +" DESC");
        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));// 将图片路径添加到list中
        }
        cursor.close();
        return list;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FOR_START_CAMERA){
            saveUrl.add(0,mStrPhotoPath);//添加新的照片路径
            Log.d("ImageUrl", saveUrl.size()+"");
            picturesAdapter.notifyDataSetChanged();
        }


    }

    /***
     * 上传图片
     * @param Urls
     */
    private void uploadPic(List<String> Urls){
        Map<String,Object> param=new HashMap<String, Object>();
        param.put("imageUrl",Urls);
        param.put("requestType", Constant.REQEUSR_TYPE);
        OkHttpHelper.getInstance().post(Constant.UPLOAD_PIC, param, new BaseCallback<ResponseBean>() {
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
            public void onSuccess(Response response, ResponseBean responseBean) {

                int retrunCode = responseBean.getRet_code();
                if (retrunCode==200) {
                    Toast.makeText(getApplication(), responseBean.getRet_msg(), Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplication(), responseBean.getRet_msg(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onError(Response response, int code, Exception e) {


            }
        });


    }

    private String getFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("_yyyyMMdd_HHmmss");
        return dateFormat.format(date);
    }

    /****
     * 开启相机
     */
    private void startCamera(){
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try{
                File mPhotoFile = new File(Constant.IMAG_DIR);
                if(!mPhotoFile.exists()){
                    mPhotoFile.mkdirs();
                }
                mStrPhotoPath = Constant.IMAG_DIR + getFileName()+ ".jgp";
                Log.d("pic", "pic file path:" + mStrPhotoPath);
              //  intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mStrPhotoPath)));

                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(this,new File(mStrPhotoPath)));


                startActivityForResult(intent, FOR_START_CAMERA);
            }catch(Exception e){
                Log.d("pic", "pic file path:" +  e.toString());

                Toast.makeText(getApplicationContext(), "启动失败："+ e.toString(),Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "sd卡不存在", Toast.LENGTH_LONG).show();
        }
    }
    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }





}
