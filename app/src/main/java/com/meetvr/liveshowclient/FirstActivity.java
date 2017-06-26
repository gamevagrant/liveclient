package com.meetvr.liveshowclient;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.MediaSyncEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.util.RecyclerViewUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.meetvr.liveshowclient.weight.ListBaseAdapter;
import com.meetvr.liveshowclient.weight.LiveHallItemData;
import com.meetvr.liveshowclient.weight.RoomListInterface;
import com.meetvr.liveshowclient.weight.Room_Lists;
import com.meetvr.liveshowclient.weight.SampleHeader;
import com.meetvr.share.MyApplication;
import com.meetvr.share.control.meetvr.Control;
import com.meetvr.share.control.meetvr.MpControl;
import com.meetvr.share.control.vr_interface.BaseResponse;
import com.meetvr.share.control.vr_interface.RegistInterface;
import com.meetvr.share.info.BannerInfo;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.reponse.BannerResponse;
import com.meetvr.share.reponse.Response;
import com.meetvr.share.request.BannerRequest;
import com.meetvr.share.request.CheckRoomStateRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.utrils.Constants;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.utrils.NetworkUtils;
import com.meetvr.share.utrils.PreferencesUtils;
import com.meetvr.share.view.CircleImageView;
import com.meetvr.share.view.ProgessDlg;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;



public class FirstActivity extends Activity implements RoomListInterface ,RegistInterface{

	/*
	 下来列表的定义
	 */
	private static final String TAG = "lzx";

	/**
	 * 服务器端一共多少条数据
	 */
	private static  int TOTAL_COUNTER = 1000;//最大不能超过1000条数据

	/**
	 * 每一页展示多少条数据
	 */
	private static  int REQUEST_COUNT = 10;

	/**
	 * 已经获取到多少条数据了
	 */
	private static int mCurrentCounter = 0;

	private LRecyclerView mRecyclerView = null;

	private DataAdapter mDataAdapter = null;

	private PreviewHandler mHandler = new PreviewHandler(this);
	private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

	private boolean isRefresh = false;

	private SampleHeader sampleHeader;

	private int mPosition;

	//---bander
	private ViewPager viewPager;//页卡内容
	private List<View> views = new ArrayList<View>();// Tab页面列表
	private List<View> selViews = new ArrayList<View>();
	//private LayoutInflater mLayoutInflater;
	private LinearLayout selPointView;
	private ImageView seledView;
	//private ArrayList<GiftInfo> gifts;
	private BannerResponse bannerRespons =new BannerResponse();
	private int selBanderPos = 0;


	private UserInfo userInfo;

	private AlertDialog alertDialog;
	private ProgessDlg progessDlg;
	private boolean isHavaeHeader = false;
	ImageLoader imageLoader = ImageLoader.getInstance();
	private static DisplayImageOptions displayOptions;

	/*
	  轮播bander图的事件
	 */
	private static final int BANDER_IDX_MSG=10;
	private Thread mThreadBander=null;

