package com.meetvr.share.utrils;

/**
 * Created by wzm-pc on 2016/8/20.
 */
public class Constants {
    public static int START_FLASH_DELAY=100; //启动页停留时间

    public static final String APP_VER="1.3.2"; //APP的版本号
	public static final String CHANNEL="0"; //0默认，1应用宝，2 360，3小米商店，4百度手机助手
    public  static  final String DB_NAME="vr_db";//数据库名称

    public static final String APP_VERSION_KEY="APP_VERSION_KEY";//升级版本号
    public static final String APP_UP_FLAG_KEY="APP_UP_FLAG_KEY"; //升级标志
    public static final String UPGRADEDESC_KEY="UPGRADEDESC_KEY"; //升级描述
    public static final String FIRST="first";
    public static final String SECOND="seond";
    public static final String THREE="three";
	
	public static Boolean TestMode = false;

    public static final String TEST_USER_HTTP="http://test.user.api.moxiangtv.com:8080"; //用户中心地址
    public static final String TEST_LIVEW_HTTP="http://test.bussiness.api.moxiangtv.com:8080";//业务平台地址
    public static final String TEST_BILL_HTTP="http://test.billing.api.moxiangtv.com:8080";//帐务系统地址
    public static final String TEST_IM_HTTP="http://test.im.api.moxiangtv.com"; //消息中心地址
    public static final String TEST_API_HTTP="http://test.api.moxiangtv.com"; //消息中心地址


    public static final String REAL_USER_HTTP="http://user.api.moxiangtv.com"; //用户中心地址
    public static final String REAL_LIVEW_HTTP="http://bussiness.api.moxiangtv.com";//业务平台地址
    public static final String REAL_BILL_HTTP="http://billing.api.moxiangtv.com";//帐务系统地址
    public static final String REAL_IM_HTTP="http://im.api.moxiangtv.com"; //消息中心地址
    public static final String REAL_API_HTTP="http://api.moxiangtv.com"; //消息中心地址


	public static String Get_USER_HTTP()
	{
		if(TestMode)
			return TEST_API_HTTP;
		return REAL_API_HTTP;
	}

	public static String Get_USER_API_VERSION()
	{
		return "v2";
	}

	public static String Get_LIVEW_HTTP()
	{
		if(TestMode)
			return TEST_API_HTTP;
		return REAL_API_HTTP;
	}

	public static String Get_LIVE_API_VERSION()
	{
		return "v2";
	}

	public static String Get_BILL_HTTP()
	{
		if(TestMode)
			return TEST_API_HTTP;
		return REAL_API_HTTP;
	}

	public static String Get_BILL_API_VERSION()
	{
		return "v2";
	}

	public static String Get_IM_HTTP()
	{
		if(TestMode)
			return TEST_API_HTTP;
		return REAL_API_HTTP;
	}

	public static String Get_IM_API_VERSION()
	{
		return "v2";
	}

	public static String Get_API_HTTP()
	{
		if(TestMode)
			return TEST_API_HTTP;
		return REAL_API_HTTP;
	}

	public static String Get_API_VERSION()
	{
		return "v2";
	}


    public static final String APP_ID ="wxc128abaecbcfcf45"; //微信的ID

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016072300104161";
}
