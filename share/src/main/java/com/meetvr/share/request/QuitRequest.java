package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/24.
 */
public class QuitRequest extends Request {

    public QuitRequest(String token, String identifier){
        super(Constants.Get_USER_HTTP(),Constants.Get_USER_API_VERSION(), Request.servicename.user,"user_quit");

        addNetUrl("?token="+token+"&identifier="+identifier);
        addJsonObj("app_type","host_app");
    }



}
