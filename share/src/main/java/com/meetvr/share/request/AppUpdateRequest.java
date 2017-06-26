package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/26.
 *
 客户端升级接口:
 http://api.moxiangtv.com/v1/client/upgrade
 http://user.api.moxiangtv.com/v1/client/upgrade  老的地址
 POST参数：
 client_version 客户端版本号 举例：1.0.0
 client_type 客户端类型 举例：user/host字段
 返回数据：
 upgrade 升级类型（0:不用升级； 1:普通升级；2:强制升级）
 vername	String	版本名称
 version	String	三段式版本号(1.0.1)
 url	String	升级包地址
 content	String	升级说明
 @荦枫  接一下这个接口

 */
public class AppUpdateRequest extends Request {
    public AppUpdateRequest(){
        super(Constants.Get_API_HTTP(),"v2", Request.servicename.client,"upgrade");
       // addNetUrl("?token="+token+"&identifier="+identifier);
        addJsonObj("client_type","user");
        addJsonObj("client_version",Constants.APP_VER);
    }

}
