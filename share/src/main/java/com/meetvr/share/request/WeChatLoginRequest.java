package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/24.
 */
public class WeChatLoginRequest extends Request {

    public WeChatLoginRequest(){
        super(Constants.Get_USER_HTTP(),Constants.Get_USER_API_VERSION(), Request.servicename.user,"weixin_login");
    }

    public void setUserCode(String userCode){
        addJsonObj("user_code",userCode);
    }
    public void setPwd(String pwd){
        addJsonObj("password",pwd);
    }
    public void addVersion(String version){
        addJsonObj("app_ver",version);
    }
    public void adddev_infon(String dev_info){
        addJsonObj("dev_info",dev_info);
    }
    public void addos_vern(String os_ver){
        addJsonObj("os_ver",os_ver);
    }
    public void setWeixin_token(String weixin_token){
        addJsonObj("weixin_token",weixin_token);
    }

}
