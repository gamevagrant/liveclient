package com.meetvr.share.control.meetvr;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;


import com.meetvr.share.info.MpBalanceInfo;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.reponse.Response;
import com.meetvr.share.request.LiveMp_Balance;
import com.meetvr.share.request.Request;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.view.ProgessDlg;

import org.json.JSONObject;

/**
 * Created by wzm-pc on 2016/8/31.
 */
public class MpControl {
    private Context context;
    private MainInfoUpdate onUpData;
    private ProgessDlg progessDlg;
    private void hideProgress(){
        if(progessDlg!=null){
            progessDlg.dismiss();
            progessDlg=null;
        }
    }
        public MpControl(final Context context, final MainInfoUpdate onUpData, UserInfo userInfo, final ProgessDlg progessDlg){
            this.context = context;
            this.onUpData= onUpData;
            this.progessDlg = progessDlg;
            try {
                if(userInfo!=null){
                    LiveMp_Balance liveMp_balance = new LiveMp_Balance(userInfo.getToken(),userInfo.getId());
                    liveMp_balance.addParam(userInfo.getId());
                    liveMp_balance.addQuery(context, new Request.OnNetWorkProc() {
                        @Override
                        public void onStart() {
                            if(progessDlg!=null)
                                progessDlg.showPopupWindow();
                        }

                        @Override
                        public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                            try {
                                if(isOK){
                                    Response response1 = new Response();
                                    JSONObject object= response1.parse(response);
                                    if(Mutils.isResponseOk(response1.getAct_stat())){
                                        MpBalanceInfo mpBalanceInfo = new MpBalanceInfo();
                                        mpBalanceInfo.setMp_balance(Mutils.getJsonString(object,"mb_num"));
                                        mpBalanceInfo.setMp_his_count(Mutils.getJsonString(object,"charge_total"));
                                        mpBalanceInfo.setMb_balance(Mutils.getJsonString(object,"mb_num"));
                                        if(onUpData!=null){
                                            onUpData.OnMpResult(true,mpBalanceInfo);
                                        }
                                    }else{
                                        if(response1.getErr_code()==4010001){
                                            context.sendBroadcast(new Intent("com.login.again"));
                                        }
                                        onUpData.OnMpResult(false,null);
                                    }

                                }else{
                                    if(errMsg!=null&&errMsg.length()>0){
                                        //Toast.makeText(context,errMsg,Toast.LENGTH_SHORT).show();
                                    }else{
                                        //Toast.makeText(context, R.string.net_con_error,Toast.LENGTH_SHORT).show();
                                    }
                                    onUpData.OnMpResult(false,null);

                                }
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
