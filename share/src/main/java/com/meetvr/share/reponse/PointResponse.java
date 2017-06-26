package com.meetvr.share.reponse;

import com.meetvr.share.control.vr_interface.BaseResponse;

import org.json.JSONObject;

/**
 * Created by wzm-pc on 2016/12/1.
 */

public class PointResponse extends Response implements BaseResponse {
    @Override
    public void paseJson(JSONObject resp) {
       // super.parse(resp);
    }

    @Override
    public String getAct_stat() {
        return "ok";
    }

    @Override
    public String getErr_msg() {
        return "";
    }

    @Override
    public int getErrCcode() {
        return super.getErr_code();
    }
}
