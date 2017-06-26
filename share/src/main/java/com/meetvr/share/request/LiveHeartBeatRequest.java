package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/26.
 */
public class LiveHeartBeatRequest extends Request {
    public LiveHeartBeatRequest(String token, String identifier){
        super(Constants.Get_LIVEW_HTTP(),Constants.Get_LIVE_API_VERSION(), Request.servicename.live,"heartbeat");
        addNetUrl("?token="+token+"&identifier="+identifier);
    }
    public void addParam(String host_id, String live_history_id){
        addJsonObj("host_id",host_id);
        addJsonObj("live_history_id",live_history_id);
        addJsonObj("heart_type","host_app");
    }

}
