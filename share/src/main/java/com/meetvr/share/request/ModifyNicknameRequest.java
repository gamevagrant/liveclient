package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/26.
 */
public class ModifyNicknameRequest extends Request {
    public ModifyNicknameRequest(String token, String identifier){
        super(Constants.Get_USER_HTTP(),Constants.Get_USER_API_VERSION(), Request.servicename.user,"myinfo_set");
        addNetUrl("?token="+token+"&identifier="+identifier);
    }

    public void setParam(String user_id, String nickname, String head_pic){
        addJsonObj("user_id",user_id);
        if(nickname!=null){
            addJsonObj("nickname",nickname);
        }

        if(head_pic!=null){
            addJsonObj("head_pic",head_pic);
        }
    }

}
