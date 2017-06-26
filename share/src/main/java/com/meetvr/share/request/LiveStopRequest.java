package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/26.
 */
public class LiveStopRequest extends Request {
    public LiveStopRequest(String token, String identifier){
        super(Constants.Get_LIVEW_HTTP(),Constants.Get_LIVE_API_VERSION(), Request.servicename.live,"live_end");
        addNetUrl("?token="+token+"&identifier="+identifier);
    }
    public void addParam(String live_history_id){
        addJsonObj("live_history_id",live_history_id);
    }

}
