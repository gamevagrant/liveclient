package com.meetvr.share.control.meetvr;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;


import com.meetvr.share.info.UserInfo;
import com.meetvr.share.reponse.Response;
import com.meetvr.share.request.LiveStartRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.view.ProgessDlg;
import org.json.JSONObject;

/**
 * Created by wzm-pc on 2016/8/31.
 */
public class PubControl {
    private Context context;
    private MainInfoUpdate onPubControl;
    private ProgessDlg progessDlg;
    private void hideProgress(){
        if(progessDlg!=null){
            progessDlg.dismiss();
            progessDlg=null;
        }
    }
        public PubControl(final Context context, final MainInfoUpdate onPubControl, UserInfo userInfo, final ProgessDlg progessDlg){
            this.context = context;
            this.onPubControl= onPubControl;
            this.progessDlg = progessDlg;
            try {
                if(userInfo!=null){
                    LiveStartRequest liveStartRequest = new LiveStartRequest(userInfo.getToken(),userInfo.getId());
                    liveStartRequest.addQuery(context, new Request.OnNetWorkProc() {
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
                                        //Toast.makeText(ReadyActivity.this,"密码修改成功！",Toast.LENGTH_SHORT).show();
                                       String live_history_id = Mutils.getJsonString(object,"live_history_id");
                                        if(onPubControl!=null){
                                            onPubControl.OnPubResult(true,live_history_id);
                                        }

                                    }else{
                                        Toast.makeText(context,response1.getErr_msg(), Toast.LENGTH_SHORT).show();
                                        if(onPubControl!=null){
                                            onPubControl.OnPubResult(false,null);
                                        }
                                    }

                                }else{
                                    if(errMsg!=null&&errMsg.length()>0){
                                        Toast.makeText(context,"请检查网络", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                                    }
                                    //startPubActivity(); //测试进入
                                    if(onPubControl!=null){
                                        onPubControl.OnPubResult(false,null);
                                    }

                                }
                                hideProgress();



                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    Toast.makeText(context,"登录的时候没有获取到用户信息，可能是登录服务么启动！", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

}
