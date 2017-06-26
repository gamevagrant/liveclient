package com.meetvr.liveshowclient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.meetvr.share.MyApplication;
import com.meetvr.share.control.meetvr.MainInfoUpdate;
import com.meetvr.share.control.meetvr.MpControl;
import com.meetvr.share.control.vr_interface.BaseResponse;
import com.meetvr.share.control.vr_interface.RegistInterface;
import com.meetvr.share.info.MpBalanceInfo;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.reponse.Response;
import com.meetvr.share.request.ModifyNicknameRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.utrils.ImageTools;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.utrils.PreferencesUtils;
import com.meetvr.share.view.CircleImageView;
import com.meetvr.share.view.ProgessDlg;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SecondActivity extends Activity implements View.OnClickListener , MainInfoUpdate,RegistInterface{
	private UserInfo userInfo;
	private MpBalanceInfo mpBalanceInfo;
	private CircleImageView headIimg;

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int REQUEST_READ_CONTACTS = 0;
	private String pngFileName;
	private ProgessDlg progessDlg;
	private AlertDialog alertDialog;
	private static DisplayImageOptions displayOptions;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.second_activity);
		Mutils.setWindowStatusBarColor(this,R.color.title_bg_color);
		iniDisplayOption();
		getUserInfo();
		iniUi();

	}

	private void iniDisplayOption(){
		displayOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	}

	private void getUserInfo(){
		MyApplication application = (MyApplication)getApplication();
		userInfo = application.getUserInfo();
	}
	private void iniUi(){
		try {

			Mutils.setTitle(findViewById(R.id.user_code),"ID:"+userInfo.getId());
			findViewById(R.id.nackname_txt).setOnClickListener(this);
			findViewById(R.id.modify_nickname_img).setOnClickListener(this);
			//findViewById(R.id.eye_sel_layout).setOnClickListener(this);
			findViewById(R.id.setting_info_layout).setOnClickListener(this);
			headIimg = (CircleImageView) findViewById(R.id.live_info_header);

			findViewById(R.id.account_layout).setOnClickListener(this);
			findViewById(R.id.live_info_header).setOnClickListener(this);

			findViewById(R.id.mod_sel_layout).setOnClickListener(this);

			//MyApplication application =(MyApplication) getApplication();

			if(headIimg!=null&&userInfo.getHeadphoto()!=null){
				//application.displayImg(userInfo.getHeadphoto()+"?imageView2/1/w/100/h/100",headIimg,R.drawable.avatar_bg,R.drawable.avatar_bg);
				ImageLoader.getInstance().displayImage(userInfo.getHeadphoto()+"?imageView2/1/w/100/h/100",headIimg,displayOptions);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void OnPubResult(boolean isOk, String live_history_id) {

	}

	@Override
	public void OnMpResult(boolean isOk, MpBalanceInfo mpBalanceInfo) {
		if(isOk){
			this.mpBalanceInfo = mpBalanceInfo;
			Mutils.setTitle(findViewById(R.id.mp_txt),mpBalanceInfo.getMb_balance());
		}
	}

	private void loadUerinfo(){
		MyApplication application = (MyApplication)getApplication();
		if(application.getUserInfo()==null){
			application.loadUserInfo();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(userInfo==null){
			loadUerinfo();
			getUserInfo();
			//Mutils.pointEnter(this,userInfo.getUsercode(),"vrlive__stay","place","5");
			//Mutils.pointEnter(this,userInfo.getUsercode(),"vrlive__stay","intime",""+new Date().getTime());
		}
		Mutils.setTitle(findViewById(R.id.nackname_txt),userInfo.getUsername());
		new MpControl(this,this,userInfo,null);
		setEyeName();




	}

	@Override
	protected void onStop() {
		super.onStop();
		if(userInfo==null){
			//Mutils.pointEnter(this,userInfo.getUsercode(),"vrlive__stay","outtime",""+new Date().getTime());
		}
	}
	private void setEyeName(){
		/*
		int selPos = PreferencesUtils.getSharePreInt(this,"sel",-1);
		switch (selPos){
			case -1:
				Mutils.setTitle(findViewById(R.id.eye_name_txt),"默认眼镜");
				break;
			case 0:
				Mutils.setTitle(findViewById(R.id.eye_name_txt),"暴风墨镜5");
				break;
			case 1:
				Mutils.setTitle(findViewById(R.id.eye_name_txt),"暴风墨镜4");
				break;
			case 2:
				Mutils.setTitle(findViewById(R.id.eye_name_txt),"小鸟看看");
				break;
			case 3:
				Mutils.setTitle(findViewById(R.id.eye_name_txt),"大鹏看看");
				break;
		}
*/		int modsel= PreferencesUtils.getSharePreInt(this,"modsel",0);
		switch (modsel){
			case 0:
				Mutils.setTitle(findViewById(R.id.mod_name_txt),"头瞄模式");
				break;
			case 1:
				Mutils.setTitle(findViewById(R.id.mod_name_txt),"手柄模式");
				break;
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

	private void exitMe(){
		try {
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()){

                default:
                    break;
				case R.id.nackname_txt: //进入到修改资料界面
				case R.id.modify_nickname_img:
					changeNickName();
					break;
				//case R.id.eye_sel_layout:
				//	startPrivateActivity(EyeSelActivity.class);
				//	break;
				case R.id.back:
					finish();
					break;
				case R.id.setting_info_layout:
					startPrivateActivity(SettingActivity.class);

					break;
				case R.id.account_layout:
					//startPrivateActivity(AccountsActivity.class);
					Intent intent = new Intent(this,AccountsActivity.class);
					intent.putExtra("mb",mpBalanceInfo.getMb_balance());
					startActivity(intent);
					break;
				case R.id.live_info_header:
					//startPrivateActivity(ModifyInfoActivity.class);
					showPicturePicker(this);
					break;
				case R.id.mod_sel_layout:
					startPrivateActivity(ModelSelActivity.class);
					break;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void changeNickName(){
		Intent intent = new Intent(this,ModifyPwdActivity.class);
		intent.putExtra(ModifyPwdActivity.FROM_TYPE,0);
		intent.putExtra(ModifyPwdActivity.MODIFY_TYPE,1);
		intent.putExtra(ModifyPwdActivity.IDENTIFIER,userInfo.getId());
		intent.putExtra(ModifyPwdActivity.TOKEN,userInfo.getToken());
		startActivity(intent);
	}

	private void startPrivateActivity(Class<?> cls){
		Intent intent= new Intent(this,cls);
		startActivity(intent);
	}

	@Override
	public void onResulte(boolean isOk) {
		if(isOk) {
			MyApplication application = (MyApplication) getApplication();
			application.saveUser(userInfo);
		}
	}

	@Override
	public void onResulte(boolean isOk, BaseResponse baseResponse) {

	}

	//修改图片
	private void shiwProgress(){
		progessDlg = new ProgessDlg(this,headIimg);
		progessDlg.showPopupWindow();
	}
	private void hideProgess(){
		progessDlg.dismiss();
		progessDlg = null;
	}


	private boolean mayRequestContacts() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}
		if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&(checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
			return true;
		}
		if (shouldShowRequestPermissionRationale(CAMERA)&&shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
			Snackbar.make(headIimg, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
					.setAction(android.R.string.ok, new View.OnClickListener() {
						@Override
						@TargetApi(Build.VERSION_CODES.M)
						public void onClick(View v) {
							requestPermissions(new String[]{CAMERA,WRITE_EXTERNAL_STORAGE}, REQUEST_READ_CONTACTS);
						}
					});
		} else {
			requestPermissions(new String[]{CAMERA,WRITE_EXTERNAL_STORAGE}, REQUEST_READ_CONTACTS);
		}
		return false;
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (requestCode == REQUEST_READ_CONTACTS) {
			if (grantResults.length >=1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// populateAutoComplete();
				showPicturePicker(this);
			}else{
				Toast.makeText(this,"请进入设置，给予觅她VR直播相机和存储权限",Toast.LENGTH_SHORT).show();
			}
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==100){
			if(resultCode == RESULT_OK) {
				// 拿到剪切数据
				if(data!=null){
					Bitmap bmap = data.getParcelableExtra("data");
					if(bmap!=null);
					headIimg.setImageBitmap(bmap);
					pngFileName = String.valueOf(System.currentTimeMillis());
					ImageTools.savePhotoToSDCard(bmap,Environment.getExternalStorageDirectory().getAbsolutePath(), pngFileName);
					//上传该文件
					shiwProgress();
					upLoadFile();
				}


			}

		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case TAKE_PICTURE:
					//将保存在本地的图片取出并缩小后显示在界面上
                  /*
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

                    //将处理过的图片显示在界面上，并保存到本地
                    headIimg.setImageBitmap(newBitmap);
                    String  value =  String.valueOf(System.currentTimeMillis());
                    ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), value);
                    imageUri=  Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/"+value+".png"));

                    cropImageUri(imageUri,100,100,100);
                    //startPhotoZoom(imageUri,100);
                    */

					cropImageUri(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/image.jpg")),100,100,100);
					break;

				case CHOOSE_PICTURE:
					ContentResolver resolver = getContentResolver();
					//照片的原始资源地址
					Uri originalUri = data.getData();
					try {
						//使用ContentProvider通过URI获取原始图片
                        /*
                      Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();

                            headIimg.setImageBitmap(smallBitmap);
                            String  value1 =  String.valueOf(System.currentTimeMillis()) ;
                            ImageTools.savePhotoToSDCard(smallBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), value1);
                            imageUri=  Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/"+value1+".png"));
                            cropImageUri(imageUri,100,100,100);
                            }
                            */
						cropImageUri(originalUri,100,100,100);

						// cropImageUri(originalUri,100,100,100);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;


				default:
					break;
			}
		}
	}


	private Bitmap decodeUriAsBitmap(Uri uri){

		Bitmap bitmap = null;

		try {

			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

		} catch (FileNotFoundException e) {

			e.printStackTrace();

			return null;

		}

		return bitmap;

	}
	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){


		Intent intent = new Intent();

		intent.setAction("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);// 输出图片大小
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent,requestCode);



	}




	public void showPicturePicker(Context context){
		if(!mayRequestContacts()){
			return ;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = getLayoutInflater().inflate(R.layout.dialog_capture,null);
		builder.setView(view);
		view.findViewById(R.id.tackpic).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
						Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
						//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
						openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
						startActivityForResult(openCameraIntent, TAKE_PICTURE);
				       alertDialog.dismiss();
			}
		});
		view.findViewById(R.id.selec_pic).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
						Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
						openAlbumIntent.setType("image/*");
						startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
						alertDialog.dismiss();
			}
		});

		//builder.setTitle("");
	//	builder.setNegativeButton("取消", null);
//		builder.setItems(new String[]{"拍照","相册"}, new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				switch (which) {
//					case TAKE_PICTURE:
//						Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//						Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
//						//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//						openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//						startActivityForResult(openCameraIntent, TAKE_PICTURE);
//						break;
//
//					case CHOOSE_PICTURE:
//						Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
//						openAlbumIntent.setType("image/*");
//						startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
//						break;
//
//					default:
//						break;
//				}
//			}
//		});
		alertDialog =builder.show();
	}

	//http://cdn.store-online.moxiangtv.com
	private void upLoadFile(){
		UploadManager uploadManager = new UploadManager();
		String data = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+pngFileName+".png";
		String key ="user/headpic/"+ pngFileName+".png";
		String token = userInfo.getQiniu_token();
		uploadManager.put(data, key, token,
				new UpCompletionHandler() {
					@Override
					public void complete(String key, ResponseInfo info, JSONObject res) {
						//res包含hash、key等信息，具体字段取决于上传策略的设置。
						//文件上传挖成
						if(info.isOK()){
							//修改头像地址
							try {
								String urlImg = userInfo.getQiniu_file_domain();
								if(urlImg.substring(urlImg.length()-1,urlImg.length()).equals("/")){

								}else{
									urlImg+="/";
									userInfo.setQiniu_file_domain(urlImg);
								}
								saveHeaderPic();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{
							Toast.makeText(SecondActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
							hideProgess();

							//Qiniu token过期
							if(info.error.equals("expired token"))
							{
								SecondActivity.this.sendBroadcast(new Intent("com.login.again"));
							}
						}

					}
				}, null);

//        uploadManager.put(data, key, token,handler,
//                new UploadOptions(null, null, false,
//                        new UpProgressHandler(){
//                            public void progress(String key, double percent){
//                                Log.i("qiniu", key + ": " + percent);
//                            }
//                        }, null));
	}

	private void updatePicData(String picUrl){

		JSONObject jsonObject = Mutils.newJson(userInfo.jsonValue);
		try {
			jsonObject.put("head_pic",picUrl);
			userInfo.jsonValue = jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void saveHeaderPic(){

		ModifyNicknameRequest modifyNicknameRequest = new ModifyNicknameRequest(userInfo.getToken(),userInfo.getId());
		// modifyPwdRequest.setUrlParam();
		modifyNicknameRequest.setParam(userInfo.getId(),null,userInfo.getQiniu_file_domain()+"user/headpic/"+pngFileName+".png");
		modifyNicknameRequest.addQuery(this, new Request.OnNetWorkProc() {
			@Override
			public void onStart() {

			}


			@Override
			public void onRespon(JSONObject response, boolean isOK, String errMsg) {
				try {
					if(isOK){
						Response response1 = new Response();
						response1.parse(response);
						if(Mutils.isResponseOk(response1.getAct_stat())){
							Toast.makeText(SecondActivity.this,"头像修改成功", Toast.LENGTH_SHORT).show();
							String urlImg = userInfo.getQiniu_file_domain();
							if(urlImg.substring(urlImg.length()-1,urlImg.length()).equals("/")){
								((MyApplication)getApplication()).getUserInfo().setHeadphoto(userInfo.getQiniu_file_domain()+"user/headpic/"+pngFileName+".png");
							}else{
								((MyApplication)getApplication()).getUserInfo().setHeadphoto(userInfo.getQiniu_file_domain()+"/"+"user/headpic/"+pngFileName+".png");
							}
							updatePicData(userInfo.getHeadphoto());
							((MyApplication)getApplication()).saveUser(((MyApplication)getApplication()).getUserInfo());
							}else{
							Toast.makeText(SecondActivity.this,response1.getErr_msg(), Toast.LENGTH_SHORT).show();
						}

					}else{
						Toast.makeText(SecondActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
//						if(errMsg!=null&&errMsg.length()>0){
//							Toast.makeText(SecondActivity.this,errMsg, Toast.LENGTH_SHORT).show();
//						}else{
//							Toast.makeText(SecondActivity.this,R.string.net_con_error, Toast.LENGTH_SHORT).show();
//						}

					}
					hideProgess();

				} catch (Resources.NotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}


}
