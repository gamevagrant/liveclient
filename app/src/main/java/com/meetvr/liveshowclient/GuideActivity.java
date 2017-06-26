package com.meetvr.liveshowclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.meetvr.share.MyApplication;
import com.meetvr.share.control.meetvr.Control;
import com.meetvr.share.control.vr_interface.BaseResponse;
import com.meetvr.share.control.vr_interface.RegistInterface;
import com.meetvr.share.reponse.PointResponse;
import com.meetvr.share.request.PointRequest;
import com.meetvr.share.utrils.Constants;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.utrils.PreferencesUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;

import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.util.Date;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    public static IWXAPI api=null;
   // private int weCode;
    private MyApplication application;
    private BroadcastReceiver receiver;
    private void hideActionBar(){
        try {
            getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
           // Window window = getWindow();
           // window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_guide);
       // hideActionBar();


        Mutils.hideAction(this);
        Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
        iniUi();
        application = (MyApplication)getApplication();

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.registerApp(Constants.APP_ID);
        registBroad();

        if(1== PreferencesUtils.getSharePreInt(this,"test_mode",0))
        {
            Constants.TestMode = true;
        }

        isAudioLogin();


    }

    private void isAudioLogin(){

        //
        int iret = PreferencesUtils.getSharePreInt(this,"login_type");
        if(iret==1||iret==2){
            application.loadUserInfo();
            /*
            PointRequest pointRequest = new PointRequest(application.getUserInfo().getUsercode(),this);
            pointRequest.addArray("vrlive_active","ctime",""+new Date().getTime());
            pointRequest.addArray("vrlive_active","value",""+"1");
            new Control(this,null,pointRequest,new PointResponse(),null);
            */
            Intent intent = new Intent(this,MainTabActivity.class);
            startActivity(intent);
            finish();
        }else{
            //PointRequest pointRequest = new PointRequest("0",this);
            //pointRequest.addArray("vrlive_active","ctime",""+new Date().getTime());
            //pointRequest.addArray("vrlive_active","value",""+"1");
            //new Control(this,null,pointRequest,new PointResponse(),null);
        }
    }

    private void iniUi(){
        try {
            TextView declearTxt = (TextView)findViewById(R.id.declaraction_txt);
//            SpannableStringBuilder builder = new SpannableStringBuilder("登录即代表您同意摩象服务和隐私条款");
//            ForegroundColorSpan fSpan = new ForegroundColorSpan(Color.WHITE);
//            ForegroundColorSpan sSpan = new ForegroundColorSpan(getResources().getColor(R.color.declear_txt));
//            builder.setSpan(fSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            builder.setSpan(sSpan, 8, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

           String txtValue = "<font color=\"#ffffff\">登录即代表您同意</font> <u><font color=\"#5eabec\">觅她用户服务和隐私政策条款</font></u>";
            declearTxt.setText(Html.fromHtml(txtValue));
            declearTxt.setOnClickListener(this);

            findViewById(R.id.phone_btn).setOnClickListener(this);
            //Toast.makeText(this,"gaodu:"+getStatusBarHeight(),Toast.LENGTH_SHORT).show();
            findViewById(R.id.wechat_btn).setOnClickListener(this);

            findViewById(R.id.test_btn).setOnClickListener(this);

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.phone_btn:
                startLoginActivty();
                break;
            case R.id.wechat_btn:
                //Intent intent = new Intent(this, WXEntryActivity.class);
                //startActivityForResult(intent,200);

                final   SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "meetvr";
                api.sendReq(req);

//               final  IWXAPI  api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
//                api.registerApp(Constants.APP_ID);
//
//                final   SendAuth.Req req = new SendAuth.Req();
//                req.scope = "snsapi_userinfo";
//                req.state = "meetvr";
//                api.sendReq(req);
                break;
            case R.id.declaraction_txt:
                try {
                   //String str[] = getAssets().list("./");
                    //Toast.makeText(this,str[0],Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,WebViewActivity.class);
                    //intent.putExtra("from",1);
                    intent.putExtra("name","觅她用户服务和隐私政策条款");
                    intent.putExtra("url","http://www.moxiangtv.com/doc/service.html");
                   // intent.putExtra("url","file:///android_asset/warning.html");

                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.test_btn:
                {
                    test++;
                    if(10==test)
                    {
                        Constants.TestMode =!Constants.TestMode;
                        test = 0;
                        PreferencesUtils.putSharePre (this,"test_mode",Constants.TestMode?1:0);
                        Toast.makeText(this,"test mode "+Constants.TestMode,Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
     int test = 0;
    private void startLoginActivty(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==200){
          String weToken =   data.getStringExtra("weToken");
            if(weToken!=null&&weToken.length()>10){//提交登录

            }
        }
        if(resultCode==100){
            finish();
        }
    }

    private void registBroad(){
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver(receiver,new IntentFilter("com.gui.kill"));
    }

    //
//    private void regWeChat(){
//       api = WXAPIFactory.createWXAPI(this, null,true);
//
//        // 将该app注册到微信
//        api.registerApp(Constants.APP_ID);
//    }
//
//    private void loginWchat(){
//
//            // send oauth request
//           final   SendAuth.Req req = new SendAuth.Req();
//            req.scope = "snsapi_userinfo";
//            req.state = "meetvr";
//            api.sendReq(req);
//
//    }
//
//    @Override
//    public void onReq(BaseReq baseReq) {
//
//        Toast.makeText(this,"ok..",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onResp(BaseResp baseResp) {
//        try {
//            if(baseResp.errCode==0){
//                weCode = baseResp.hashCode();
//            }else{
//                Toast.makeText(this,baseResp.errStr,Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
