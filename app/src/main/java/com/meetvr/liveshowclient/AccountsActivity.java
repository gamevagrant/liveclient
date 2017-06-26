package com.meetvr.liveshowclient;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.meetvr.share.MyApplication;
import com.meetvr.share.control.meetvr.Control;
import com.meetvr.share.control.vr_interface.BaseResponse;
import com.meetvr.share.control.vr_interface.RegistInterface;
import com.meetvr.share.info.RcardInfo;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.reponse.OrderResponse;
import com.meetvr.share.reponse.RcardResponse;
import com.meetvr.share.reponse.Response;
import com.meetvr.share.request.OrderRequest;
import com.meetvr.share.request.RcardRequest;
import com.meetvr.share.utrils.Constants;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.view.ProgessDlg;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

public class AccountsActivity extends AppCompatActivity implements View.OnClickListener,RegistInterface {

    private String account;
    private UserInfo userInfo;
    private RcardResponse rcardResponse = new RcardResponse();
    private ProgessDlg progessDlg;
    private LinearLayout linearLayout;
    private int selPos =0;
    private View selView;
    private ImageView alipayImg;
    private ImageView weChatImg;

    private String orderInfo; //支付宝

    private static final int SDK_PAY_FLAG = 1;

    private OrderResponse orderResponse = new OrderResponse();
    private BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        if(getSupportActionBar()!=null){
            Mutils.hideAction(this);
        }
        if(getActionBar()!=null){
            getActionBar().hide();
        }
        Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
        getIntentData();
        iniUi();
        registBroad();
    }
    private void registBroad(){
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver(receiver,new IntentFilter("com.pay.kill"));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    private void iniUi(){
        try {
            Mutils.setTitle(findViewById(R.id.title_txt),"充值");
            findViewById(R.id.back).setOnClickListener(this);
            findViewById(R.id.allipay_layout).setOnClickListener(this);
            findViewById(R.id.weixinpay_layout).setOnClickListener(this);
            findViewById(R.id.commit_btn).setOnClickListener(this);
            Mutils.setTitle(findViewById(R.id.mb_count_txt),account);
            linearLayout = (LinearLayout)findViewById(R.id.money_view) ;
            alipayImg = (ImageView)findViewById(R.id.allipay_img);
            weChatImg = (ImageView)findViewById(R.id.wechat_img);
            weChatImg.setVisibility(View.GONE);
            RcardRequest rcardRequest = new RcardRequest(userInfo.getToken(),userInfo.getId());
            // progessDlg = new ProgessDlg(this,linearLayout);
            new Control(this,this,rcardRequest,rcardResponse,null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void getIntentData(){
       account =   getIntent().getStringExtra("mb");
        MyApplication application = (MyApplication)getApplication();
        userInfo = application.getUserInfo();
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

    @Override
    public void onResulte(boolean isOk) {

    }

    @Override
    public void onResulte(boolean isOk, BaseResponse baseResponse) {
        if(isOk){
            if (baseResponse instanceof RcardResponse){
                iniAcountList();
            }
            if(baseResponse instanceof OrderResponse){
                if(orderResponse.getPay_way().equals("alipay")){
                    payAli();
                }
                if(orderResponse.getPay_way().equals("wxpay")){
                    wxPay();
                }

            }
        }
    }

    private void setAccountTxt(RcardInfo rcardInfo , TextView name, TextView price, TextView ratio){
        Mutils.setTitle(name,rcardInfo.getMb_num()+"摩币");
        Mutils.setTitle(price,""+rcardInfo.getPrice()+"元");
        Mutils.setTitle(ratio,"+"+rcardInfo.getDiscount_ratio()+"%");
    }

    private void iniAcountList(){
        int len = rcardResponse.getDatas().size()/3 +1;
        for(int i=0;i<len;i++){
            View view = getLayoutInflater().inflate(R.layout.acount_item,null);
            if(i*3<rcardResponse.getDatas().size()){
                RcardInfo rcardInfo0 = rcardResponse.getDatas().get(i*3);
                FrameLayout frameLayout = (FrameLayout)view.findViewById(R.id.mb_f0);
                frameLayout.setVisibility(View.VISIBLE);
                TextView name =(TextView) view.findViewById(R.id.mb_name0);
                TextView price = (TextView)view.findViewById(R.id.mb_num0);
                TextView ratio = (TextView)view.findViewById(R.id.mb_yh0);
                setAccountTxt(rcardInfo0,name,price,ratio);

                if(i==0){
                    if(rcardInfo0.getDiscount_ratio()>0){
                        frameLayout.setBackgroundResource(R.drawable.bg_sum_none);
                    }else{
                        frameLayout.setBackgroundResource(R.drawable.bg_sum_selected);
                        ratio.setVisibility(View.GONE);
                    }
                    selView = frameLayout;
                }else{
                    frameLayout.setBackgroundResource(R.drawable.bg_sum_unselected);
                    ratio.setVisibility(View.GONE);
                }

                frameLayout.setOnClickListener(new SelClick(i,0));


            }
            if(i*3+1<rcardResponse.getDatas().size()){
                RcardInfo rcardInfo1 = rcardResponse.getDatas().get(i*3+1);
                FrameLayout frameLayout = (FrameLayout)view.findViewById(R.id.mb_f1);
                frameLayout.setVisibility(View.VISIBLE);

                TextView name =(TextView) view.findViewById(R.id.mb_name1);
                TextView price = (TextView)view.findViewById(R.id.mb_num1);
                TextView ratio = (TextView)view.findViewById(R.id.mb_yh1);
                setAccountTxt(rcardInfo1,name,price,ratio);
                frameLayout.setBackgroundResource(R.drawable.bg_sum_unselected);
                ratio.setVisibility(View.GONE);
                frameLayout.setOnClickListener(new SelClick(i,1));

            }
            if(i*3+2<rcardResponse.getDatas().size()){
                RcardInfo rcardInfo2 = rcardResponse.getDatas().get(i*3+2);
                FrameLayout frameLayout = (FrameLayout)view.findViewById(R.id.mb_f2);
                frameLayout.setVisibility(View.VISIBLE);

                TextView name =(TextView) view.findViewById(R.id.mb_name2);
                TextView price = (TextView)view.findViewById(R.id.mb_num2);
                TextView ratio = (TextView)view.findViewById(R.id.mb_yh2);
                setAccountTxt(rcardInfo2,name,price,ratio);
                frameLayout.setBackgroundResource(R.drawable.bg_sum_unselected);
                ratio.setVisibility(View.GONE);
                frameLayout.setOnClickListener(new SelClick(i,2));

            }
            linearLayout.addView(view);


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
          case  R.id.back:
            finish();
            break;
            case R.id.allipay_layout:
                changeImgStatus(alipayImg,weChatImg);
                break;
            case R.id.weixinpay_layout:
                changeImgStatus(weChatImg,alipayImg);
                break;
            case R.id.commit_btn:
                getOrdInfo();
                break;
        }
    }

    private void getOrdInfo(){

        try {
            OrderRequest orderRequest = new OrderRequest(userInfo.getToken(),userInfo.getId());
            RcardInfo rcardInfo = rcardResponse.getDatas().get(selPos);
            String payType = "alipay";
            if(weChatImg.getVisibility()==View.VISIBLE){
                payType ="wxpay";
            }
            orderRequest.addParam(String.valueOf(rcardInfo.getId()),payType);

            new Control(this,this,orderRequest,orderResponse,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeImgStatus(ImageView showimg,ImageView hideimg){
        showimg.setVisibility(View.VISIBLE);
        hideimg.setVisibility(View.GONE);
    }


    class SelClick implements View.OnClickListener{
        private int mPos,nPos;
        public SelClick(int mPos,int nPos){
            this.mPos = mPos;
            this.nPos = nPos;
        }
        @Override
        public void onClick(View view) {
            try {
                selPos = mPos*3 + nPos;
                selView.setBackgroundResource(R.drawable.bg_sum_unselected);
                setRatioState(selView,View.GONE);

                RcardInfo rcardInfo = rcardResponse.getDatas().get(selPos);
                if(rcardInfo.getDiscount_ratio()>0){
                    view.setBackgroundResource(R.drawable.bg_sum_none);
                    setRatioState(view,View.VISIBLE);
                }else{
                    view.setBackgroundResource(R.drawable.bg_sum_selected);
                    setRatioState(view,View.GONE);
                }
                selView = view;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        private void setRatioState(View view,int showState){
            View ratio = null;

            switch (view.getId()){
                case R.id.mb_f0:
                    ratio = view.findViewById(R.id.mb_yh0);
                    break;
                case  R.id.mb_f1:
                    ratio = view.findViewById(R.id.mb_yh1);
                    break;
                case  R.id.mb_f2:
                    ratio = view.findViewById(R.id.mb_yh2);
                    break;
            }
            ratio.setVisibility(showState);
        }
    }

    private void wxPay(){
        try{

/*
                JSONObject json = new JSONObject("{\"appid\":\"wxb4ba3c02aa476ea1\",\"partnerid\":\"1305176001\",\"package\":\"Sign=WXPay\",\"noncestr\":\"d0280527ab6799aa6661a9f5ce9cd94c\",\"timestamp\":1477045008,\"prepayid\":\"wx201610211816485b95e49f850092348788\",\"sign\":\"8A8AA9B2B61923371D43C276B063FAA8\"}");
                if(null != json && !json.has("retcode") ){
                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    req.appId			= json.getString("appid");
                    req.partnerId		= json.getString("partnerid");
                    req.prepayId		= json.getString("prepayid");
                    req.nonceStr		= json.getString("noncestr");
                    req.timeStamp		= json.getString("timestamp");
                    req.packageValue	= json.getString("package");
                    req.sign			= json.getString("sign");
                    req.extData			= "app data"; // optional
                    Toast.makeText(AccountsActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    WXAPIFactory.createWXAPI(this, Constants.APP_ID).sendReq(req);
                    */

            JSONObject json =   Mutils.newJson(orderResponse.getPay_info());// new JSONObject(orderResponse.getPay_info());
            //JSONObject json =   Mutils.newJson("{\"sign\":\"09B7EEB52A13CA3D6C1D1FBC613DD8A0\",\"timestamp\":\"1477045726\",\"noncestr\":\"43ff6b2995844b148bc7e7a076444807\",\"partnerid\":\"1399253002\",\"prepayid\":\"wx20161021182845900d9328d60232483763\",\"package\":\"Sign\\u003dWXPay\",\"appid\":\"wxc128abaecbcfcf45\"}");
                if(null != json && !json.has("retcode") ){
                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    req.appId			= Mutils.getJsonString(json,"appid");//json.getString("appid");
                    req.partnerId		= Mutils.getJsonString(json,"partnerid");//json.getString("partnerid");
                    req.prepayId		= Mutils.getJsonString(json,"prepayid");//json.getString("prepayid");
                    req.nonceStr		= Mutils.getJsonString(json,"noncestr");//json.getString("noncestr");
                    req.timeStamp		= Mutils.getJsonString(json,"timestamp");//json.getString("timestamp");
                    req.packageValue	=  Mutils.getJsonString(json,"package");;//json.getString("package");
                    req.sign			= Mutils.getJsonString(json,"sign");//json.getString("sign");
                   // req.extData			= "app data"; // optional
                 //   Toast.makeText(AccountsActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    WXAPIFactory.createWXAPI(this, Constants.APP_ID).sendReq(req);

                }else{
                    Log.d("PAY_GET", "返回错误"+Mutils.getJsonString(json,"retmsg"));
                    Toast.makeText(AccountsActivity.this, "返回错误"+Mutils.getJsonString(json,"retmsg"), Toast.LENGTH_SHORT).show();
                }
        }catch(Exception e){
            Log.e("PAY_GET", "异常："+e.getMessage());
            Toast.makeText(AccountsActivity.this, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void payAli(){
       // final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(AccountsActivity.this);
                //Log.e("oder_info",orderResponse.getPay_info());
                Map<String, String> result = alipay.payV2(orderResponse.getPay_info(), true);
                //Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为 9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(AccountsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        if(TextUtils.equals(resultStatus, "6001")){
                        Toast.makeText(AccountsActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
                    }else  if(TextUtils.equals(resultStatus, "8000")){
                            Toast.makeText(AccountsActivity.this, "正在处理中", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AccountsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    break;
                }

                default:
                    break;
            }
        };
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
