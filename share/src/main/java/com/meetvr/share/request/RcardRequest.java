package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/9/1.
 */
public class RcardRequest extends Request{
    /*
       @param
       token：登录返回的token
       identifier：用户的唯一ID
     */
    public RcardRequest(String token, String identifier){
        super(Constants.Get_BILL_HTTP(),Constants.Get_BILL_API_VERSION(), Request.servicename.bill,"recharge_card");
        addNetUrl("?token="+token+"&identifier="+identifier);
        addJsonObj("start",0);
        addJsonObj("count",1000);
    }

}
