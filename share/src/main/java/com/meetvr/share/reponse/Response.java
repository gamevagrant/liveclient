package com.meetvr.share.reponse;

import android.util.Log;


import com.meetvr.share.utrils.Mutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wzm-pc on 2016/8/24.
 */
public class Response {
    private String act_stat;
    private String err_msg="";
    private int err_code;
    public JSONObject parse(String response){
        try {
            JSONObject jsonObject = Mutils.newJson(response);
            if(jsonObject!=null){
                Log.v("Response",response.toString());
                act_stat = Mutils.getJsonString(jsonObject,"act_stat");
                if(Mutils.isResponseOk(act_stat)){
                    return Mutils.getJsonObj(jsonObject,"data");
                }
                err_msg = Mutils.getJsonString(jsonObject,"err_msg");
                err_code = Mutils.getJsonInt(jsonObject,"err_code");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  null;
    }
    public JSONObject parse(JSONObject jsonObject){
        try {
            act_stat = Mutils.getJsonString(jsonObject,"act_stat");
            if(Mutils.isResponseOk(act_stat)){
                return Mutils.getJsonObj(jsonObject,"data");
            }
            err_msg = Mutils.getJsonString(jsonObject,"err_msg");
            err_code = Mutils.getJsonInt(jsonObject,"err_code");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public JSONArray parseArrsy(JSONObject object){

        try {
            act_stat = Mutils.getJsonString(object,"act_stat");
            if(Mutils.isResponseOk(act_stat)){
                return Mutils.getJsonArray(object,"data");
            }
            err_msg = Mutils.getJsonString(object,"err_msg");
            err_code = Mutils.getJsonInt(object,"err_code");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getAct_stat() {
        return act_stat;
    }

    public void setAct_stat(String act_stat) {
        this.act_stat = act_stat;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }
    protected JSONObject getJsonobj(JSONArray obj, int idx){
        try {
            return obj.getJSONObject(idx);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
