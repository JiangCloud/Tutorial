package com.cloud.xtilus.makingfriends.activity.moments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.alibaba.sdk.android.oss.ClientException;
//import com.alibaba.sdk.android.oss.ServiceException;
//import com.alibaba.sdk.android.oss.model.PutObjectRequest;
//import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.cloud.xtilus.makingfriends.R;
import com.cloud.xtilus.makingfriends.activity.moments.publish.GifSizeFilter;
import com.cloud.xtilus.makingfriends.activity.moments.publish.MomentsPublishActivity;
import com.cloud.xtilus.makingfriends.application.MakingFriendApplication;
import com.cloud.xtilus.makingfriends.bean.MomentsMessageDao;
import com.cloud.xtilus.makingfriends.http.OkHttpUtils;
import com.cloud.xtilus.makingfriends.util.ACache;
import com.cloud.xtilus.makingfriends.util.CommonUtils;
import com.cloud.xtilus.makingfriends.util.Constant;
import com.cloud.xtilus.makingfriends.util.DateHelper;
import com.cloud.xtilus.makingfriends.util.HTPathUtils;
import com.cloud.xtilus.makingfriends.util.Param;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
//import com.htmessage.sdk.utils.UploadFileUtils;









import java.io.File;
import java.util.ArrayList;
import java.util.List;


import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by huangfangyi on 2017/7/11.
 * qq 84543217
 */

public class MomentsPresenter implements MomentsContract.Presenter {
    private MomentsContract.View monmentsView;

    private static final String isFriend = "0";//默认经过好友关系查询
    private static final String isFold = "1";//默认返回
    private static final String category = "0";//默认没有经纬度0
    private List<JSONObject> data = new ArrayList<>();
    private String currentTime;
    private File cameraFile;
    private String backgroudMoment = "";
    private String cacheKeyBg ;
    private String cacheKeyTime ;
    private String cacheKey ;
    private  String userId;
    private OSS oss;
    private static final int REQUEST_CODE_CAMERA = 0;
    private static final int REQUEST_CODE_LOCAL = 1;
    private static final int REQUEST_CODE_PUBLISH= 2;
    private static final int REQUEST_CODE_CAMERA_FOR_BG = 3;
    private static final int REQUEST_CODE_LOCAL_FOR_BG = 4;

    private MomentsMessageDao momentsMessageDao;

     public MomentsPresenter(MomentsContract.View view) {
        monmentsView = view;
        monmentsView.setPresenter(this);
         momentsMessageDao=new MomentsMessageDao(view.getBaseContext());
         cacheKey = "moments"+MakingFriendApplication.getInstance().getUsername();
         cacheKeyBg = MakingFriendApplication.getInstance().getUsername() +"momentBg";
         cacheKeyTime = MakingFriendApplication.getInstance().getUsername()+"time";
         userId  = MakingFriendApplication.getInstance().getUserJson().getString(Constant.JSON_KEY_HXID);
         oss=MakingFriendApplication.getInstance().getOss();

    }

    @Override
    public void start() {

    }


    @Override
    public List<JSONObject> getData() {
        JSONArray jsonArray = ACache.get(monmentsView.getBaseContext()).getAsJSONArray(cacheKey);
        if (jsonArray!=null){
            List<JSONObject> list = JSONArray.parseArray(jsonArray.toJSONString(), JSONObject.class);
            data.addAll(list);
        }
        return data;
    }
    public String getBackgroudMoment(){
        String backgroud = ACache.get(monmentsView.getBaseContext()).getAsString(cacheKeyBg);
        if (!TextUtils.isEmpty(backgroud)){
            backgroudMoment =backgroud;
        }
        return backgroudMoment;
    }

    @Override
    public void getCacheTime() {
        String time = ACache.get(monmentsView.getBaseContext()).getAsString(cacheKeyTime);
        if (TextUtils.isEmpty(time)){
            currentTime = DateHelper.getStringTime(System.currentTimeMillis());
        }else{
            currentTime = time;
        }
        monmentsView.refreshListView(currentTime);
    }

