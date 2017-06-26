package com.meetvr.share.control.update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;


import com.meetvr.share.R;
import com.meetvr.share.utrils.Constants;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.utrils.NetworkUtils;
import com.meetvr.share.utrils.PreferencesUtils;

import java.util.Date;


public class AppUpDate {

	Context mContext;
	boolean mIsShow = false;

	public AppUpDate(Context c, boolean isShow) {
		mContext = c;
		mIsShow = isShow;
	}

	public void checkUpDate() {
		updateMan = new UpdateManager(mContext, mIsShow,appUpdateCb);
		updateMan.checkUpdate();
	}

	// 自动更新回调函数
	private UpdateManager updateMan;
	private ProgressDialog updateProgressDialog;
	UpdateManager.UpdateCallback appUpdateCb = new UpdateManager.UpdateCallback() {

		public void downloadProgressChanged(int progress) {
			if (updateProgressDialog != null && updateProgressDialog.isShowing()) {
				updateProgressDialog.setProgress(progress);
			}

		}

		public void downloadCompleted(Boolean sucess, CharSequence errorMsg) {
			if (updateProgressDialog != null && updateProgressDialog.isShowing()) {
				updateProgressDialog.dismiss();
			}

			if (sucess) {
				PreferencesUtils.putSharePre(mContext,  Constants.APP_UP_FLAG_KEY, "0");
				updateMan.update();
				System.exit(0);
			} else {
				DialogHelper.Confirm(mContext, R.string.dialog_error_title, R.string.dialog_downfailed_msg, R.string.dialog_downfailed_btn, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						updateMan.downloadPackage();

					}
				}, R.string.dialog_downfailed_btnnext, null);
			}
		}

		public void downloadCanceled() {
		}



		public void checkUpdateCompleted(Boolean hasUpdate, CharSequence updateInfo) {
			if (hasUpdate) {
				String updateMsg="版本号：" + PreferencesUtils.getSharePreStr(mContext, Constants.APP_VERSION_KEY)+"\n" +"更新内容：\n";
				final String upgradetype = PreferencesUtils.getSharePreStr(mContext, Constants.APP_UP_FLAG_KEY);
				boolean isFource = false;
				if (upgradetype.equals("1")) {

					//updateMsg = updateMsg+ mContext.getText(R.string.dialog_force_update_msg).toString();
					updateMsg = updateMsg+PreferencesUtils.getSharePreStr(mContext, Constants.UPGRADEDESC_KEY);
					isFource = true;
				} else {
					updateMsg =updateMsg+  PreferencesUtils.getSharePreStr(mContext, Constants.UPGRADEDESC_KEY);
					if (updateMsg == null || updateMsg.length() <= 0) {
						updateMsg = mContext.getText(R.string.dialog_update_msg).toString();
					}
				}
				DialogHelper.Confirm(isFource,mContext, mContext.getText(R.string.dialog_update_title), updateMsg, mContext.getText(R.string.dialog_update_btnupdate), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {// 确定事件
						downloadAPK();
						//Mutils.pointEnter(mContext,"0","vrlive_upgrade","click" ,"1");
					}
				}, mContext.getText(R.string.dialog_update_btnnext), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(upgradetype.equals("2")){//开始wifi下载

							if(NetworkUtils.isWifi(mContext)){
								updateMan.comeFrome =0;
								updateMan.downloadPackage();
							}

						}
						//Mutils.pointEnter(mContext,"0","vrlive_upgrade","click" ,"2");
						if (upgradetype.equals("1")) {// 强制升级的取消
							((Activity)mContext).finish(); // 退出应用
						}else {
							String firtValue = PreferencesUtils.getSharePreStr(mContext,Constants.FIRST);
							String[]firtValues = firtValue.split(",");
							String versonValue = PreferencesUtils.getSharePreStr(mContext, Constants.APP_VERSION_KEY);
							long timeNow= new Date().getTime();
							if(firtValue==null||firtValue.length()<0||!versonValue.equals(firtValues[0])){
								PreferencesUtils.putSharePre(mContext,Constants.FIRST,versonValue+","+timeNow);
							}else{
								String secondValue = PreferencesUtils.getSharePreStr(mContext,Constants.SECOND);
								String[]seondValues = secondValue.split(",");
								if(secondValue==null||secondValue.length()<0||!versonValue.equals(seondValues[0])){
									PreferencesUtils.putSharePre(mContext,Constants.SECOND,versonValue+","+timeNow);
								}else{
									PreferencesUtils.putSharePre(mContext,Constants.THREE,versonValue+","+timeNow);
								}
							}
						}
					}
				});
			} else {
				if (mIsShow) {
					Toast.makeText(mContext, R.string.zjszxbb, Toast.LENGTH_SHORT).show();
				}
			}

		}

		/**
		 * 下载apk
		 */
		private void downloadAPK() {
			updateProgressDialog = new ProgressDialog(mContext);
			updateProgressDialog.setMessage(mContext.getText(R.string.dialog_downloading_msg));
			updateProgressDialog.setIndeterminate(false);
			updateProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			updateProgressDialog.setMax(100);
			updateProgressDialog.setProgress(0);
			updateProgressDialog.setCanceledOnTouchOutside(false);
			updateProgressDialog.setCancelable(false);
			if(PreferencesUtils.getSharePreStr(mContext, Constants.APP_UP_FLAG_KEY).equals("1")){
				updateProgressDialog.show();
			}else{
				updateMan.createNotification();
			}
			updateMan.comeFrome =1;//来自确定
			updateMan.downloadPackage();
		}

	};

}
