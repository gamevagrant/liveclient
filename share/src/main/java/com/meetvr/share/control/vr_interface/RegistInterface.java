package com.meetvr.share.control.vr_interface;

import com.meetvr.share.reponse.Response;

/**
 * Created by wzm-pc on 2016/10/14.
 */

public interface RegistInterface {
    public void onResulte(boolean isOk);
    public void  onResulte(boolean isOk,BaseResponse baseResponse);
}
