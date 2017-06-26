package com.meetvr.share.reponse;



import com.meetvr.share.info.RicherData;
import com.meetvr.share.utrils.Mutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wzm-pc on 2016/9/1.
 */
public class RichListsResponse extends Response {
    public void  parseArraylist(JSONObject obj, ArrayList<RicherData> datas){
        try {
            JSONArray object = super.parseArrsy(obj);
            if(Mutils.isResponseOk(getAct_stat())){
                for(int i=0;i<object.length();i++){
                    JSONObject jsonObject = getJsonobj(object,i);
                    RicherData richerData = new RicherData();
                    richerData.setNickName(Mutils.getJsonString(jsonObject,"nickname"));
                    richerData.setMoney(Mutils.getJsonString(jsonObject,"mb_count"));
                    richerData.setHead_pic(Mutils.getJsonString(jsonObject,"head_pic"));
                    richerData.setSex(Mutils.getJsonString(jsonObject,"sex"));
                    richerData.setUser_code(Mutils.getJsonString(jsonObject,"user_code"));
                    richerData.setUser_id(Mutils.getJsonString(jsonObject,"user_id"));
                    datas.add(richerData);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
