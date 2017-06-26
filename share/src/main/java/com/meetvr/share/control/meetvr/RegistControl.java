package com.meetvr.share.control.meetvr;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.meetvr.share.control.vr_interface.RegistInterface;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.info.UserRegistInfo;
import com.meetvr.share.reponse.LoginResponse;
import com.meetvr.share.reponse.Response;
import com.meetvr.share.request.RegistRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.view.ProgessDlg;

import org.json.JSONObject;

/**
 * Created by wzm-pc on 2016/8/31.
 */
public class RegistControl {
    private Context context;
    private RegistInterface registInterface;
    private ProgessDlg progessDlg;
    private void hideProgress(){
        if(progessDlg!=null){
            progessDlg.dismiss();
            progessDlg=null;
        }
    }
        public RegistControl(final Context context, final RegistInterface onregistInterface, UserRegistInfo userInfo,final UserInfo uInfo, final ProgessDlg progessDlg){
            this.context = context;
            this.registInterface= onregistInterface;
            this.progessDlg = progessDlg;
            try {
                RegistRequest registRequest =  new RegistRequest();
                registRequest.setRequestParam(userInfo.getPhone(),userInfo.getPassword(),userInfo.getDevice_id(),userInfo.getVcode());
                registRequest.setRequestLogin(userInfo.getApp_ver(),userInfo.getDev_info(),userInfo.getOs_ver());
                registRequest.addQuery(context, new Request.OnNetWorkProc() {
                        @Override
                        public void onStart() {
                            if(progessDlg!=null)
                                progessDlg.showPopupWindow();
                        }

                        @Override
                        public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                            try {
                                if(isOK){

                                        LoginResponse loginResponse = new LoginResponse();
                                        loginResponse.parse(response,uInfo);
                                    if(Mutils.isResponseOk(loginResponse.getAct_stat())){
                                        if(registInterface!=null){
                                            registInterface.onResulte(true);
                                        }

                                    }else{
                                        Toast.makeText(context,loginResponse.getErr_msg(), Toast.LENGTH_SHORT).show();
                                        if(registInterface!=null){
                                            registInterface.onResulte(false);
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
                                        registInterface.onResulte(false);
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
