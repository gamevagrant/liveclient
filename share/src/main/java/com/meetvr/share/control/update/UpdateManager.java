package com.meetvr.share.control.update;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.meetvr.share.MyApplication;
import com.meetvr.share.R;
import com.meetvr.share.info.UserInfo;
import com.meetvr.share.reponse.Response;
import com.meetvr.share.request.AppUpdateRequest;
import com.meetvr.share.request.Request;
import com.meetvr.share.utrils.Constants;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.utrils.PreferencesUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;

public class UpdateManager {

     private String curVersion;
     private String newVersion;
     private int curVersionCode;
     private int newVersionCode;
     private String updateInfo;
     private UpdateCallback callback;
     private Context ctx;
     private int progress;  
     private Boolean hasNewVersion;
     private Boolean canceled;
     public static String UPDATE_DOWNURL = "http://www.baidu.com/update/update_test.apk";
     public static final String UPDATE_SAVENAME = "updateapk.apk";
     private static final int UPDATE_CHECKCOMPLETED = 1;
     private static final int UPDATE_DOWNLOADING = 2; 
     private static final int UPDATE_DOWNLOAD_ERROR = 3; 
     private static final int UPDATE_DOWNLOAD_COMPLETED = 4; 
     private static final int UPDATE_DOWNLOAD_CANCELED = 5;
     private String savefolder ="";
     private boolean isDownloadOk = false;
    public int comeFrome = -1;
    private boolean isDownding = false;

    private boolean isShow = false;
    private NotificationManager notificationManager;
    private Notification notification;
    private String app_name="觅她VR直播";
    private RemoteViews contentView;

