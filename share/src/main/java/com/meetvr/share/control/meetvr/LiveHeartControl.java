package com.meetvr.share.control.meetvr;

import android.content.Context;
import android.content.res.Resources;

import com.meetvr.share.info.UserInfo;
import com.meetvr.share.request.LiveHeartBeatRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.view.ProgessDlg;

import org.json.JSONObject;

/**
 * Created by wzm-pc on 2016/8/31.
 */
public class LiveHeartControl {
    private Context context;
    private ProgessDlg progessDlg;
    private void hideProgress(){
        if(progessDlg!=null){
            progessDlg.dismiss();
            progessDlg=null;
        }
    }
        public LiveHeartControl(final Context context, UserInfo userInfo, String his_id, final ProgessDlg progessDlg){
            this.context = context;
            this.progessDlg = progessDlg;
            try {
                if(userInfo!=null){
                    LiveHeartBeatRequest liveHeartBeatRequest = new LiveHeartBeatRequest(userInfo.getToken(),userInfo.getId());
                    liveHeartBeatRequest.addParam(userInfo.getId(),his_id);
                    liveHeartBeatRequest.addQuery(context, new Request.OnNetWorkProc() {
                        @Override
                        public void onStart() {
                            if(progessDlg!=null)
                                progessDlg.showPopupWindow();
                        }

                        @Override
                        public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                            try {
                                hideProgress();
                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    //Toast.makeText(context,"登录的时候没有获取到用户信息，可能是登录服务么启动！",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

}
