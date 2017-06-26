package com.meetvr.share.reponse;

import com.meetvr.share.info.UserInfo;
import com.meetvr.share.utrils.Mutils;



import org.json.JSONObject;

/**
 * Created by wzm-pc on 2016/8/24.
 */
public class LoginResponse extends Response{
    public void parse(JSONObject jobj, UserInfo userInfo){
        try {
            JSONObject jsonObject = super.parse(jobj);
            if(Mutils.isResponseOk(getAct_stat())){
                userInfo.setId(Mutils.getJsonString(jsonObject,"userid"));
                //userInfo.setUsercode(Mutils.getJsonString(jsonObject,"user_code"));
                userInfo.setUsertype(Mutils.getJsonInt(jsonObject,"user_type"));
                userInfo.setAccountType(Mutils.getJsonInt(jsonObject,"out_user_type"));
                userInfo.setHeadphoto(Mutils.getJsonString(jsonObject,"head_pic"));
                userInfo.setUsername(Mutils.getJsonString(jsonObject,"nickname"));
                userInfo.setSex(Mutils.getJsonString(jsonObject,"sex"));
                userInfo.setPhone(Mutils.getJsonString(jsonObject,"phone"));
                // userInfo.setRemark(Mutils.getJsonString(jsonObject,"remark"));
                userInfo.setSig(Mutils.getJsonString(jsonObject,"sig"));
       //         userInfo.setLivetype(Mutils.getJsonInt(jsonObject,"live_enable_stat"));
       //         userInfo.setLiveurl(Mutils.getJsonString(jsonObject,"live_url"));
       //         userInfo.setRoomid(Mutils.getJsonString(jsonObject,"room_id"));
                userInfo.setToken(Mutils.getJsonString(jsonObject,"token"));
               userInfo.setQiniu_token(Mutils.getJsonString(jsonObject,"qiniu_token"));
                userInfo.setFirst_login(Mutils.getJsonInt(jsonObject,"first_login"));
      //          userInfo.setHeartbeat(Mutils.getJsonInt(jsonObject,"heartbeat"));
                userInfo.setQiniu_file_domain(Mutils.getJsonString(jsonObject,"qiniu_file_domain"));
            }
            userInfo.jsonValue = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
