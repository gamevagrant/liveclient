package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/26.
 */
public class LiveStartRequest extends Request {
    public LiveStartRequest(String token, String identifier){
        super(Constants.Get_LIVEW_HTTP(),Constants.Get_LIVE_API_VERSION(), Request.servicename.live,"live_start");
        addNetUrl("?token="+token+"&identifier="+identifier);
    }

}
