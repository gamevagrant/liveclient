package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;


public class CheckRoomStateRequest extends Request {
    public CheckRoomStateRequest(String token, String identifier){
        super(Constants.Get_LIVEW_HTTP(),Constants.Get_LIVE_API_VERSION(), Request.servicename.live,"room_check");
        addNetUrl("?token="+token+"&identifier="+identifier);
    }

    public void addParam(int live_id,String live_stat){
        addJsonObj("live_id",live_id);
        addJsonObj("type",live_stat);
    }


}
