package com.meetvr.share.reponse;

import com.meetvr.share.control.vr_interface.BaseResponse;
import com.meetvr.share.info.RcardInfo;
import com.meetvr.share.utrils.Mutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wzm-pc on 2016/10/19.
 */

public class RcardResponse extends Response implements BaseResponse {

    private ArrayList<RcardInfo> datas = new ArrayList<RcardInfo>();
    public ArrayList<RcardInfo> getDatas(){
        return datas;
    }
    @Override
    public void paseJson(JSONObject resp) {
       JSONArray jsonArray =  super.parseArrsy(resp);
        if(jsonArray!=null&& Mutils.isResponseOk(getAct_stat())){
            for(int i=0;i<jsonArray.length();i++){
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    RcardInfo rcardInfo = new RcardInfo();
                    rcardInfo.setId(Mutils.getJsonInt(object,"id"));
                    rcardInfo.setMb_num(Mutils.getJsonInt(object,"mb_num"));
                    rcardInfo.setCard_name(Mutils.getJsonString(object,"card_name"));
                    rcardInfo.setCard_pic(Mutils.getJsonString(object,"card_pic"));
                    rcardInfo.setDiscount_mb_num(Mutils.getJsonInt(object,"discount_mb_num"));
                    rcardInfo.setDiscount_ratio(Mutils.getJsonInt(object,"discount_ratio"));
                    rcardInfo.setPrice((float) Mutils.getJsonFloat(object,"price"));
                    datas.add(rcardInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public String getAct_stat() {
        return super.getAct_stat();
    }

    @Override
    public String getErr_msg() {
        return super.getErr_msg();
    }

    @Override
    public int getErrCcode() {
        return super.getErr_code();
    }
}
