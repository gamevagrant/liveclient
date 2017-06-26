package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/26.
 */
public class CheckCoderRequest extends Request {
    public CheckCoderRequest(){
        super(Constants.Get_USER_HTTP(),Constants.Get_USER_API_VERSION(), Request.servicename.user,"vcode_check");
    }
    public void setCheckParam(String phone, String device_id, String vcode){
        try {
            addJsonObj("vcode",vcode);
            addJsonObj("device_id",device_id);
            addJsonObj("phone",phone);
            addJsonObj("app_type","vr_app");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
