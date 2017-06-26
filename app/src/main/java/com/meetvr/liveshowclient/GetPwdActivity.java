package com.meetvr.liveshowclient;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.meetvr.share.reponse.Response;
import com.meetvr.share.request.CheckCoderRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.request.VcodeRequest;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.view.ProgessDlg;

import org.json.JSONObject;

public class GetPwdActivity extends AppCompatActivity implements View.OnClickListener{



    private enum STATE{PHONE,VERCODE};
    private STATE state= STATE.PHONE;

    private EditText phoneEdit,codeEdit;

    private ProgessDlg progessDlg;
    private Button getCodeBtn;

    private String phone,vcode;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    getCodeBtn.setText(String.valueOf(msg.arg1)+"秒");
                    break;
                case 0://
                    getCodeBtn.setText("获取");
                    getCodeBtn.setEnabled(true);
                    getCodeBtn.setBackgroundResource(R.drawable.bg_btn1_normal);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pwd);
        Mutils.hideAction(this);
        Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
        iniUi();

    }
    private void iniUi(){
        try {
            findViewById(R.id.back).setOnClickListener(this);

            Mutils.setTitle(findViewById(R.id.title_txt),"找回密码");



            findViewById(R.id.next_btn).setOnClickListener(this);
            phoneEdit = (EditText)findViewById(R.id.phone_edit);
            codeEdit = (EditText)findViewById(R.id.ver_code);
           // findViewById(R.id.get_vocder).setOnClickListener(this);
            getCodeBtn = (Button)findViewById(R.id.get_vocder);
            getCodeBtn.setOnClickListener(this);
            Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);

            findViewById(R.id.del_btn).setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showProgess(){
        try {
            progessDlg = new ProgessDlg(this,phoneEdit);
            progessDlg.showPopupWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void hideProgress(){
        try {
            if(progessDlg!=null){
                progessDlg.dismiss();
                progessDlg = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgress();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode== KeyEvent.KEYCODE_BACK){
            if(progessDlg!=null){
                return false;
            }
            if(state== STATE.VERCODE){
                showPhone(true);
                state = STATE.PHONE;
                return false;
            }

        }

        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                changeBtn();
                break;
            case R.id.next_btn:
                processNextBtn();
                break;
            case R.id.get_vocder:
                getVcoder();
                break;
            case R.id.del_btn:
                phoneEdit.setText("");
                break;
        }
    }

    private void changeBtn(){
        if(state== STATE.VERCODE){
            showPhone(true);
            state = STATE.PHONE;
        }else{
            finish();
        }
    }

    private void processNextBtn(){
        if(state== STATE.PHONE){
            getPhoneVerCode();
        }else{
            if(state== STATE.VERCODE){ //获取成功后退出
                getPwd();
            }
        }

    }
    private void getPhoneVerCode(){
        String phoneNum = phoneEdit.getText().toString();
        if(TextUtils.isEmpty(phoneNum)){
           // phoneEdit.setError("请输入手机号！");
            Toast.makeText(GetPwdActivity.this,"请输入手机号！",Toast.LENGTH_SHORT).show();
            return;
        }
        if(phoneNum.length()<11){
            //phoneEdit.setError("输入位数不够！");
            Toast.makeText(GetPwdActivity.this,"输入位数不够！",Toast.LENGTH_SHORT).show();
            return;
        }
        state = STATE.VERCODE;
        showPhone(false);

    }
    private void sleep(long miseconds){
        try {
            Thread.sleep(miseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void getVcoder(){
        try {
            String phoneNum = phoneEdit.getText().toString();
            VcodeRequest vcodeRequest = new VcodeRequest();
            vcodeRequest.setPhoneDevid(phoneNum,Mutils.getDeviceId());
            vcodeRequest.addQuery(this, new Request.OnNetWorkProc() {
                @Override
                public void onStart() {
                    showProgess();
                }

                @Override
                public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                    try {
                        if(isOK){
                            Response response1 = new Response();
                            response1.parse(response);
                            if(Mutils.isResponseOk(response1.getAct_stat())){
                                Toast.makeText(GetPwdActivity.this,"验证码已经发送到您手机！", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(GetPwdActivity.this,response1.getErr_msg(), Toast.LENGTH_SHORT).show();
                            }

                            getCodeBtn.setBackgroundResource(R.drawable.bg_btn1_normal_pressed);
                            getCodeBtn.setEnabled(false);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int tlen = 60;
                                    while (tlen>0){
                                       Message msg = new Message();
                                        msg.what = 1;
                                        msg.arg1=tlen;
                                        handler.sendMessage(msg);
                                        sleep(1000);
                                        tlen--;
                                    }
                                    handler.sendEmptyMessage(0);
                                }
                            }).start();
                        }else{
                            Toast.makeText(GetPwdActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
//                            if(errMsg!=null&&errMsg.length()>0){
//                                Toast.makeText(GetPwdActivity.this,errMsg, Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(GetPwdActivity.this,R.string.net_con_error, Toast.LENGTH_SHORT).show();
//                            }

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

    private void getPwd(){
        CheckCoderRequest checkCoderRequest = new CheckCoderRequest();
        phone = phoneEdit.getText().toString();
        vcode = codeEdit.getText().toString();

        if(TextUtils.isEmpty(vcode)){
            Toast.makeText(this,"验证码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        checkCoderRequest.setCheckParam(phoneEdit.getText().toString(),Mutils.getDeviceId(),vcode);
        checkCoderRequest.addQuery(this, new Request.OnNetWorkProc() {
            @Override
            public void onStart() {
                showProgess();
            }

            @Override
            public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                try {
                    if(isOK){
                        Response response1 = new Response();
                        JSONObject object =  response1.parse(response);
                        if(Mutils.isResponseOk(response1.getAct_stat())){
                            String token = Mutils.getJsonString(object,"token");
                            String identifier = Mutils.getJsonString(object,"identifier");
                            Intent intent = new Intent(GetPwdActivity.this,ModifyPwdActivity.class);
                            intent.putExtra(ModifyPwdActivity.MODIFY_TYPE,2);
                            intent.putExtra(ModifyPwdActivity.TOKEN,token);
                            intent.putExtra(ModifyPwdActivity.IDENTIFIER,identifier);
                            intent.putExtra(ModifyPwdActivity.PHONE,phone);
                            intent.putExtra(ModifyPwdActivity.VCODE,vcode);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(GetPwdActivity.this,response1.getErr_msg(), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(GetPwdActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
//                        if(errMsg!=null&&errMsg.length()>0){
//                            Toast.makeText(GetPwdActivity.this,errMsg, Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(GetPwdActivity.this,R.string.net_con_error, Toast.LENGTH_SHORT).show();
//                        }

                    }
                    hideProgress();

                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showPhone(boolean isShow){
        if(isShow){
            findViewById(R.id.user_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.ver_code_layout).setVisibility(View.GONE);
        }else {
            findViewById(R.id.user_layout).setVisibility(View.GONE);
            findViewById(R.id.ver_code_layout).setVisibility(View.VISIBLE);
        }
    }

    private void stateChange(){
        if(state== STATE.PHONE){
            finish();
        }else{
            showPhone(true);
        }
    }
}
