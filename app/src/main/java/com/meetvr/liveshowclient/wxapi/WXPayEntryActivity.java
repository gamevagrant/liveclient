package com.meetvr.liveshowclient.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.meetvr.share.utrils.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {


		if(resp.errCode==0){
			if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
				Toast.makeText(this,"支付成功",Toast.LENGTH_SHORT).show();
				//setResult(100,new Intent());
				sendBroadcast(new Intent("com.pay.kill"));
				finish();
			}
		}else if(resp.errCode==-2){
			Toast.makeText(this,"取消支付",Toast.LENGTH_SHORT).show();
			finish();
		}else if(resp.errCode==-1){
			Toast.makeText(this,"支付失败",Toast.LENGTH_SHORT).show();
			finish();
		}

	}
}