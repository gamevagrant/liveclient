package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/26.
 */
public class BannerRequest extends Request {
    public BannerRequest(String token, String identifier){
        super(Constants.Get_LIVEW_HTTP(),Constants.Get_LIVE_API_VERSION(), Request.servicename.live,"banner_all");
        addNetUrl("?token="+token+"&identifier="+identifier);
    }
    public void addParam(int type){
        addJsonObj("banner_site",type);
    }
}
