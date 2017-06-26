package com.meetvr.share.request;


import com.meetvr.share.utrils.Constants;

/**
 * Created by wzm-pc on 2016/8/26.
 */
public class ImSigNewRequest extends Request {
    public ImSigNewRequest(String token, String identifier){
        super(Constants.Get_USER_HTTP(),Constants.Get_USER_API_VERSION(), Request.servicename.user,"im_sig_get");
        addNetUrl("?token="+token+"&identifier="+identifier);
    }



}
