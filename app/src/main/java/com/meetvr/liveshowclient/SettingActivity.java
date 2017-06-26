package com.meetvr.liveshowclient;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.meetvr.share.MyApplication;
import com.meetvr.share.control.update.AppUpDate;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.utrils.Constants;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.utrils.PreferencesUtils;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if(getSupportActionBar()!=null){
            Mutils.hideAction(this);
        }
        if(getActionBar()!=null){
            getActionBar().hide();
        }
        getUserInfo();
        Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
        iniUi();
    }
    private void getUserInfo(){
        MyApplication application = (MyApplication)getApplication();
        userInfo = application.getUserInfo();
    }

    private void iniUi(){
        Mutils.setTitle(findViewById(R.id.title_txt),"设置");
        findViewById(R.id.back).setOnClickListener(this);
        Mutils.setTitle(findViewById(R.id.version_txt), Constants.APP_VER);
        findViewById(R.id.check_updata_layout).setOnClickListener(this);
        findViewById(R.id.clean_cache_layout).setOnClickListener(this);
        findViewById(R.id.user_xieyi_layout).setOnClickListener(this);
        findViewById(R.id.about_me_layout).setOnClickListener(this);
        findViewById(R.id.exit_btn).setOnClickListener(this);
        findViewById(R.id.modify_pwd_layout).setOnClickListener(this);
    }

    private boolean mayRequestStorage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(this.findViewById(R.id.check_updata_layout), R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 300);
                        }
                    });
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 400);
        }
        return false;
    }



    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode==400){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new AppUpDate(this,true).checkUpDate();
            }else{
                Toast.makeText(this,"禁用写入扩展卡将无法升级",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void update(){
        if(mayRequestStorage()){
            new AppUpDate(this,true).checkUpDate();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.check_updata_layout:
                update();
                //Toast.makeText(SettingActivity.this,"getnxin jianche",Toast.LENGTH_SHORT).show();
                break;
            case R.id.clean_cache_layout:
                try {
                    MyApplication application = (MyApplication) getApplication();
                    application.getBmpCache().clean();
                    Toast.makeText(this,"清除完成",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.user_xieyi_layout:
                Intent intent2 = new Intent(this,WebViewActivity.class);
                //intent2.putExtra("from",1);
                intent2.putExtra("name","觅她用户服务和隐私政策条款");
                intent2.putExtra("url","http://www.moxiangtv.com/doc/service.html");
                startActivity(intent2);
                break;
            case R.id.about_me_layout:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.exit_btn:
                PreferencesUtils.putSharePre(this,"login_type",0); //1 账号登录  2 微信登录
                MyApplication application = (MyApplication)getApplication();
                application.deleteUser();
                Intent intent1 = new Intent(this,GuideActivity.class);
                startActivity(intent1);
                sendBroadcast(new Intent("com.kill.me"));
                finish();
                break;
            case  R.id.modify_pwd_layout:
                MyApplication app = (MyApplication)getApplication();

                if(app.getUserInfo().getAccountType()!=0)
                {//0:表示手机注册用户 1:小米账户, 2:微信账户, 3:qq账户, 4:微博账号, 5:小米VR, 6:大鹏VR, 7:三星gear
                    Toast.makeText(SettingActivity.this,"微信注册用户无需修改密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                changeNickName(0);

        }
    }

    private void changeNickName(int type){
        Intent intent = new Intent(this,ModifyPwdActivity.class);
        intent.putExtra(ModifyPwdActivity.FROM_TYPE,0);
        intent.putExtra(ModifyPwdActivity.MODIFY_TYPE,type);//0 pwd 1 nick
        intent.putExtra(ModifyPwdActivity.IDENTIFIER,userInfo.getId());
        intent.putExtra(ModifyPwdActivity.TOKEN,userInfo.getToken());
        startActivity(intent);
    }


    //getActivity().getResources().getConfiguration().orientation 
}
