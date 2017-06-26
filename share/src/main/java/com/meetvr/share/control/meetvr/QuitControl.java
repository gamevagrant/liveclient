package com.meetvr.share.control.meetvr;

import android.content.Context;
import android.content.res.Resources;


import com.meetvr.share.request.QuitRequest;
import com.meetvr.share.request.Request;

import org.json.JSONObject;

/**
 * Created by wzm-pc on 2016/8/31.
 */
public class QuitControl {
        public void quit(final Context context, String token, String identify){
            try {

                QuitRequest quitRequest = new QuitRequest(token,identify);
                quitRequest.addQuery(context, new Request.OnNetWorkProc() {
                        @Override
                        public void onStart() {

                        }
                        @Override
                        public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                            try {



                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    });


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

}
