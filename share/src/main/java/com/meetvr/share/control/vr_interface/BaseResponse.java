package com.meetvr.share.control.vr_interface;

import org.json.JSONObject;

/**
 * Created by wzm-pc on 2016/10/19.
 */

public interface BaseResponse {
   abstract  void  paseJson(JSONObject resp);
   abstract  String getAct_stat();
   abstract   String getErr_msg();
   abstract  int getErrCcode();
}
