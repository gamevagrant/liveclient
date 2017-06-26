package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/26.
 */
public class RegistRequest extends Request{
    public RegistRequest(){
        super(Constants.Get_USER_HTTP(),Constants.Get_USER_API_VERSION(), Request.servicename.user,"user_regist");
    }
    public void setRequestParam(String phone,String password,String device_id,String vcode){
        addJsonObj("phone",phone);
        addJsonObj("password",password);
        addJsonObj("device_id",device_id);
        addJsonObj("vcode",vcode);
    }

    public void setRequestLogin(String app_ver,String dev_info,String os_ver){
        addJsonObj("app_ver",app_ver);
        addJsonObj("dev_info",dev_info);
        addJsonObj("os_ver",os_ver);
    }

}
