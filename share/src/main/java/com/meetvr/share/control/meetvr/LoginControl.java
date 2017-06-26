package com.meetvr.share.control.meetvr;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.meetvr.share.control.vr_interface.RegistInterface;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.info.UserRegistInfo;
import com.meetvr.share.reponse.LoginResponse;
import com.meetvr.share.request.LoginRequest;
import com.meetvr.share.request.RegistRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.request.WeChatLoginRequest;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.view.ProgessDlg;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by wzm-pc on 2016/8/31.
 */
public class LoginControl {
    private Context context;
    private RegistInterface registInterface;
    private ProgessDlg progessDlg;
    public static int start_coutnt = 0;
    private void hideProgress(){
        if(progessDlg!=null){
            progessDlg.dismiss();
            progessDlg=null;
        }
    }
        public LoginControl(final Context context, final RegistInterface onregistInterface, UserRegistInfo userInfo, final UserInfo uInfo,Request request, final ProgessDlg progessDlg){
            this.context = context;
            this.registInterface= onregistInterface;
            this.progessDlg = progessDlg;
            try {
                if(request instanceof LoginRequest){
                    LoginRequest loginRequest = (LoginRequest) request;
                    loginRequest.setUserCode(userInfo.getPhone());
                    loginRequest.setPwd(userInfo.getPassword());
                    loginRequest.addos_vern(userInfo.getOs_ver());
                    loginRequest.adddev_infon(userInfo.getDev_info());
                    loginRequest.addVersion(userInfo.getApp_ver());
                    Date date = new Date();
                    loginRequest.addJsonObj("time_t",""+date.getTime()+"_"+start_coutnt);
                    start_coutnt++;
                }
                if(request instanceof WeChatLoginRequest){
                    WeChatLoginRequest weChatLoginRequest = (WeChatLoginRequest) request;
                    weChatLoginRequest.setWeixin_token(userInfo.getWeixin_token());
                    weChatLoginRequest.addos_vern(userInfo.getOs_ver());
                    weChatLoginRequest.adddev_infon(userInfo.getDev_info());
                    weChatLoginRequest.addVersion(userInfo.getApp_ver());
                    Date date1 = new Date();
                    weChatLoginRequest.addJsonObj("time_t",""+date1.getTime()+"_"+start_coutnt);
                    start_coutnt++;
                }


                request.addQuery(context, new Request.OnNetWorkProc() {
                        @Override
                        public void onStart() {
                            //if(progessDlg!=null)
                                //progessDlg.showPopupWindow();
                        }

                        @Override
                        public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                            try {
                                if(isOK){
                                       //Response response1 = new Response();
                                       // JSONObject object= response1.parse(response);
                                        //Toast.makeText(ReadyActivity.this,"密码修改成功！",Toast.LENGTH_SHORT).show();
                                       //String live_history_id = Mutils.getJsonString(object,"live_history_id");
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
