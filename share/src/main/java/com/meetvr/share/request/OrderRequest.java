package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/9/1.
 */
public class OrderRequest extends Request{
    /*
       @param
       token：登录返回的token
       identifier：用户的唯一ID
     */
    public OrderRequest(String token, String identifier){
        super(Constants.Get_BILL_HTTP(),Constants.Get_BILL_API_VERSION(), Request.servicename.bill,"order_submit");
        addNetUrl("?token="+token+"&identifier="+identifier);
    }
    public void addParam(String card_id ,String pay_way){
        addJsonObj("card_id",card_id);
        addJsonObj("pay_way",pay_way);
    }

}
