package com.meetvr.share.view;






import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.meetvr.share.R;


public class ProgessDlg {
	// 弹出Popwindow
	private Context context;
	private PopupWindow popupWindow;
	private View mView;
	public   ProgessDlg(Context context, View view){
		try {
			this.context= context;
			this.mView = view; 
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	public void dismiss(){
		try {
			popupWindow.dismiss();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void showPopupWindow() {
		try {
			Context mContext = context;
			LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View view = mLayoutInflater.inflate(R.layout.popwindow, null);
			popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
			popupWindow.setAnimationStyle(android.R.style.Animation);
			popupWindow.update();
			popupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