    /**
     * 方法描述：createNotification方法
     * @param
     * @return
     * @see
     */
    public void createNotification() {

        //notification = new Notification(R.drawable.dot_enable,app_name + getString(R.string.is_downing) ,System.currentTimeMillis());
        notification = new Notification(
                //R.drawable.video_player,//应用的图标
                R.drawable.ic_launcher,//应用的图标
                app_name + ctx.getString(R.string.is_downing),
                System.currentTimeMillis());
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        //notification.flags = Notification.FLAG_AUTO_CANCEL;

        /*** 自定义  Notification 的显示****/
        contentView = new RemoteViews(ctx.getPackageName(),R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, app_name + ctx.getString(R.string.is_downing));
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        notification.contentView = contentView;

//		updateIntent = new Intent(this, AboutActivity.class);
//		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		//updateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
//		notification.contentIntent = pendingIntent;

        notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.layout.notification_item, notification);
    }



    /**
     * 获取单个文件的MD5值！

     * @param file
     * @return
     */

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    public UpdateManager(Context context, boolean isS,UpdateCallback updateCallback) {
        ctx = context;
        callback = updateCallback;
        canceled = false;
        getCurVersion();
        this.isShow = isS;
    }
    
    public String getNewVersionName()
    {
        return newVersion;
    }
    
    public String getUpdateInfo()
    {
        return updateInfo;
    }

    private void getCurVersion() {
        try {
            PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0);
            curVersion = pInfo.versionName;
            curVersionCode = pInfo.versionCode;
        } catch (NameNotFoundException e) {
            Log.e("update", e.getMessage());
            curVersion = "1.1.1000";
            curVersionCode = 111000;
        }

    }

    public void checkUpdate() {        
        hasNewVersion = false;
       // UserInfo userInfo ;
        MyApplication application = (MyApplication)ctx.getApplicationContext();
      //  userInfo = application.getUserInfo();
        AppUpdateRequest appUpdateRequest = new AppUpdateRequest();
        appUpdateRequest.addQuery(ctx, new Request.OnNetWorkProc() {
            @Override
            public void onStart() {

            }

            @Override
            public void onRespon(JSONObject response, boolean isOK, String errMsg) {
                Response response1 = new Response();
                JSONObject dataJson = response1.parse(response);
                if(Mutils.isResponseOk(response1.getAct_stat())){
                    if(Mutils.getJsonInt(dataJson,"update_status")==1){
                        hasNewVersion = true;
                        String upFlag = Mutils.getJsonString(dataJson,"upgradetype");
                        PreferencesUtils.putSharePre(ctx, Constants.APP_UP_FLAG_KEY,upFlag);
                        UPDATE_DOWNURL = Mutils.getJsonString(dataJson,"url");
                        String version = Mutils.getJsonString(dataJson,"version");
                        PreferencesUtils.putSharePre(ctx,Constants.APP_VERSION_KEY,version);
                        PreferencesUtils.putSharePre(ctx, Constants.UPGRADEDESC_KEY,Mutils.getJsonString(dataJson,"content"));
                        String md5 = Mutils.getJsonString(dataJson,"md5");
                        savefolder = Environment.getExternalStorageDirectory() + "/" + "download";;
                        File ApkFile = new File(savefolder,UPDATE_SAVENAME);
                        String md5Str = getFileMD5(ApkFile);
                        if((md5!=null)&&(md5Str!=null)&&(md5Str.equals(md5))){ //不需要下载
                            isDownloadOk = true;
                        }
                        if(upFlag.equals("1")){
                            updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
                        }else  if(isShow){//偷偷下载 提示升级
                            updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
                        }else{
                            try {
                                String versonSave = PreferencesUtils.getSharePreStr(ctx,Constants.FIRST);
                                if(versonSave==null||versonSave.length()<=0){
                                    updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
                                }else{
                                     String[] versionValue = versonSave.split(",");
                                     if(versionValue[0].equals(version)){
                                         long firstTime = Long.valueOf(versionValue[1]);
                                         long interTime = new Date().getTime()-firstTime;
                                         if(interTime>3*1000*60*60*24){
                                             String seondValue =PreferencesUtils.getSharePreStr(ctx,Constants.SECOND);
                                             if(seondValue==null||seondValue.length()<=0){
                                                 updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
                                             }else{
                                                 String[] seoncdValues = seondValue.split(",");
                                                 long secondTime = Long.valueOf(seoncdValues[1]);
                                                 long sinterTime = new Date().getTime()-secondTime;
                                                 if(!version.equals(seoncdValues[0])){
                                                     updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
                                                 }else{
                                                     if(sinterTime>7*1000*60*60*24){
                                                         String threeValue = PreferencesUtils.getSharePreStr(ctx,Constants.THREE);
                                                         if(threeValue==null||threeValue.length()<=0){
                                                             updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
                                                         }else{
                                                             String[] threeValues = threeValue.split(",");
                                                             if(!version.equals(threeValues[0])){
                                                                 updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
                                                             }else {
                                                                 long tInterTime = new Date().getTime() - Long.valueOf(threeValues[1]);
                                                                 if(tInterTime>7*1000*60*60*24){
                                                                     updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
                                                                 }
                                                             }


                                                         }
                                                     }
                                                 }

                                             }
                                         }


                                     }else{
                                        // updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
                                     }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }else{
                        updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
                    }


                }

            }
        });
      //  updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);

        /*
        new Thread(){

            @Override
            public void run() {
                Log.i("@@@@@", ">>>>>>>>>>>>>>>>>>>>>>>>>>>getServerVerCode() ");
                try {
                	String newGetVerson = PreferencesUtils.getSharePreStr(ctx, Constants.APP_VERSION_KEY);
            		UPDATE_DOWNURL = PreferencesUtils.getSharePreStr(ctx, Constants.APP_UP_URL_KEY); //"http://192.168.141.150/b.apk";
            		newVersion  = newGetVerson;
            		String upgradeflg = PreferencesUtils.getSharePreStr(ctx, Constants.APP_UP_FLAG_KEY);
            		if(upgradeflg.equals("1")){
            			 hasNewVersion = true;
            		}
            		
                  
                } catch (Exception e) {
                    Log.e("update", e.getMessage());
                }
                updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
            };
            // ***************************************************************
        }.start();
        */

    }
    public void update() {
    	
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.fromFile(new File(savefolder, UPDATE_SAVENAME)),
                "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }
	/**
	* 判断手机是否有SD卡。
	* 
	* @return 有SD卡返回true，没有返回false。
	*/
	public static boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private FileOutputStream getFileStream(){
		try {
			FileOutputStream fos=null;
			if(hasSDCard()){
				savefolder = Environment.getExternalStorageDirectory() + "/" + "download";;
              	File file = new File(savefolder);
				if (!file.exists())
				{
					file.mkdir();
				}
               File ApkFile = new File(savefolder,UPDATE_SAVENAME);
               if(ApkFile.exists())
               {
                  ApkFile.delete();
               }
               fos = new FileOutputStream(ApkFile);
            }else {
            	savefolder = ctx.getFilesDir().getAbsolutePath();
				fos =  ctx.openFileOutput(UPDATE_SAVENAME,  
				                    Activity.MODE_WORLD_READABLE | Activity.MODE_WORLD_WRITEABLE);
			}
			return fos;
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		return null;
	}
	
    public void downloadPackage() 
    {
       if(isDownding) return;
        if(isDownloadOk){
            if(comeFrome==1){
                updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_COMPLETED);
            }
            if(notification!=null){
                notificationManager.cancel(R.layout.notification_item);
            }

            return;
        }
        new Thread() {
             @Override
                public void run() {  
                    try {
                        boolean downOK = false;
                        int prog = -1;
                        URL url = new URL(UPDATE_DOWNURL);
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.connect();  
                        int length = conn.getContentLength();  
                        InputStream is = conn.getInputStream();
                        FileOutputStream fos = getFileStream();
                        int count = 0;  
                        byte buf[] = new byte[1024];  
                        do{  
                            int numread = is.read(buf);  
                            count += numread;  
                            progress =(int)(((float)count / length) * 100);

                            if(prog!=progress){
                                updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOADING));
                            }
                            prog = progress;
                            if(numread>0){
                                fos.write(buf,0,numread);
                            }
                            if(numread <= 0){
                                if(comeFrome==1){
                                   // updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_COMPLETED);
                                    downOK = true;
                                }
                                //Log.e("9999",""+"okko"+comeFrome);
                                break;
                            }
                        }while(!canceled);
                        fos.close();
                        is.close();
                        if(downOK){
                            updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_COMPLETED);
                            Log.e("9999",""+"fasong");
                        }
                        if(canceled)
                        {
                            updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_CANCELED);
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace(); 
                        
                        updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOAD_ERROR,e.getMessage()));
                    } catch(IOException e){
                        e.printStackTrace();  
                        
                        updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOAD_ERROR,e.getMessage()));
                    }  finally {
                        isDownding = false;
                    }
                      
                } 
        }.start();
    }

    public void cancelDownload()
    {
        canceled = true;
    }
    
    Handler updateHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
            case UPDATE_CHECKCOMPLETED:
                
                callback.checkUpdateCompleted(hasNewVersion, newVersion);
                break;
            case UPDATE_DOWNLOADING:
                
                callback.downloadProgressChanged(progress);
                if(notification!=null){
                    contentView.setTextViewText(R.id.notificationPercent,progress + "%");
                    contentView.setProgressBar(R.id.notificationProgress, 100,progress, false);
                    notification.contentView = contentView;
                    notificationManager.notify(R.layout.notification_item, notification);
                }
                break;
            case UPDATE_DOWNLOAD_ERROR:
                
                callback.downloadCompleted(false, msg.obj.toString());
                if(notification!=null){
                    notification = new Notification.Builder(ctx)
                            .setAutoCancel(true)
                            .setContentTitle(app_name)
                            .setContentText(app_name + "下载出错")

                            .setSmallIcon(R.drawable.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .build();
                    notificationManager.notify(R.layout.notification_item, notification);

                }

                break;
            case UPDATE_DOWNLOAD_COMPLETED:
                Log.e("9999","777");
                if(notification!=null){
//                    Uri uri = Uri.fromFile(FileUtil.updateFile);
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(uri,"application/vnd.android.package-archive");
//                    pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);

                    //notification.flags = Notification.FLAG_AUTO_CANCEL;
                    //notification.setLatestEventInfo(ctx,app_name, getString(R.string.down_sucess), pendingIntent);
                    //notification.setLatestEventInfo(ctx,app_name, app_name + "下载完成", null);

//                    Notification.Builder builder = new Notification.Builder(ctx)
//                            .setAutoCancel(true)
//                            .setContentTitle(app_name)
//                            .setContentText(app_name + "下载完成")
//                            .setSmallIcon(R.drawable.ic_launcher)
//                            .setWhen(System.currentTimeMillis())
//                            .setOngoing(true);
//                    notification=builder.getNotification();

                     notification = new Notification.Builder(ctx)
                            .setAutoCancel(true)
                            .setContentTitle(app_name)
                            .setContentText(app_name + "下载完成")

                            .setSmallIcon(R.drawable.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .build();
                    notificationManager.notify(R.layout.notification_item, notification);
                }
                callback.downloadCompleted(true, "");
                break;
            case UPDATE_DOWNLOAD_CANCELED:
                
                callback.downloadCanceled();
                if(notification!=null){
                    notification = new Notification.Builder(ctx)
                            .setAutoCancel(true)
                            .setContentTitle(app_name)
                            .setContentText(app_name + "下载取消")

                            .setSmallIcon(R.drawable.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .build();
                    notificationManager.notify(R.layout.notification_item, notification);

                }

                default:
                break;
            }
        }
    };

    public interface UpdateCallback {
        public void checkUpdateCompleted(Boolean hasUpdate,
                                         CharSequence updateInfo);
        public void downloadProgressChanged(int progress);
        public void downloadCanceled();
        public void downloadCompleted(Boolean sucess, CharSequence errorMsg);
    }

}