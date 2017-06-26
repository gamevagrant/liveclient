package com.meetvr.share.request;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.meetvr.share.MyApplication;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wzm-pc on 2016/8/24.
 */
public class Request {
    public static enum  servicename{user,live,bill,im,client,point,activity};
    public interface OnNetWorkProc{
        void onStart();
        void onRespon(JSONObject response, boolean isOK, String errMsg);
    }
    private String domain;
    private String version;
    private String servicename;
    private String command;
    private String netUrl="";
    private JSONObject jsonObject = new JSONObject();
    private JsonObjectRequest jRequest;
    private MyApplication app;
    Request(String domain, String version, servicename sname, String command){
        this.domain = domain;
        this.version = version;
        this.servicename = sname.name();
        this.command = command;
        addNetUrl(this.domain);
        addNetUrl("/"+this.version);
        addNetUrl("/"+this.servicename);
        addNetUrl("/"+this.command);
    }
public String getJsonString(){
    try {
        return  jsonObject.toString();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return  "";
}
public  String getUrl(){
    return netUrl;
}
    public void addNetUrl(String urlValue){
        netUrl=netUrl +urlValue;
    }


    public void addJsonObj(String key, Object value){
        try {
            if(value!=null)
                jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addJsonObj(String key, String value){
        try {
            if(value!=null)
                jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void addJsonObj(String key, boolean value){
        try {
            jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addJsonObj(String key, int value){
        try {
            jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void addJsonObj(String key, float value){
        try {
            jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addQuery(Context context, final OnNetWorkProc netWorkProc){
        try {
            if(netWorkProc!=null)
                netWorkProc.onStart();
            MyApplication application =(MyApplication) context.getApplicationContext();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(netUrl, jsonObject,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(netWorkProc!=null){
                                    netWorkProc.onRespon(response,true,"ok");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try {
                        if(netWorkProc!=null){
                            netWorkProc.onRespon(null,false,error.getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            //jsonObjectRequest.setTag(this.getClass());
            //jsonObjectRequest.getHeaders().put("Content-Type","application/json; charset=utf-8");
           // jsonObjectRequest.
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
            application.callNet(jsonObjectRequest);

            jRequest = jsonObjectRequest;
            app = application;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void chanelRequest(){
        if(app!=null){
            app.chanel(jRequest);
        }
    }


    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }


}
