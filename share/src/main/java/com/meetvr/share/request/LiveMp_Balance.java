package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/9/1.
 */
public class LiveMp_Balance extends Request{
    /*
       @param
       token：登录返回的token
       identifier：用户的唯一ID
     */
    public LiveMp_Balance(String token, String identifier){
        super(Constants.Get_BILL_HTTP(),Constants.Get_BILL_API_VERSION(), Request.servicename.bill,"user_account");
        addNetUrl("?token="+token+"&identifier="+identifier);
    }
    public void addParam(String host_id){
        addJsonObj("user_id",host_id);
    }
}
