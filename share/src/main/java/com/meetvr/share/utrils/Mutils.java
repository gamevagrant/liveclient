package com.meetvr.share.utrils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.meetvr.share.control.meetvr.Control;
import com.meetvr.share.reponse.PointResponse;
import com.meetvr.share.request.PointRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wzm-pc on 2016/8/23.
 */
public class Mutils {
    public static void setTitle(View view, String title){
        try {
            if(title!=null)
                 ((TextView)view).setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    public static void pointEnter(Context context, String usercoder , String name, String param , String value){

            PointRequest pointRequest = new PointRequest(usercoder,context);
            pointRequest.addArray(name,param,""+value);

            new Control(context,null,pointRequest,new PointResponse(),null);

    }
*/
    public static String getDeviceId(){

        return Build.SERIAL;
    }
    public static String getInfo(){
        return Build.MODEL;
    }


    public static JSONObject newJson(String content){
        try {
            if(content!=null&&content.length()>0){
                JSONObject jsonObject = new JSONObject(content);
                return jsonObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getJsonString(JSONObject jsonObject, String key){
        try {
            if(jsonObject!=null){
                String value = jsonObject.getString(key);
                return value;
            }
        } catch (JSONException e) {
          //  e.printStackTrace();
        }
        return "";
    }
    public static long getJsonLong(JSONObject jsonObject, String key){
        try {
            if(jsonObject!=null){
                long value = jsonObject.getLong(key);
                return value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getJsonInt(JSONObject jsonObject, String key){
        try {
            if(jsonObject!=null){
                int value = jsonObject.getInt(key);
                return value;
            }
        } catch (JSONException e) {
         //   e.printStackTrace();
        }
        return -1;
    }
    public static double getJsonFloat(JSONObject jsonObject, String key){
        try {
            if(jsonObject!=null){
                double value = jsonObject.getDouble(key);
                return value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1.0;
    }

    public static boolean getJsonBool(JSONObject jsonObject, String key){
        try {
            if(jsonObject!=null){
                boolean value = jsonObject.getBoolean(key);
                return value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static JSONArray getJsonArray(JSONObject jsonObject , String key){
        try {
            if(jsonObject!=null){
                JSONArray value = jsonObject.getJSONArray(key);
                return  value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static JSONObject getJsonObj(JSONObject jsonObject, String key){
        try {
            if(jsonObject!=null){
                JSONObject value = jsonObject.getJSONObject(key);
                return value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  static boolean isResponseOk(String content){
        try {
            return content.equals("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  false;
    }

    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHashString(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

    public static String getDataTime(Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }

    public static void hideAction(AppCompatActivity appCompatActivity) {
        // Hide UI first
        try {
            //
            ActionBar actionBar = appCompatActivity.getSupportActionBar();
            actionBar.hide();

        } catch (Exception e) {
            e.printStackTrace();
            //getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        }

    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
