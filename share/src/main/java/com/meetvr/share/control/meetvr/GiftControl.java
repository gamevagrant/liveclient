package com.meetvr.share.control.meetvr;

import android.content.Context;
import android.content.res.Resources;

import com.meetvr.share.info.GiftInfo;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.reponse.GiftResponse;
import com.meetvr.share.request.GiftRequest;
import com.meetvr.share.request.Request;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wzm-pc on 2016/8/31.
 */
public class GiftControl {
    private Context context;
    private volatile static GiftControl instance;
    private boolean isStart = false;
    private GiftControl(){
        //注册消息监听器

    }

    public static GiftControl getInstance(){
        if (instance == null) {
            synchronized (GiftControl.class) {
                if (instance == null) {
                    instance = new GiftControl();
                }
            }
        }
        return instance;
    }

        public void getGifts(final Context context, UserInfo userInfo, final ArrayList<GiftInfo> gifts){
            this.context = context;
            if(isStart) return;
            try {
                if(userInfo!=null){
                    GiftRequest giftRequest = new GiftRequest(userInfo.getToken(),userInfo.getId());
                    giftRequest.addQuery(context, new Request.OnNetWorkProc() {
                        @Override
                        public void onStart() {
                            isStart=true;
                        }
                        @Override
                        public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                            try {
                                if(isOK){
                                    GiftResponse.getInstance().parseGift(response,gifts);
                                }


                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            }
                            isStart = false;
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

}
