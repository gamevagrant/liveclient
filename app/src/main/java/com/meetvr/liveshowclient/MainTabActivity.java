package com.meetvr.liveshowclient;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.meetvr.share.MyApplication;
import com.meetvr.share.control.update.AppUpDate;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.reponse.Response;
import com.meetvr.share.request.ClaimRewardRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.utrils.Constants;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.utrils.NetworkUtils;
import com.meetvr.share.utrils.PreferencesUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainTabActivity extends TabActivity implements OnClickListener {

	private TabHost mTabHost;
	private final String FIRST_TAB="FIRST_TAB";
	private final String SECOND_TAB="SECOND_TAB";
	private final String THREE_TAB="THREE_TAB";
	private final String FOUR_TAB = "FOUR_TAB";

	private UserInfo userInfo;
	private BroadcastReceiver receiver;
	private final String mPageName = "AnalyticsLiveClient";

	private AlertDialog alertDialog;

	private boolean authorFailed = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(Window.FEATURE_NO_TITLE, 0);
		 setContentView(R.layout.activity_main_tab);
		try {
			if(1== PreferencesUtils.getSharePreInt(this,"test_mode",0))
			{
				Constants.TestMode = true;
			}

			//透明状态栏
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
			loadUerinfo();
			getInfo();
			update();
			//MobclickAgent.setDebugMode(true);
			// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
			// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
			MobclickAgent.openActivityDurationTrack(false);
			// MobclickAgent.setAutoLocation(true);
			// MobclickAgent.setSessionContinueMillis(1000);
			// MobclickAgent.startWithConfigure(
			// new UMAnalyticsConfig(mContext, "4f83c5d852701564c0000011", "Umeng", EScenarioType.E_UM_NORMAL));
			MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 iniUi();
		 switchBtn(FIRST_TAB, R.drawable.main_p, R.drawable.main_p, R.drawable.person_n, R.drawable.main_n);

		autoClaimReward();
	}
	private void update(){
		if(mayRequestStorage()){
			new AppUpDate(this,false).checkUpDate();
		}
	}
	private void loadUerinfo(){
		MyApplication application = (MyApplication)getApplication();
		if(application.getUserInfo()==null){
			application.loadUserInfo();
		}
	}
	private void getInfo(){
		MyApplication application = (MyApplication)getApplication();
		userInfo = application.getUserInfo();
	}

	private void iniUi() {
		try {
			mTabHost = getTabHost();
			addTab(FIRST_TAB, FirstActivity.class);
			addTab(SECOND_TAB, SecondActivity.class);
			//addTab(THREE_TAB, ThreeActivity.class);
			// addTab(FOUR_TAB, FourActivity.class);

			findViewById(R.id.first_btn).setOnClickListener(this);
			findViewById(R.id.first_layout).setOnClickListener(this);

			findViewById(R.id.second_btn).setOnClickListener(this);
			findViewById(R.id.second_layout).setOnClickListener(this);

			findViewById(R.id.three_btn).setOnClickListener(this);
			findViewById(R.id.three_layout).setOnClickListener(this);

			// findViewById(R.id.four_btn).setOnClickListener(this);
			// findViewById(R.id.four_layout).setOnClickListener(this);
			registBroadCast();


		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	private boolean mayRequestStorage() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}
		if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
			return true;
		}
		if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
			Snackbar.make(this.mTabHost, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);

	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);

	}

	private boolean mayRequestAudioRecord() {
		/*
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}
		if ((checkSelfPermission(RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
			return true;
		}
		if (shouldShowRequestPermissionRationale(RECORD_AUDIO)) {
			Snackbar.make(this.mTabHost, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
					.setAction(android.R.string.ok, new View.OnClickListener() {
						@Override
						@TargetApi(Build.VERSION_CODES.M)
						public void onClick(View v) {
							requestPermissions(new String[]{RECORD_AUDIO}, 300);
						}
					});
		} else {
			requestPermissions(new String[]{RECORD_AUDIO}, 300);
		}
		return false;
		*/

		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.RECORD_AUDIO},
                    300);
            return false;
		}
		return true;
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (requestCode == 300) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// populateAutoComplete();
				checkNetWork();
			}else{
				Toast.makeText(this,"您禁用了录音权限，请到设置打开",Toast.LENGTH_SHORT).show();
				//请前往设置页面并
				// 开启摩象直播的录音权限
				//2行，居中对齐显示
				checkNetWork();
			}
		}
		if(requestCode==400){
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				new AppUpDate(this,false).checkUpDate();
			}else{
				Toast.makeText(this,"禁用写入扩展卡将无法升级",Toast.LENGTH_SHORT).show();
			}
		}
	}





	private void checkNetWork(){
		//if(!NetworkUtils.isNetAvailable(this)){
		AlertDialog.Builder builder =  new AlertDialog.Builder(this);//.setMessage("nihao").setPositiveButton("确定",null);
		View view = getLayoutInflater().inflate(R.layout.dialog,null);
		builder.setView(view);
		TextView textView = (TextView)view.findViewById(R.id.content_show_tip);
		if(NetworkUtils.isMobile(this)){
			textView.setText("当前为移动网络是否继续");
			view.findViewById(R.id.content_show_btn_c).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					alertDialog.dismiss();
				}
			});
			view.findViewById(R.id.content_show_btn_e).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					alertDialog.dismiss();
					startVr();
				}
			});
			alertDialog = builder.show();
		}else if(!NetworkUtils.isNetAvailable(this)){
			view.findViewById(R.id.content_show_btn_c).setVisibility(View.GONE);
			view.findViewById(R.id.btn_line).setVisibility(View.GONE);
			textView.setText("当前为网络已断开,请连接网络");
			view.findViewById(R.id.content_show_btn_e).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					alertDialog.dismiss();
				}
			});
		alertDialog =	builder.show();
		}else if(NetworkUtils.isWifi(this)){
			startVr();
		}
	}
	private void startVr() {


			Intent intent = new Intent(this,com.meetvr.liveshowclient.VrStartActivity.class);
			intent.putExtra("meetvr_live",getsJsonData());
			startActivity(intent);


	}


	@Override
	protected void onDestroy() {

		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
	
   private void addTab(String tabName, Class<?> cls){
    	
    	Intent integer = new Intent();
    	integer.setClass(this, cls);
    	TabSpec tabSpec = mTabHost.newTabSpec(tabName);
    	tabSpec.setIndicator(tabName, null);
    	tabSpec.setContent(integer);
    	mTabHost.addTab(tabSpec);
    	
    }
	
   private void switchBtn(String stringTab, int fId, int sId, int tId, int ffId){
   	mTabHost.setCurrentTabByTag(stringTab);
   	
   	
   	ImageView firstView  = (ImageView) findViewById(R.id.first_btn);
   	//ImageView secondView  = (ImageView) findViewById(R.id.second_btn);
   	ImageView threeView  = (ImageView) findViewById(R.id.three_btn);
   	//ImageView fourView = (ImageView) findViewById(R.id.four_btn);
   	firstView.setImageResource(fId);
   	//secondView.setImageResource(sId);
   	threeView.setImageResource(tId);
   	//fourView.setImageResource(ffId);
   	
   	
   }
   	
   /*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_tab, menu);
		return true;
	}
*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.first_layout:
		case R.id.first_btn:
			switchBtn(FIRST_TAB, R.drawable.main_p, R.drawable.main_p, R.drawable.person_n, R.drawable.main_n);
			break;
			
		case R.id.second_layout:
		case R.id.second_btn:
			if(mayRequestAudioRecord()){
				//-----------------------------------------呼出权限
				int frequence = 8000; //录制频率，单位hz.这里的值注意了，写的不好，可能实例化AudioRecord对象的时候，会出错。我开始写成11025就不行。这取决于硬件设备
				int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
				int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
				int bufferSize = AudioRecord.getMinBufferSize(frequence, channelConfig, audioEncoding);
				//实例化AudioRecord
				AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, frequence, channelConfig, audioEncoding, bufferSize);
				record.startRecording();

				byte[] data = new byte[512];
				int read = record.read(data, 0, 512);
				if(AudioRecord.ERROR_INVALID_OPERATION != read){
					// 做正常的录音处理
					//Toast.makeText(FirstActivity.this,"可以正常录音",Toast.LENGTH_SHORT).show();
				} else {
					//录音可能被禁用了，做出适当的提示
					Toast.makeText(this,"您禁用了录音权限，请到设置打开",Toast.LENGTH_SHORT).show();
				}
				record.stop();
				//-----------------------------------------
				checkNetWork();
			}

			break;
		case R.id.three_layout:
		case R.id.three_btn:
			switchBtn(SECOND_TAB, R.drawable.main_n, R.drawable.main_p, R.drawable.person_p, R.drawable.person_p);
			
			break;
//		case R.id.four_layout:
//		case R.id.four_btn:
//			switchBtn(FOUR_TAB, R.drawable.one_normal, R.drawable.two_normal, R.drawable.three_normal, R.drawable.four_active);
//			break;
			case R.id.content_show_btn_e:
				startVr();
		default:
			break;
		}

	}

	private String getsJsonData(){

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("eye_type", PreferencesUtils.getSharePreInt(this,"sel"));
			jsonObject.put("is_hand",  (PreferencesUtils.getSharePreInt(this,"modsel")==1)?true:false);
			jsonObject.put("start_type",0);
			jsonObject.put("login_data", userInfo.jsonValue);
			jsonObject.put("item_data", "");
			jsonObject.put("user_id", "");
			jsonObject.put("password", "");
			jsonObject.put("test_mode", Constants.TestMode);
			jsonObject.put("app_ver", Constants.APP_VER);
			jsonObject.put("session", "");
			int channel = ((MyApplication)getApplication()).GetChannelId();
			jsonObject.put("channel", channel);
			Log.d("Unity","channel="+channel);
			return jsonObject.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		try {
			if(keyCode== KeyEvent.KEYCODE_BACK){
				Intent i= new Intent(Intent.ACTION_MAIN);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.addCategory(Intent.CATEGORY_HOME);
				startActivity(i);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		return super.onKeyDown(keyCode, event);
	}
	public void registBroadCast(){
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals("com.kill.me")){
					finish();
				}
				if(intent.getAction().equals("com.login.again")){
					loginAgain();
				}

			}
		};
		IntentFilter  intentFilter = new IntentFilter();
		intentFilter.addAction("com.kill.me");
		intentFilter.addAction("com.login.again");
		registerReceiver(receiver,intentFilter);
	}


	//---重新登录提示

	private void loginAgain(){

		authorFailed = true;
		AlertDialog.Builder builder =  new AlertDialog.Builder(this);//.setMessage("nihao").setPositiveButton("确定",null);
		View view = getLayoutInflater().inflate(R.layout.dialog,null);
		builder.setView(view);
		TextView textView = (TextView)view.findViewById(R.id.content_show_tip);
		textView.setText("认证失效，请重新登录");
		view.findViewById(R.id.content_show_btn_c).setVisibility(View.GONE);
		view.findViewById(R.id.content_show_btn_e).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				alertDialog.dismiss();
				exitMe();
			}
		});
		builder.setCancelable(false);
		alertDialog =	builder.show();

	}

	private void exitMe(){
		try {
			PreferencesUtils.putSharePre(this,"login_type",0); //1 账号登录  2 微信登录
			MyApplication application = (MyApplication)getApplication();
			application.deleteUser();
			Intent intent1 = new Intent(this,GuideActivity.class);
			startActivity(intent1);
			sendBroadcast(new Intent("com.kill.me"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void autoClaimReward()
	{
		ClaimRewardRequest request = new ClaimRewardRequest(userInfo.getToken(),userInfo.getId());
		request.addQuery(this, new Request.OnNetWorkProc() {
			@Override
			public void onStart() {

			}
			@Override
			public void onRespon(JSONObject response, boolean isOK, String errMsg) {
				try {
					if(isOK){
						Response resp = new Response();
						JSONObject object =  resp.parse(response);
						if(Mutils.isResponseOk(resp.getAct_stat()))
						{
							int reward = Mutils.getJsonInt(object,"mb_get");
							Toast.makeText(MainTabActivity.this,"今日登陆成功领取"+reward+"摩币", Toast.LENGTH_SHORT).show();
							Log.i("meetvr", "ClaimRewardRequest ok ");
						}
						else
						{
							Log.i("meetvr", "ClaimRewardRequest failed ");
						}
					}
					else
					{
						Log.i("meetvr", "ClaimRewardRequest failed ");
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}


}
