package com.meetvr.liveshowclient.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meetvr.liveshowclient.GuideActivity;
import com.meetvr.liveshowclient.MainTabActivity;
import com.meetvr.liveshowclient.R;
import com.meetvr.share.MyApplication;
import com.meetvr.share.control.meetvr.Control;
import com.meetvr.share.control.meetvr.LoginControl;
import com.meetvr.share.control.vr_interface.BaseResponse;
import com.meetvr.share.control.vr_interface.RegistInterface;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.info.UserRegistInfo;
import com.meetvr.share.reponse.PointResponse;
import com.meetvr.share.request.PointRequest;
import com.meetvr.share.request.WeChatLoginRequest;
import com.meetvr.share.utrils.Constants;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.utrils.PreferencesUtils;
import com.meetvr.share.view.ProgessDlg;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler ,RegistInterface{
	
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	

	private String weToken;
	private UserInfo userInfo = new UserInfo();

	private  ProgessDlg progessDlg;
	private RelativeLayout relativeLayout;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


		relativeLayout = new RelativeLayout(this);
		relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		relativeLayout.setBackgroundColor(getResources().getColor(R.color.title_bg_color));
		setContentView(relativeLayout);
		Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);

		 GuideActivity.api.handleIntent(getIntent(), this);

    }
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        GuideActivity.api.handleIntent(intent, this);
	}

	// ΢
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:

			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:

			break;
		default:
			break;
		}
	}


	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
	   //int code =	resp.hashCode();
		//Intent intent = new Intent();
		if(resp.errCode==0){

//			int iret = resp.getType();
//			if(resp.getType()==ConstantsAPI.COMMAND_PAY_BY_WX){
//				Toast.makeText(this,"支付成功",Toast.LENGTH_SHORT).show();
//				setResult(100,new Intent());
//				finish();
//			}

			if(resp.getType()==ConstantsAPI.COMMAND_SENDAUTH){
				weToken = ((SendAuth.Resp) resp).code;
				//intent.putExtra("weToken",weToken);
				//开始登录
				final UserRegistInfo userRegistInfo = new UserRegistInfo();
				//userRegistInfo.setPhone(user);
				//userRegistInfo.setPassword(Mutils.getMD5(password+"A1#"));
				userRegistInfo.setApp_ver(Constants.APP_VER);
				userRegistInfo.setDev_info(Mutils.getInfo());
				userRegistInfo.setOs_ver(Mutils.getInfo());
				userRegistInfo.setWeixin_token(weToken);
				TextView textView = new TextView(this);
				progessDlg = new ProgessDlg(this,relativeLayout);
				new LoginControl(this,this,userRegistInfo,userInfo,new WeChatLoginRequest(),null);

			}

		}else{
			//Toast.makeText(this,"失败"+resp.errStr,Toast.LENGTH_SHORT).show();
			finish();
			//Toast.makeText(this,"finish",Toast.LENGTH_SHORT).show();

		}



	}
	/*
	private void pointEnter(String usercoder){
		PointRequest pointRequest = new PointRequest(usercoder,this);
		pointRequest.addArray("vrlive_login","intime",""+new Date().getTime());
		pointRequest.addArray("vrlive_login","outtime",""+new Date().getTime());
		pointRequest.addArray("vrlive_login","state","0");
		pointRequest.addArray("vrlive_login","value","1");

		new Control(this,null,pointRequest,new PointResponse(),null);
	}
	*/

	@Override
	public void onResulte(boolean isOk) {

		if(isOk){
			MyApplication application =(MyApplication) getApplication();
			application.saveUser(userInfo);
			Intent intent = new Intent(this,MainTabActivity.class);
			startActivity(intent);
			//setResult(100,new Intent());
			sendBroadcast(new Intent("com.gui.kill"));
			PreferencesUtils.putSharePre(this,"login_type",2); //1 账号登录  2 微信登录
			//pointEnter("0");

		}
		finish();
		//Toast.makeText(this,"finish",Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onResulte(boolean isOk, BaseResponse baseResponse) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(progessDlg!=null){
				return false;
			}

		}

		return super.onKeyDown(keyCode, event);
	}

}