    @Override
    public void loadeData(final int pageIndex) {
        Context context = monmentsView.getBaseContext();
        List<Param> params = new ArrayList<>();
        params.add(new Param("isFriend", isFriend));
        params.add(new Param("category", category));
        params.add(new Param("pageNum", pageIndex + ""));
        params.add(new Param("pageSize", 20 + ""));
        params.add(new Param("isFold", isFold));
        params.add(new Param("userId",userId));
        new OkHttpUtils(context).post(params, Constant.URL_SOCIAL, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                int code = jsonObject.getInteger("ret_code");
                switch (code) {
                    case 200:
                        JSONArray jsonArray = jsonObject.getJSONArray("ret_data");
                        currentTime = jsonObject.getString("time");
                        String background ="";
                        if (jsonObject.containsKey("backgroundImage")){
                            background = jsonObject.getString("backgroundImage");
                        }
                        backgroudMoment =background;
                        List<JSONObject> list = JSONArray.parseArray(jsonArray.toJSONString(), JSONObject.class);
                        if (pageIndex == 0) {
                            data.clear();
                            ACache.get(monmentsView.getBaseContext()).put(cacheKey,jsonArray);
                            ACache.get(monmentsView.getBaseContext()).put(cacheKeyBg,background);
                            ACache.get(monmentsView.getBaseContext()).put(cacheKeyTime,currentTime);
                        }
                        data.addAll(list);
                        monmentsView.refreshListView(currentTime);
                        break;
                    case -1:
                        Toast.makeText(monmentsView.getBaseContext(), R.string.just_nothing, Toast.LENGTH_SHORT).show();
                        break;
                }
                monmentsView.onRefreshComplete();
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(monmentsView.getBaseContext(), monmentsView.getBaseContext().getResources().getString(R.string.request_failed_msg) + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void setGood(final int position, final String aid,final String fxId) {
        // 更新后台
        List<Param> params = new ArrayList<>();
        params.add(new Param("userId", userId));
        params.add(new Param("mid", aid));
        new OkHttpUtils(monmentsView.getBaseContext()).post(params, Constant.URL_SOCIAL_GOOD, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("ret_code");
                switch (code) {
                    case 200:
                        JSONObject tempData=data.get(position);

                        String userId=tempData.getString("userId");
                        String imagestr=tempData.getString("imageStr");

                        String imageUrl="";
                        if (!TextUtils.isEmpty(imagestr)) {
                            String[] images = imagestr.split(",");
                            if(images!=null){
                                imageUrl= images[0];
                            }
                        }
                        JSONArray praises = jsonObject.getJSONArray("praises");
                        monmentsView.updateGoodView(position, praises);
                        momentsMessageDao.sendMomentsCmd(imageUrl,aid,null,0,fxId);
                        break;
                    case -1:
                        Toast.makeText(monmentsView.getBaseContext(), R.string.praise_failed, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(monmentsView.getBaseContext(), R.string.praise_failed, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(monmentsView.getBaseContext(), R.string.praise_failed_msg + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void comment(final int position, final String aid, final String content,final String fxId) {

        List<Param> params = new ArrayList<>();
       // params.add(new Param("tid", aid));


        params.add(new Param("userId", userId));
        params.add(new Param("mid", aid));
        params.add(new Param("cotent", content));
        new OkHttpUtils(monmentsView.getBaseContext()).post(params, Constant.URL_SOCIAL_COMMENT, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("ret_code");
                switch (code) {
                    case 200:
                        JSONArray comments = jsonObject.getJSONArray("comment");
                        monmentsView.updateCommentView(position, comments);
                        JSONObject tempData=data.get(position);
                        String userId=tempData.getString("userId");

                        String imagestr=tempData.getString("imageStr");
                        String imageUrl="";
                        if (!TextUtils.isEmpty(imagestr)) {
                            String[] images = imagestr.split(",");
                            if(images!=null){
                                imageUrl= images[0];
                            }
                        }
                        momentsMessageDao.sendMomentsCmd(imageUrl,aid,content,1,fxId);
                      //  momentsMessageDao.sendMomentsCmd(imageUrl,aid,content,1,userId);
                        break;
                    case -1:
                        Toast.makeText(monmentsView.getBaseContext(), R.string.service_not_response, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(monmentsView.getBaseContext(), R.string.service_not_response, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(monmentsView.getBaseContext(), R.string.service_not_response, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void cancelGood(final int position, String gid) {
        List<Param> params = new ArrayList<>();
        params.add(new Param("pid", gid));
        new OkHttpUtils(monmentsView.getBaseContext()).post(params, Constant.URL_SOCIAL_GOOD_CANCEL, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("ret_code");
                switch (code) {
                    case 200:
                        JSONArray praises = jsonObject.getJSONArray("praises");
                        monmentsView.updateGoodView(position, praises);
                        break;

                    default:
                        Toast.makeText(monmentsView.getBaseContext(), R.string.praise_cancle_fail, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(monmentsView.getBaseContext(), R.string.praise_cancle_fail_msg + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void deleteComment(final int position, String cid) {
        List<Param> params = new ArrayList<>();
        params.add(new Param("cid", cid));
        new OkHttpUtils(monmentsView.getBaseContext()).post(params, Constant.URL_SOCIAL_DELETE_COMMENT, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("ret_code");
                switch (code) {
                    case 200:
                        JSONArray comments = jsonObject.getJSONArray("comment");
                        monmentsView.updateCommentView(position, comments);
                        break;
                    default:
                        Toast.makeText(monmentsView.getBaseContext(), R.string.delete_comment_failed, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(monmentsView.getBaseContext(), R.string.delete_comment_failed_msg + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBarRightViewClicked() {
        monmentsView.showPicDialog(1, monmentsView.getBaseContext().getString(R.string.push_moment));
    }

    @Override
    public void deleteItem(final int position, String aid) {
        final List<Param> params = new ArrayList<>();
        params.add(new Param("mid", aid));
        new OkHttpUtils(monmentsView.getBaseContext()).post(params, Constant.URL_SOCIAL_DELETE, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code = jsonObject.getIntValue("ret_code");
                switch (code) {
                    case 200:
                       String fileNames=  jsonObject.getString("ret_data");
                        data.remove(position);
                        deleteFiles(fileNames);//删除说说时删除图片
                        monmentsView.refreshListView(null);
                        break;

                    default:
                        Toast.makeText(monmentsView.getBaseContext(), R.string.delete_dynamic_failed, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(monmentsView.getBaseContext(), R.string.delete_dynamic_failed_msg + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private  void  deleteFiles(String fileNames){


          String []names=  fileNames.split(",");

           for(String name:names){
               Log.d("fileName","file====================================="+name);
               // 创建删除请求
               DeleteObjectRequest delete = new DeleteObjectRequest(Constant.BUCKET_NAME, name);
               // 异步删除
               OSSAsyncTask deleteTask = oss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
                   @Override
                   public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                       Log.d("asyncCopyAndDelObject", "success!");
                   }

                   @Override
                   public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                       // 请求异常
                       if (clientExcepion != null) {
                           // 本地异常如网络异常等
                           clientExcepion.printStackTrace();
                       }
                       if (serviceException != null) {
                           // 服务异常
                           Log.e("ErrorCode", serviceException.getErrorCode());
                           Log.e("RequestId", serviceException.getRequestId());
                           Log.e("HostId", serviceException.getHostId());
                           Log.e("RawMessage", serviceException.getRawMessage());
                       }
                   }

               });
           }







    }















    @Override
    public void startToPhoto(int type) {
        if (!CommonUtils.isSdcardExist()) {
            CommonUtils.showToastShort(monmentsView.getBaseContext(), R.string.sd_card_does_not_exist);
            return;
        }

        cameraFile = new File(new HTPathUtils("monent", monmentsView.getBaseContext()).getImagePath() + "/" + MakingFriendApplication.getInstance().getUsername()
                + System.currentTimeMillis() + ".png");
        if (type == 1){
            monmentsView.getBaseActivity().startActivityForResult(
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                    REQUEST_CODE_CAMERA);
        }else{
            monmentsView.getBaseActivity().startActivityForResult(
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                    REQUEST_CODE_CAMERA_FOR_BG);
        }
    }

    @Override
    public void startToAlbum(int type) {
        if (type ==1){


            Matisse.from(monmentsView.getBaseActivity())
                    .choose(MimeType.ofAll(),false)
                    .countable(true)
                    .capture(true)
                    .captureStrategy(new CaptureStrategy(true,"com.cloud.xtilus.makingfriends.provider"))
                    .maxSelectable(9)
                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .gridExpectedSize(monmentsView.getBaseActivity().getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    //.setOnSelectedListener((uriList, pathList) -> {
                      //  Log.e("onSelected", "onSelected: pathList=" + pathList);
                   // })
                    //.setOnCheckedListener(isChecked -> )
                    .showPreview(false) // Default is `true`
                    .setOnCheckedListener(isChecked -> {
                        Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                    })
                    .forResult(REQUEST_CODE_LOCAL);

/*

            PhotoPicker.builder()
                    .setPhotoCount(9)
                    .setShowCamera(true)
                    .setShowGif(true)
                    .setPreviewEnabled(false)
                    .start(monmentsView.getBaseActivity() , REQUEST_CODE_LOCAL);*/
        }else{




            Matisse.from(monmentsView.getBaseActivity())
                    .choose(MimeType.ofAll())
                    .countable(true)
                    .maxSelectable(1)
                   // .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .gridExpectedSize(monmentsView.getBaseActivity().getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    //.setOnSelectedListener((uriList, pathList) -> {
                    //  Log.e("onSelected", "onSelected: pathList=" + pathList);
                    // })
                    //.setOnCheckedListener(isChecked -> )
                    .showPreview(false) // Default is `true`
                    .setOnCheckedListener(isChecked -> {
                        Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                    })
                   .forResult(REQUEST_CODE_LOCAL_FOR_BG);
  /*          PhotoPicker.builder()
                    .setPhotoCount(1)
                    .setShowCamera(false)
                    .setShowGif(false)
                    .setPreviewEnabled(false)
                    .start(monmentsView.getBaseActivity() , REQUEST_CODE_LOCAL_FOR_BG);*/
        }
    }
    @Override
    public void onResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                if (cameraFile != null && cameraFile.exists()) {
                    List<String> list = new ArrayList<>();
                    list.add(cameraFile.getAbsolutePath());
                    startToPublishActivity((ArrayList<String>) list);
                }
            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (intent != null) {
                    ArrayList<String> list = (ArrayList<String>) Matisse.obtainPathResult(intent);
                   // ArrayList<String> list = intent.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    if (list != null) {
                        startToPublishActivity(list);
                    }
                }
            }else if (requestCode == REQUEST_CODE_PUBLISH) { // send local image
                if (intent != null) {
                    String  jsonStr =  intent.getStringExtra("data");
                     if (jsonStr != null) {
                         data.add(0, JSONObject.parseObject(jsonStr));
                         monmentsView.refreshListView(null);
                     }
                }
            }else  if (requestCode == REQUEST_CODE_CAMERA_FOR_BG) { //更新志工圈背景
                if (cameraFile != null && cameraFile.exists()) {
                    String path = cameraFile.getAbsolutePath();
                    updateMomentBackGround(path);
                }
            } else if (requestCode == REQUEST_CODE_LOCAL_FOR_BG) { //相册选择背景 更新志工圈背景
                if (intent != null) {
                    List<String> list =  Matisse.obtainPathResult(intent);
                   // ArrayList<String> list = intent.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    if (list != null && list.size()!=0) {
                        String path = list.get(0);
                        updateMomentBackGround(path);
                    }
                }
            }
        }
    }

    private void startToPublishActivity(ArrayList<String> urls) {
        Intent intent = new Intent();
        intent.setClass(monmentsView.getBaseContext(), MomentsPublishActivity.class);
        intent.putStringArrayListExtra("pathList", urls);
        monmentsView.getBaseActivity().startActivityForResult(intent,REQUEST_CODE_PUBLISH);
    }

    @Override
    public String getCurrentTime() {
        return currentTime;
    }


    private void updateMomentBackGround(final String value){
        if (TextUtils.isEmpty(value)){
            return;
        }
        final Dialog progressDialog = MakingFriendApplication.getInstance().createLoadingDialog(monmentsView.getBaseContext(), monmentsView.getBaseContext().getString(R.string.are_uploading));
        progressDialog.show();
        final String fileName = value.substring(value.lastIndexOf("/") + 1);
        Luban.get(monmentsView.getBaseContext()).load(new File(value)).putGear(Luban.THIRD_GEAR).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(final File file) {
                monmentsView.getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // uploadImageback(fileName,file.getAbsolutePath(),progressDialog);
                        uploadImageback(fileName,value,progressDialog);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                monmentsView.getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonUtils.showToastShort(monmentsView.getBaseContext(),R.string.update_failed);

                    }
                });
            }
        }).launch();
    }



    private void uploadImageback( final String fileName, String filePath, final Dialog progressDialog) {

        PutObjectRequest put = new PutObjectRequest("showshop", fileName, filePath);

        // You can set progress callback during asynchronous upload
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {

                final String url = Constant.baseImgUrl + fileName;
                monmentsView.getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uploadBackground(url,progressDialog);
                    }
                });
                Log.d("PutObject", "UploadSuccess");
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // Request exception
                monmentsView.getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });

                if (clientExcepion != null) {
                    // Local exception, such as a network exception
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // Service exception
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

// task.cancel(); // Cancel the task

// task.waitUntilFinished(); // Wait till the task is finished

    }

/*





    private void uploadImageback(final String fileName, String value, final Dialog progressDialog){
        new UploadFileUtils(monmentsView.getBaseContext(), fileName, value).asyncUploadFile(new UploadFileUtils.a() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

            }

            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                final String url = Constant.baseImgUrl + fileName;
                monmentsView.getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uploadBackground(url,progressDialog);
                    }
                });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                monmentsView.getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
*/



    private void uploadBackground(final String url,final Dialog progressDialog) {
        List<Param> params = new ArrayList<Param>();
        params.add(new Param("backImageUrl", url));
        params.add(new Param("userId", userId));
        new OkHttpUtils(monmentsView.getBaseContext()).post(params, Constant.URL_UPLOAD_MOMENT_BACKGROUND, new OkHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                int code = jsonObject.getIntValue("ret_code");
                switch (code) {
                    case 200:
                        monmentsView.showBackground(url);
                        backgroudMoment = url;
                        ACache.get(monmentsView.getBaseContext()).put(cacheKeyBg,url);
                        CommonUtils.showToastShort(monmentsView.getBaseContext(),R.string.update_success);
                        break;
                    default:
                        CommonUtils.showToastShort(monmentsView.getBaseContext(),R.string.update_failed);
                        break;
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                CommonUtils.showToastShort(monmentsView.getBaseContext(),R.string.update_failed);
                progressDialog.dismiss();
            }
        });
    }
}
