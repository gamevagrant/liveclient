package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/26.
 */
public class ModifyPwdRequest extends Request {
    public ModifyPwdRequest(String token, String identifier,String user_id,String old_password, String new_password){
        super(Constants.Get_USER_HTTP(),Constants.Get_USER_API_VERSION(), Request.servicename.user,"password_set");
        addNetUrl("?token="+token+"&identifier="+identifier);

        addJsonObj("user_id",user_id);
        addJsonObj("old_password",old_password);
        addJsonObj("new_password",new_password);
    }

    public ModifyPwdRequest(String phone,String vcode,String password){
        super(Constants.Get_USER_HTTP(),Constants.Get_USER_API_VERSION(), Request.servicename.user,"password_reset");
        addJsonObj("phone",phone);
        addJsonObj("vcode",vcode);
        addJsonObj("password",password);
    }

}
