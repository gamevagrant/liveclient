package com.meetvr.share;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.litesuits.orm.LiteOrm;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.utrils.BitmapCache;
import com.meetvr.share.utrils.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


import java.util.ArrayList;


/**
 * 全局Application
 */
public class MyApplication extends Application {
    private static final String TAG=MyApplication.class.getName();

    private static Context context;
    private static RequestQueue mQueue;
    private ImageLoader imageLoader;
    private BitmapCache bitmapCache =  new BitmapCache();

    private UserInfo userInfo;
    private LiteOrm db;



    @Override
    public void onCreate() {
        super.onCreate();

       // CrashHandler catchHandler = CrashHandler.getInstance();
       // catchHandler.init(getApplicationContext());

        context = getApplicationContext();

        mQueue = Volley.newRequestQueue(getApplicationContext());
        imageLoader = new ImageLoader(mQueue,bitmapCache);
        db = LiteOrm.newCascadeInstance(this,"meetvrdb");


    }
    public BitmapCache getBmpCache(){
        return bitmapCache;
    }
    public static void callNet(Request<?> request){
        try {
            mQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void chanel(Object tag){
        try {
            mQueue.cancelAll(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return context;
    }


    public void saveUser(UserInfo userInfo){
        if(this.userInfo==null){
            db.save(userInfo);
        }else{
            db.update(userInfo);
        }
        this.userInfo = userInfo;


    }
    public UserInfo getUserInfo(){
        return userInfo;
    }

    public void loadUserInfo(){
       ArrayList<UserInfo> users =  (ArrayList<UserInfo>)db.query(UserInfo.class);
        if(users!=null&&users.size()>0){
            userInfo = users.get(0);
        }
    }

    public void deleteUser(){
        db.delete(UserInfo.class);
        userInfo = null;
    }


    public void displayImg(String imgUrl, ImageView imageView, int defaultImg, int errImg){
        try {
          //  String headImg = "http://c.hiphotos.baidu.com/baike/c0%3Dbaike72%2C5%2C5%2C72%2C24/sign=8c5d53b2b01c8701c2bbbab44616f54a/8c1001e93901213f1a20a38a57e736d12f2e9551.jpg";
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,defaultImg,errImg);
            imageLoader.get(imgUrl,listener);
            //imageLoader.get(headImg,listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int GetChannelId()
    {
        try {
            ApplicationInfo appInfo= this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            if(appInfo.metaData.containsKey("CHANNEL_ID"))
            {
                return appInfo.metaData.getInt("CHANNEL_ID",0);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }




}
