package com.meetvr.share.reponse;



import com.meetvr.share.info.GiftInfo;
import com.meetvr.share.utrils.Mutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wzm-pc on 2016/9/1.
 */
public class GiftResponse extends Response {
    private volatile static GiftResponse instance;
    public static GiftResponse getInstance(){
        if (instance == null) {
            synchronized (GiftResponse.class) {
                if (instance == null) {
                    instance = new GiftResponse();
                }
            }
        }
        return instance;
    }
 synchronized    public void parseGift(JSONObject obj, ArrayList<GiftInfo> gifts){
        JSONArray jsonArray = super.parseArrsy(obj);
        if(!Mutils.isResponseOk(getAct_stat())) return;
         if(gifts.size()>0){
             gifts.clear();
         }
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject = getJsonobj(jsonArray,i);
            GiftInfo giftInfo = new GiftInfo();
            giftInfo.setGift_name(Mutils.getJsonString(jsonObject,"gift_name"));
            giftInfo.setPrice((float) Mutils.getJsonFloat(jsonObject,"price"));
            giftInfo.setUrl2d(Mutils.getJsonString(jsonObject,"url2d"));
            giftInfo.setId(Mutils.getJsonString(jsonObject,"id"));
            giftInfo.setGift_id(Mutils.getJsonString(jsonObject,"id"));
            giftInfo.setPackage_num(1);
            gifts.add(giftInfo);
//            JSONArray jsonArray1 = Mutils.getJsonArray(jsonObject,"gift_package");
//            for(int j=0;j<jsonArray1.length();j++){
//                JSONObject jobj = getJsonobj(jsonArray1,j);
//                    GiftInfo gInfo = new GiftInfo();
//                    gInfo.setUrl2d(giftInfo.getUrl2d());
//                    gInfo.setPrice(giftInfo.getPrice());
//                    gInfo.setGift_name(giftInfo.getGift_name());
//                    gInfo.setGift_id(Mutils.getJsonString(jobj,"gift_id"));
//                    gInfo.setId(Mutils.getJsonString(jobj,"id"));
//                    gInfo.setPackage_num(Mutils.getJsonInt(jobj,"package_num"));
//                    gifts.add(gInfo);
//            }

        }
    }


}
