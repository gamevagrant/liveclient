package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/26.
 */
public class VcodeRequest extends Request{
    public  VcodeRequest(){
        super(Constants.Get_USER_HTTP(),Constants.Get_USER_API_VERSION(), Request.servicename.user,"vcode_send");
        addJsonObj("app_type","vr_app");
    }
    public void setPhoneDevid(String phone, String device_id){
        addJsonObj("phone",phone);
        addJsonObj("device_id",device_id);
    }

    public void setTyoe(String b_type){
        addJsonObj("b_type",b_type);
    }
}
