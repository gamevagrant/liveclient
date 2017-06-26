package com.meetvr.share.info;

/**
 * Created by wzm-pc on 2016/10/14.
 */

public class UserRegistInfo {
    String phone="";
    String password="";
    String device_id="";
    String  vcode="";


     String   app_ver ;//:"1.10.1",
     String   dev_info; //:"手机型号、内存、屏幕信息",
     String   os_ver;//:"系统版本"
     String   weixin_token;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getApp_ver() {
        return app_ver;
    }

    public void setApp_ver(String app_ver) {
        this.app_ver = app_ver;
    }

    public String getDev_info() {
        return dev_info;
    }

    public void setDev_info(String dev_info) {
        this.dev_info = dev_info;
    }

    public String getOs_ver() {
        return os_ver;
    }

    public void setOs_ver(String os_ver) {
        this.os_ver = os_ver;
    }

    public String getWeixin_token() {
        return weixin_token;
    }

    public void setWeixin_token(String weixin_token) {
        this.weixin_token = weixin_token;
    }
}
