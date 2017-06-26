package com.meetvr.liveshowclient;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.meetvr.share.MyApplication;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.reponse.Response;
import com.meetvr.share.request.ModifyNicknameRequest;
import com.meetvr.share.request.ModifyPwdRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.view.ProgessDlg;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyPwdActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = this.getClass().getName();
    public static final String MODIFY_TYPE="modify_type";
    private EditText pwd0,pwd1,pwd2,nickEdit;
    public static final String TOKEN="token";
    public static final String IDENTIFIER="identifier";
    public static final String FROM_TYPE="from_type";
    public static final String PHONE="phone";
    public static final String VCODE="vcode";
    private String tokenValue,identifierValue;

    private ProgessDlg progessDlg;
    private int type=0;
    private int fromLogin =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        hide();
        tranportActionBar();
        getModifyTypeAndUserData();
        iniUi();

    }

    private void getModifyTypeAndUserData(){
        type = getIntent().getIntExtra(MODIFY_TYPE,0); //0修改密码，1修改昵称，2忘记密码
        tokenValue = getIntent().getStringExtra(TOKEN);
        identifierValue = getIntent().getStringExtra(IDENTIFIER);
        fromLogin = getIntent().getIntExtra(FROM_TYPE,0);
    }
    private void iniUi(){
        try {

            findViewById(R.id.back).setOnClickListener(this);

            findViewById(R.id.title_save).setOnClickListener(this);

            findViewById(R.id.del_btn).setOnClickListener(this);

            if(type==0){
                findViewById(R.id.modify_nickname_root).setVisibility(View.GONE);
                findViewById(R.id.modify_pwd_root).setVisibility(View.VISIBLE);
                Mutils.setTitle(findViewById(R.id.title_txt),"修改密码");
                pwd0 = (EditText)  findViewById(R.id.pwd_edit_old);
                pwd1 = (EditText)  findViewById(R.id.pwd_edit_new_f);
                pwd2 = (EditText) findViewById(R.id.pwd_edit_new_s);
                findViewById(R.id.eye_btn_0).setOnClickListener(this);
                findViewById(R.id.eye_btn).setOnClickListener(this);
                findViewById(R.id.eye_btn2).setOnClickListener(this);

            }

            if(type==1){
                Mutils.setTitle(findViewById(R.id.title_txt),"修改昵称");
                findViewById(R.id.modify_pwd_root).setVisibility(View.GONE);
                findViewById(R.id.modify_nickname_root).setVisibility(View.VISIBLE);
                nickEdit = (EditText)findViewById(R.id.nickname_edit);
                MyApplication application = (MyApplication)getApplication();
                nickEdit.setText(application.getUserInfo().getUsername());
            }
            if(type==2){
                findViewById(R.id.modify_nickname_root).setVisibility(View.GONE);
                findViewById(R.id.modify_pwd_root).setVisibility(View.VISIBLE);
                findViewById(R.id.modify_pwd_old).setVisibility(View.GONE);
                Mutils.setTitle(findViewById(R.id.title_txt),"设置密码");
                //pwd0 = (EditText)  findViewById(R.id.pwd_edit_old);
                pwd1 = (EditText)  findViewById(R.id.pwd_edit_new_f);
                pwd2 = (EditText) findViewById(R.id.pwd_edit_new_s);
                //findViewById(R.id.eye_btn_0).setOnClickListener(this);
                findViewById(R.id.eye_btn).setOnClickListener(this);
                findViewById(R.id.eye_btn2).setOnClickListener(this);

            }
            findViewById(R.id.title_save).setVisibility(View.VISIBLE);
            setMaxWidth();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setMaxWidth(){
        try {
            if(type==0||2==type){
                int maxWidth = pwd0.getMeasuredWidth();
                pwd0.setMaxWidth(maxWidth);
                maxWidth = pwd1.getMeasuredWidth();
                pwd1.setMaxWidth(maxWidth);
                maxWidth = pwd2.getMeasuredWidth();
                pwd2.setMaxWidth(maxWidth);
            }
            if(type==1){
                int maxWidth = nickEdit.getMeasuredWidth();
                nickEdit.setMaxWidth(maxWidth);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void tranportActionBar(){
        try {
//            if(Build.VERSION.SDK_INT>19){
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            }else {
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            }
            Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hide() {
        // Hide UI first
        try {
            //
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }

        } catch (Exception e) {
            e.printStackTrace();
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        }

    }


    private void showProgess(){
        try {
            progessDlg = new ProgessDlg(this,findViewById(R.id.title_save));
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

        }

        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.eye_btn_0:
                changeInputType((ImageButton)v,pwd0);
                break;
            case R.id.eye_btn:
                changeInputType((ImageButton)v,pwd1);
                break;
            case  R.id.eye_btn2:
                changeInputType((ImageButton)v,pwd2);
                break;
            case R.id.title_save:
                if(type==0 ||type==2){
                    savePwd();
                }
                if(type==1){
                    saveNickName();
                }
                break;
            case R.id.del_btn:
                nickEdit.setText("");
                break;

        }
    }

    /*
      1:为空
      2：都是数字
     */
    private int isInvalida(String value){
        if(TextUtils.isEmpty(value)){
            return 1;
        }
        /*
        if(TextUtils.isDigitsOnly(value)){
            return 2;
        }
        */
        return 0;
    }

    private void updateNickname(String nickName){

        UserInfo userInfo = ((MyApplication)getApplication()).getUserInfo();
        JSONObject jsonObject = Mutils.newJson(userInfo.jsonValue);
        try {
            jsonObject.put("nickname",nickName);
            userInfo.jsonValue = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveNickName(){
        String nickName = nickEdit.getText().toString();
        if(TextUtils.isEmpty(nickName)){
            Toast.makeText(this,"昵称不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        ModifyNicknameRequest modifyNicknameRequest = new ModifyNicknameRequest(tokenValue,identifierValue);
        // modifyPwdRequest.setUrlParam();
        modifyNicknameRequest.setParam(identifierValue,nickName,null);
        modifyNicknameRequest.addQuery(this, new Request.OnNetWorkProc() {
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
                            Toast.makeText(ModifyPwdActivity.this,"昵称修改成功", Toast.LENGTH_SHORT).show();
                            ((MyApplication)getApplication()).getUserInfo().setUsername(nickEdit.getText().toString());
                            updateNickname(nickEdit.getText().toString());
                            ((MyApplication)getApplication()).saveUser(((MyApplication)getApplication()).getUserInfo());
                            finish();
                        }else{
                            Toast.makeText(ModifyPwdActivity.this,response1.getErr_msg(), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(ModifyPwdActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
//                        if(errMsg!=null&&errMsg.length()>0){
//                            Toast.makeText(ModifyPwdActivity.this,errMsg, Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(ModifyPwdActivity.this,R.string.net_con_error, Toast.LENGTH_SHORT).show();
//                        }

                    }
                    hideProgress();

                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void savePwd(){

        String p1 = pwd1.getText().toString();
        String p2 = pwd2.getText().toString();
        int iret = isInvalida(p1);
        if(TextUtils.isEmpty(p1)){
            Toast.makeText(this,"密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        if(TextUtils.isDigitsOnly(p1)){
            Toast.makeText(this,"密码不能存数字！", Toast.LENGTH_SHORT).show();
            return;
        }
        */
        if(p1.length()<8){
            Toast.makeText(this,"密码长度不能低于8位！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!p1.equals(p2)){
            Toast.makeText(this,"两次输入不一致！", Toast.LENGTH_SHORT).show();
            return ;
        }

        ModifyPwdRequest modifyPwdRequest = null;

        if(0==type)
        {//修改密码
            String p0 = pwd0.getText().toString();
            if(p0.length()<8){
                Toast.makeText(this,"原密码格式不正确！", Toast.LENGTH_SHORT).show();
                return;
            }

            modifyPwdRequest = new ModifyPwdRequest(tokenValue,identifierValue,identifierValue,Mutils.getMD5(p0+"A1#"),Mutils.getMD5(p1+"A1#"));
        }
        else if(2==type)
        {//重置密码
            modifyPwdRequest = new ModifyPwdRequest(getIntent().getStringExtra(ModifyPwdActivity.PHONE),getIntent().getStringExtra(ModifyPwdActivity.VCODE),Mutils.getMD5(p1+"A1#"));
        }

        if(null==modifyPwdRequest)
        {
            return;
        }
       // modifyPwdRequest.setUrlParam();
        //modifyPwdRequest.setParam(identifierValue,Mutils.getMD5(p0+"A1#"),Mutils.getMD5(p1+"A1#"));
        modifyPwdRequest.addQuery(this, new Request.OnNetWorkProc() {
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
                            if(fromLogin==1){ //修改密码 第一次登录

                                Intent intent = new Intent(ModifyPwdActivity.this,MainTabActivity.class);
                                startActivity(intent);
                            }
                            Toast.makeText(ModifyPwdActivity.this,"密码修改成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(ModifyPwdActivity.this,response1.getErr_msg(), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(ModifyPwdActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
//                        if(errMsg!=null&&errMsg.length()>0){
//                            Toast.makeText(ModifyPwdActivity.this,errMsg, Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(ModifyPwdActivity.this,R.string.net_con_error, Toast.LENGTH_SHORT).show();
//                        }

                    }
                    hideProgress();

                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private static boolean isPasswordInputType(int inputType) {
        final int variation =
                inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }
//    private void changeInputType(EditText editText){
//        if(isPasswordInputType(editText.getInputType())){
//            editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//            editText.setBackgroundResource(R.drawable.ic_input_visible);
//            Log.e("lao","ddd");
//        }else{
//            editText.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
//            editText.setBackgroundResource(R.drawable.ic_input_invisible);
//            Log.e("lao","yinchang");
//        }
//    }

    private void changeInputType(ImageButton btn, EditText edit){
        if(isPasswordInputType(edit.getInputType())){
            edit.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btn.setBackgroundResource(R.drawable.ic_input_visible);
            Log.e("lao","ddd");
        }else{
            edit.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            btn.setBackgroundResource(R.drawable.ic_input_invisible);
            Log.e("lao","yinchang");
        }
    }
}
