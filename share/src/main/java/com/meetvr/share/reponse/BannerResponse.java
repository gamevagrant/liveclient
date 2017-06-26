package com.meetvr.share.reponse;

import com.meetvr.share.control.vr_interface.BaseResponse;
import com.meetvr.share.info.BannerInfo;
import com.meetvr.share.info.RcardInfo;
import com.meetvr.share.utrils.Mutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wzm-pc on 2016/10/19.
 */

public class BannerResponse extends Response implements BaseResponse {

    private ArrayList<BannerInfo> datas = new ArrayList<BannerInfo>();
    public ArrayList<BannerInfo> getDatas(){
        return datas;
    }
    @Override
    public void paseJson(JSONObject resp) {
       JSONArray jsonArray =  super.parseArrsy(resp);
        if(jsonArray!=null&& Mutils.isResponseOk(getAct_stat())){
            for(int i=0;i<jsonArray.length();i++){
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    BannerInfo bannerInfo = new BannerInfo();
                    bannerInfo.setId(Mutils.getJsonInt(object,"id"));
                    bannerInfo.setBanner_img(Mutils.getJsonString(object,"banner_img"));
                    bannerInfo.setBanner_name(Mutils.getJsonString(object,"banner_name"));
                    bannerInfo.setBanner_url(Mutils.getJsonString(object,"banner_url"));
                    bannerInfo.setCreate_time(Mutils.getJsonLong(object,"create_time"));
                    bannerInfo.setEnd_time(Mutils.getJsonLong(object,"end_time"));
                    bannerInfo.setSort_id(Mutils.getJsonInt(object,"sort_id"));
                    bannerInfo.setStart_time(Mutils.getJsonLong(object,"start_time"));
                    bannerInfo.setUpdate_time(Mutils.getJsonLong(object,"update_time"));
                    if(bannerInfo.getBanner_img().length()>2){
                        datas.add(bannerInfo);
                    }

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