	private boolean isFirstTime = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.first_activity);
		Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
		getUserInfo();
		imgLoadConfig();
		iniUi();
		crateBanderThread();

	}

	private void imgLoadConfig(){
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());

		displayOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	private void loadUerinfo(){
		MyApplication application = (MyApplication)getApplication();
		if(application.getUserInfo()==null){
			application.loadUserInfo();
		}
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



	private void getUserInfo(){
        MyApplication application = (MyApplication) getApplication();
        userInfo = application.getUserInfo();

    }

	private void RefreshDataContent()
	{
		RecyclerViewStateUtils.setFooterViewState(mRecyclerView,LoadingFooter.State.Normal);
		//cleanAllview();
		mDataAdapter.clear();
		mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
		mCurrentCounter = 0;
		isRefresh = true;
		requestData();
	}
	private void iniUi(){
		Mutils.setTitle(findViewById(R.id.title_txt),"热门直播");
		findViewById(R.id.back).setVisibility(View.GONE);

		///------------

		mRecyclerView = (LRecyclerView) findViewById(R.id.list);


		mDataAdapter = new DataAdapter(this);
		mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
		mRecyclerView.setAdapter(mLRecyclerViewAdapter);

		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
		sampleHeader = new SampleHeader(this);
//		RecyclerViewUtils.setHeaderView(mRecyclerView, sampleHeader);
		//RecyclerViewUtils.setHeaderView(mRecyclerView, new SampleHeader(this));

		mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if(!isFirstTime)
				{
					RefreshDataContent();
				}
			}
		});

		mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
				if(state == LoadingFooter.State.Loading) {
					Log.d(TAG, "the state is Loading, just wait..");
					return;
				}

				if (mCurrentCounter < TOTAL_COUNTER) {
					// loading more
					RecyclerViewStateUtils.setFooterViewState(FirstActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
					requestData();
				} else {
					//the end
					RecyclerViewStateUtils.setFooterViewState(FirstActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);

				}
			}
		});

		mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {

			@Override
			public void onScrollUp() {
			}

			@Override
			public void onScrollDown() {
			}


			@Override
			public void onScrolled(int distanceX, int distanceY) {
			}

			@Override
			public void onScrollStateChanged(int state) {

			}

		});


		mRecyclerView.setRefreshing(true);

		mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				//LiveHallItemData item = mDataAdapter.getDataList().get(position);
				//AppToast.showShortText(EndlessLinearLayoutActivity.this, item.title);
				mPosition = position;
				if(mayRequestAudioRecord()){

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
						Toast.makeText(FirstActivity.this,"您禁用了录音权限，请到设置打开",Toast.LENGTH_SHORT).show();
					}
					record.stop();

					checkNetWork();
				}

			}

			@Override
			public void onItemLongClick(View view, int position) {
				//LiveHallItemData item = mDataAdapter.getDataList().get(position);
				//AppToast.showShortText(EndlessLinearLayoutActivity.this, "onItemLongClick - " + item.title);
			}
		});
		selPointView = (LinearLayout)sampleHeader.findViewById(R.id.sel_point);
		viewPager=(ViewPager) sampleHeader.findViewById(R.id.viewPager);


		//iniViewPage();

	}

	private void showProgess(){
		try {
			if(progessDlg!=null)
			{
				progessDlg.dismiss();
				progessDlg = null;
			}
			progessDlg = new ProgessDlg(this,viewPager);
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

		//Intent intent = new Intent(this,com.meetvr.liveshowclient.VrStartActivity.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private int selectIndex = -1;
	private void startVr(int position){
		selectIndex = position;

		/*
		Intent intent = new Intent(this,com.meetvr.liveshowclient.VrStartActivity.class);
		intent.putExtra("meetvr_live",getsJsonData(position));
		startActivity(intent);
		*/

		//先检查房间是否能进入
		LiveHallItemData item = mDataAdapter.getDataList().get(selectIndex);
		if(null==item)
		{
			return;
		}
		CheckRoomStateRequest checkRequest = new CheckRoomStateRequest(userInfo.getToken(),userInfo.getId());
		checkRequest.addParam((int)item.getId(),item.getType());
		checkRequest.addQuery(this, new Request.OnNetWorkProc() {
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
						JSONObject object= response1.parse(response);
						if(Mutils.isResponseOk(response1.getAct_stat())){
							String ret = Mutils.getJsonString(object,"open");
							if(ret.equals("true"))
							{
								Intent intent = new Intent(FirstActivity.this,VrStartActivity.class);
								intent.putExtra("meetvr_live",getsJsonData(selectIndex));
								startActivity(intent);
							}
							else
							{
								RefreshDataContent();
								Toast.makeText(FirstActivity.this,"该直播已经结束",Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(FirstActivity.this,response1.getErr_msg(),Toast.LENGTH_SHORT).show();
						}


					}else{
						Toast.makeText(FirstActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();

//                            if(errMsg!=null&&errMsg.length()>0){
//                                Toast.makeText(RegistActivity.this,errMsg,Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(RegistActivity.this,R.string.net_con_error,Toast.LENGTH_SHORT).show();
//                            }

					}
					hideProgress();

				} catch (Exception e) {
					hideProgress();
					e.printStackTrace();
				}

			}
		});
	}

	private String getsJsonData(int pos){

		try {
			LiveHallItemData item = mDataAdapter.getDataList().get(pos);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("eye_type", PreferencesUtils.getSharePreInt(this,"sel"));
			jsonObject.put("is_hand", (PreferencesUtils.getSharePreInt(this,"modsel")==1)?true:false);
			jsonObject.put("start_type",1);
			jsonObject.put("login_data", userInfo.jsonValue);
			jsonObject.put("item_data", item.jsonValue);
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

	private void notifyDataSetChanged() {
		mLRecyclerViewAdapter.notifyDataSetChanged();

	}

	private void addItems(ArrayList<LiveHallItemData> list) {

		mDataAdapter.addAll(list);
		mCurrentCounter += list.size();

	}

	private static class PreviewHandler extends Handler {

		private WeakReference<FirstActivity> ref;

		PreviewHandler(FirstActivity activity) {
			ref = new WeakReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			final FirstActivity activity = ref.get();
			if (activity == null || activity.isFinishing()) {
				return;
			}
			switch (msg.what) {

				case -1:
					if(activity.isRefresh){
						activity.mDataAdapter.clear();
						mCurrentCounter = 0;
					}

					int currentSize = activity.mDataAdapter.getItemCount();

					//模拟组装10个数据
					ArrayList<LiveHallItemData> newList = new ArrayList<>();
					for (int i = 0; i < 10; i++) {
						if (newList.size() + currentSize >= TOTAL_COUNTER) {
							break;
						}

						LiveHallItemData item = new LiveHallItemData();
						//item.id = currentSize + i;
						//item.title = "item" + (item.id);

						newList.add(item);
					}


					activity.addItems(newList);

					if(activity.isRefresh){
						activity.isRefresh = false;
						activity.mRecyclerView.refreshComplete();
					}

					RecyclerViewStateUtils.setFooterViewState(activity.mRecyclerView, LoadingFooter.State.Normal);
					activity.notifyDataSetChanged();
					break;
				case -2:
					activity.notifyDataSetChanged();
					break;
				case -3:
					if(activity.isRefresh){
						activity.isRefresh = false;
						activity.mRecyclerView.refreshComplete();
					}
					activity.notifyDataSetChanged();
					RecyclerViewStateUtils.setFooterViewState(activity, activity.mRecyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, activity.mFooterClick);
					break;
				case BANDER_IDX_MSG:
					activity.changeBander();
					break;
				default:
					break;
			}
		}
	}

	private void crateBanderThread(){
		mThreadBander = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true){
					try {
						Thread.sleep(10*1000);
						mHandler.sendEmptyMessage(BANDER_IDX_MSG);
						Log.i(TAG,"create bander thread");
					} catch (InterruptedException e) {
						e.printStackTrace();
						Log.i(TAG,"create bander thread--break");
						break;
					}
				}
			}
		});
		mThreadBander.start();
	}

	public void changeBander(){
		try {
			if(views!=null&&views.size()>1&&viewPager!=null){
                int pos = (selBanderPos + 1)%views.size();
                viewPager.setCurrentItem(pos);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if(mThreadBander!=null){
                mThreadBander.interrupt();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private View.OnClickListener mFooterClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			RecyclerViewStateUtils.setFooterViewState(FirstActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
			requestData();
		}
	};


    @Override
    public void onResulte(boolean isOK, ArrayList<LiveHallItemData> datas) {
		//Toast.makeText(FirstActivity.this,"come",Toast.LENGTH_SHORT).show();
        if(isOK){
            if(datas==null){
				isRefresh = false;
				mRecyclerView.refreshComplete();
                notifyDataSetChanged();
                RecyclerViewStateUtils.setFooterViewState(FirstActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }else{
                if(isRefresh){
                    mDataAdapter.clear();
                    mCurrentCounter = 0;
					TOTAL_COUNTER = 1000;
                }

               // int currentSize = mDataAdapter.getItemCount();

                addItems(datas);

				if(datas.size()<10){
					TOTAL_COUNTER = mCurrentCounter;
					REQUEST_COUNT = datas.size();
					if(!isRefresh){
						RecyclerViewStateUtils.setFooterViewState(FirstActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
					}

					if(isRefresh){
						isRefresh = false;
						mRecyclerView.refreshComplete();
					}

				}else {

					if(isRefresh){
						isRefresh = false;
						mRecyclerView.refreshComplete();
					}
					RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
				}
                notifyDataSetChanged();
            }
        }else{
            if(isRefresh){
                isRefresh = false;
                mRecyclerView.refreshComplete();
            }
           notifyDataSetChanged();
            RecyclerViewStateUtils.setFooterViewState(this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, mFooterClick);
        }

    }

    /**
	 * 模拟请求网络
	 */
	private void requestData() {
		Log.d(TAG, "requestData");
        /*
		new Thread() {

			@Override
			public void run() {
				super.run();

				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				//模拟一下网络请求失败的情况
				if(NetworkUtils.isNetAvailable(FirstActivity.this)) {
					mHandler.sendEmptyMessage(-1);
				} else {
					mHandler.sendEmptyMessage(-3);
				}
			}
		}.start();
		*/
		if(userInfo==null){
			loadUerinfo();
			getUserInfo();
		}
		if(mCurrentCounter==0){
			BannerRequest bannerRequest = new BannerRequest(userInfo.getToken(),userInfo.getId());
			bannerRequest.addParam(1);
			bannerRespons.getDatas().clear();
			new Control(this,this,bannerRequest,bannerRespons,null);

		}
        new Room_Lists().getMore(this,userInfo.getToken(),userInfo.getId(),this,mCurrentCounter);

	}

	@Override
	public void onResulte(boolean isOk) {

	}

	@Override
	public void onResulte(boolean isOk, BaseResponse baseResponse) {
		iniViewPage();
	}


	@Override
	protected void onResume() {
		super.onResume();
		/*
		if(userInfo==null){
			Mutils.pointEnter(this,userInfo.getUsercode(),"vrlive__stay","place","4");
			Mutils.pointEnter(this,userInfo.getUsercode(),"vrlive__stay","intime",""+new Date().getTime());
		}
		*/
		isFirstTime = false;
		RefreshDataContent();

	}

	@Override
	protected void onStop() {
		super.onStop();
		/*
		if(userInfo==null){
			Mutils.pointEnter(this,userInfo.getUsercode(),"vrlive__stay","outtime",""+new Date().getTime());
		}
		*/
	}

	private class DataAdapter extends ListBaseAdapter<LiveHallItemData> {

		private LayoutInflater mLayoutInflater;

		public DataAdapter(Context context) {
			mLayoutInflater = LayoutInflater.from(context);
			mContext = context;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new ViewHolder(mLayoutInflater.inflate(R.layout.list_item_text, parent, false));
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
			LiveHallItemData item = mDataList.get(position);

            try {
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.textView.setText(item.getNickname());

                if(item.getType().equals("living")){
                    viewHolder.moveType.setImageResource(R.drawable.ic_mod_live);
					viewHolder.viewTip.setText("人在看");
					viewHolder.peasonCount.setText(item.getView_count());
                }else{
                    viewHolder.moveType.setImageResource(R.drawable.ic_mod_replay);
					viewHolder.viewTip.setText("次观看");
					viewHolder.peasonCount.setText(item.getView_count());
                }
                MyApplication application = (MyApplication)getApplication();

                application.displayImg(item.getHead_pic()+"?imageView2/1/w/139/h/139",viewHolder.headImg,R.drawable.avatar_bg,R.drawable.avatar_bg);
				//imageLoader.displayImage(item.getRoom_show_pic()+"?imageView2/1/w/1080/h/810", viewHolder.roomPic);

				//System.out.print(item.getRoom_show_pic()+"?imageView2/1/w/1080/h/810");
				Log.e(TAG,item.getRoom_show_pic()+"?imageView2/1/w/1080/h/810");
				ImageLoader.getInstance().displayImage(item.getRoom_show_pic()+"?imageView2/1/w/1080/h/810", viewHolder.roomPic,displayOptions);
               // application.displayImg(item.getRoom_show_pic()+"?imageView2/1/w/1080/h/810",viewHolder.roomPic,R.drawable.pic,R.drawable.pic);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

		private class ViewHolder extends RecyclerView.ViewHolder {

			private TextView textView,peasonCount,viewTip;//nickename
            private CircleImageView headImg;
            private ImageView moveType,roomPic;


			public ViewHolder(View itemView) {
				super(itemView);
				textView = (TextView) itemView.findViewById(R.id.nickname_text);
                peasonCount=(TextView)itemView.findViewById(R.id.living_room_count);
                headImg = (CircleImageView)itemView.findViewById(R.id.live_info_header);
                moveType = (ImageView)itemView.findViewById(R.id.move_type);
                roomPic  = (ImageView)itemView.findViewById(R.id.rom_pic);
				viewTip = (TextView)itemView.findViewById(R.id.view_show_tip);

			}
		}
	}



	private void cleanAllview(){
		try {
			views.clear();
			selViews.clear();
			selPointView.removeAllViews();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//选项卡
	private void iniViewPage(){

		//添加两个测试
		//mThreadBander.interrupt();
		cleanAllview();
		int len = bannerRespons.getDatas().size();
		if(len<=0){
			//sampleHeader.setVisibility(View.GONE);
			if(isHavaeHeader){
				RecyclerViewUtils.removeHeaderView(mRecyclerView);
				isHavaeHeader = false;
			}

			return;
		}
		//sampleHeader.setVisibility(View.VISIBLE);
		if(isHavaeHeader==false){
			RecyclerViewUtils.setHeaderView(mRecyclerView,sampleHeader);
			isHavaeHeader = true;
		}
		selBanderPos =0;

		for(int i=0;i<len;i++){
			View view = getLayoutInflater().inflate(R.layout.page_item, null);
			if(bannerRespons.getDatas().size()>0){
				ImageView img = (ImageView) view.findViewById(R.id.binder_img);
				MyApplication application = (MyApplication)getApplication();
				application.displayImg(bannerRespons.getDatas().get(i).getBanner_img()+"?imageView2/1/w/1080/h/300",img,R.drawable.defautle_bander,R.drawable.pic_banner);
				Log.d("banderurl",bannerRespons.getDatas().get(i).getBanner_img()+"?imageView2/1/w/1080/h/300");
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						try {

							BannerInfo bannerInfo = bannerRespons.getDatas().get(selBanderPos);
							if((bannerInfo.getEnd_time()>0) &&( new Date().getTime()>bannerInfo.getEnd_time())){
								Toast.makeText(FirstActivity.this,"已经结束",Toast.LENGTH_SHORT).show();
								return;
							}
							if(bannerInfo.getBanner_url().length()<2)
							{
								return;
							}
							Intent intent = new Intent(FirstActivity.this,WebViewActivity.class);
							intent.putExtra("url",bannerInfo.getBanner_url());
							intent.putExtra("name",bannerInfo.getBanner_name());
							startActivity(intent);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			views.add(view);
			ImageView imageView  = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.leftMargin = 10;
			params.rightMargin=10;
			imageView.setLayoutParams(params);
			if(i==0){
				imageView.setImageResource(R.drawable.ic_ban_checked);
				seledView = imageView;
			}else{
				imageView.setImageResource(R.drawable.ic_ban_unchecked);
			}
			selViews.add(imageView);
			selPointView.addView(imageView);
		}
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		//mThreadBander.resume();

	}


	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)   {
			container.removeView(mListViews.get(position));
		}


		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return  mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}

	}


	public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


		public void onPageScrollStateChanged(int arg0) {


		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {


		}

		public void onPageSelected(int arg0) {
			try {
				ImageView view = (ImageView) selViews.get(arg0);
				if(seledView!=null){
					seledView.setImageResource(R.drawable.ic_ban_unchecked);
				}
				view.setImageResource(R.drawable.ic_ban_checked);
				seledView = view;
				selBanderPos = arg0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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
			Snackbar.make(this.mRecyclerView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
				checkNetWork();
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
			view.findViewById(R.id.content_show_btn_c).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					alertDialog.dismiss();
				}
			});
			view.findViewById(R.id.content_show_btn_e).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					startVr(mPosition);
					alertDialog.dismiss();
				}
			});
			alertDialog =builder.show();
		}else if(!NetworkUtils.isNetAvailable(this)){
			view.findViewById(R.id.content_show_btn_c).setVisibility(View.GONE);
			view.findViewById(R.id.btn_line).setVisibility(View.GONE);
			textView.setText("当前为网络已断开,请连接网络");
			view.findViewById(R.id.content_show_btn_e).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					alertDialog.dismiss();
				}
			});
			alertDialog =	builder.show();
		}else if(NetworkUtils.isWifi(this)){
			startVr(mPosition);
		}
	}

}
