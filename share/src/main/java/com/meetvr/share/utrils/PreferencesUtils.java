package com.meetvr.share.utrils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.Toast;

public class PreferencesUtils {
	/**
	 * 
	 * @param mContext 上下文，来区别哪一个activity调用的
	 * @param whichSp 使用的SharedPreferences的名字
	 * @param field SharedPreferences的哪一个字段
	 * @return
	 */
	private static String FILE_S="prefer_file";
	//取出whichSp中field字段对应的string类型的值
	public static String getSharePreStr(Context mContext, String field){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE_S, 0);
		String s=sp.getString(field,"");//如果该字段没对应值，则取出字符串0
		return s;
	}
	//取出whichSp中field字段对应的int类型的值
	public static int getSharePreInt(Context mContext, String field){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE_S, 0);
		int i=sp.getInt(field,0);//如果该字段没对应值，则取出0
		return i;
	}

	//取出whichSp中field字段对应的int类型的值
	public static int getSharePreInt(Context mContext, String field,int defaultValue){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE_S, 0);
		int i=sp.getInt(field,defaultValue);//如果该字段没对应值，则取出0
		return i;
	}
	
	//取出whichSp中field字段对应的boolean类型的值
	public static boolean getSharePreBoolean(Context mContext, String field){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE_S, 0);
		boolean i=sp.getBoolean(field, false);//如果该字段没对应值，则取出0
		return i;
	}

	//保存string类型的value到whichSp中的field字段
	public static void putSharePre(Context mContext, String field, String value){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE_S, 0);
		sp.edit().putString(field, value).commit();
	}
	//保存int类型的value到whichSp中的field字段
	public static void putSharePre(Context mContext, String field, int value){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE_S, 0);
		sp.edit().putInt(field, value).commit();
	}
	
	//保存boolean类型的value到whichSp中的field字段
	public static void putSharePre(Context mContext, String field, Boolean value){
		SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE_S, 0);
		sp.edit().putBoolean(field, value).commit();
	}
	//保存boolean类型的value到whichSp中的field字段
		public static void clearSharePre(Context mContext){
			try {
				SharedPreferences sp=(SharedPreferences) mContext.getSharedPreferences(FILE_S, 0);
				//sp.edit().clear();
				//sp.edit().commit();
				//dataBase.edit().clear().commit();
				sp.edit().clear().commit();
			} catch (Exception e) {
				// TODO: handle exception
			}
		
		}
	
	/**
	 * Toast的封装
	 * @param mContext 上下文，来区别哪一个activity调用的
	 * @param msg 你希望显示的值。
	 */
	public static void showMsg(Context mContext, String msg) {
		Toast toast=new Toast(mContext);
		toast= Toast.makeText(mContext,msg, 300);
		toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);//设置居中
		toast.show();//显示,(缺了这句不显示)
	}
}
