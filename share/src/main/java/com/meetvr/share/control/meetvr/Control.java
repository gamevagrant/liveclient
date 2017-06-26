package com.meetvr.share.control.meetvr;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.Toast;

import com.meetvr.share.control.vr_interface.BaseResponse;
import com.meetvr.share.control.vr_interface.RegistInterface;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.info.UserRegistInfo;
import com.meetvr.share.reponse.LoginResponse;
import com.meetvr.share.request.LoginRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.request.WeChatLoginRequest;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.view.ProgessDlg;

import org.json.JSONObject;

/**
 * Created by wzm-pc on 2016/8/31.
 */
public class Control {
    private Context context;
    private RegistInterface registInterface;
    private ProgessDlg progessDlg;
    private void hideProgress(){
        if(progessDlg!=null){
            progessDlg.dismiss();
            progessDlg=null;
        }
    }
        public Control(final Context context, final RegistInterface onregistInterface, Request request, final BaseResponse jReponse, final ProgessDlg progessDlg){
            this.context = context;
            this.registInterface= onregistInterface;
            this.progessDlg = progessDlg;
            try {
                request.addQuery(context, new Request.OnNetWorkProc() {
                        @Override
                        public void onStart() {
                            if(progessDlg!=null)
                                progessDlg.showPopupWindow();
                        }

                        @Override
                        public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                            try {
                                if(isOK){
                                       //Response response1 = new Response();
                                       // JSONObject object= response1.parse(response);
                                        //Toast.makeText(ReadyActivity.this,"密码修改成功！",Toast.LENGTH_SHORT).show();
                                       //String live_history_id = Mutils.getJsonString(object,"live_history_id");
                             //           LoginResponse loginResponse = new LoginResponse();
                             //           loginResponse.parse(response,uInfo);
                                    jReponse.paseJson(response);
                                    if(Mutils.isResponseOk(jReponse.getAct_stat())){
                                        if(registInterface!=null){
                                            registInterface.onResulte(true,jReponse);
                                        }

                                    }else{
                                        Toast.makeText(context,jReponse.getErr_msg(), Toast.LENGTH_SHORT).show();
                                        if(jReponse.getErrCcode()==4010001){
                                            context.sendBroadcast(new Intent("com.login.again"));
                                        }

                                        if(registInterface!=null){
                                            registInterface.onResulte(false,jReponse);
                                        }
                                    }

                                }else{
                                    if(errMsg!=null&&errMsg.length()>0){
                                        Toast.makeText(context,"请检查网络", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                                    }
                                    //startPubActivity(); //测试进入
                                    if(registInterface!=null){
                                        registInterface.onResulte(false,jReponse);
                                    }

                                }
                                hideProgress();



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
