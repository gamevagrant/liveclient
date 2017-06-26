package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/26.
 */
public class Room_ListsRequest extends Request {
    public Room_ListsRequest(String token, String identifier){
        super(Constants.Get_LIVEW_HTTP(),Constants.Get_LIVE_API_VERSION(), Request.servicename.live,"room_list");
        addNetUrl("?token="+token+"&identifier="+identifier);
    }

    public void addParam(int start,int count){
        addJsonObj("start",start);
        addJsonObj("count",count);
        //addJsonObj("type","living");
    }


}
