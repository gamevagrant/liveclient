package com.meetvr.share.request;


import com.meetvr.share.info.UserInfo;
import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/9/1.
 */
public class ImSaveRequest extends Request{
    /*
       @param
       token：登录返回的token
       identifier：用户的唯一ID
     */
    public ImSaveRequest(String token, String identifier){
        super(Constants.Get_IM_HTTP(),Constants.Get_IM_API_VERSION(), Request.servicename.im,"save");
        addNetUrl("?token="+token+"&identifier="+identifier);
    }
    public void addParam(UserInfo userInfo, String his_id, String content, String status, long time_im){
        addJsonObj("room_id",userInfo.getRoomid());
        addJsonObj("room_type",1);
        addJsonObj("host_id",userInfo.getId());
        addJsonObj("live_record_id",his_id);
        addJsonObj("from",userInfo.getId());
        addJsonObj("to",userInfo.getRoomid());
        addJsonObj("msg_stat",status);
        addJsonObj("msg_body",content);
        addJsonObj("msg_type",1);
        addJsonObj("time_im",time_im);

    }

}
