package com.meetvr.share.request;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.DisplayMetrics;

import com.meetvr.share.utrils.Constants;
import com.meetvr.share.utrils.Mutils;
import com.meetvr.share.utrils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Created by wzm-pc on 2016/8/24.
 */
public class PointRequest extends Request {

    private JSONArray jsonArray = new JSONArray();
    public PointRequest(String usercode ,Context context) {
        super(Constants.Get_USER_HTTP(), Constants.Get_USER_API_VERSION(), Request.servicename.point, "save_point");


            addJsonObj("usercode", usercode);
            setParam(context);
            addJsonObj("points", jsonArray);

    }

    public void addArray(String point_name ,String point_param,String point_value){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("point_name", point_name);
            jsonObject.put("point_param", point_param);
            jsonObject.put("point_value", point_value);
            jsonArray.put(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setParam(Context context){
        addJsonObj("productid","1");

        String ip="";
        if(NetworkUtils.isWifi(context)){
            ip = getWifiIP(context);
        }else if(NetworkUtils.isNetAvailable(context)){
            ip = getLocalIpAddress();
        }
        addJsonObj("ip",ip);
        addJsonObj("xaid", Mutils.getDeviceId());
        addJsonObj("cn","0");
        addJsonObj("apilevel", Build.VERSION.SDK_INT);
        addJsonObj("ver",Constants.APP_VER);
        addJsonObj("model",Mutils.getInfo());
        addJsonObj("root",isRoot());
        addJsonObj("nettype",NetworkUtils.getNetType(context)[0]);
        addJsonObj("state",0);
        addJsonObj("vga",getSize(context));
        addJsonObj("screen",getScreenSizeOfDevice2(context));
        addJsonObj("cores",getNumCores());
        addJsonObj("mem",getmem_TOLAL());

    }


        // 获得可用的内存
        public  long getmem_UNUSED(Context mContext) {
            long MEM_UNUSED;
            // 得到ActivityManager
            ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            // 创建ActivityManager.MemoryInfo对象

            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);

            // 取得剩余的内存空间

            MEM_UNUSED = mi.availMem / 1024;
            return MEM_UNUSED;
        }

        // 获得总内存
        public  long getmem_TOLAL() {
            long mTotal;
            // /proc/meminfo读出的内核信息进行解释
            String path = "/proc/meminfo";
            String content = null;
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(path), 8);
                String line;
                if ((line = br.readLine()) != null) {
                    content = line;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // beginIndex
            int begin = content.indexOf(':');
            // endIndex
            int end = content.indexOf('k');
            // 截取字符串信息

            content = content.substring(begin + 1, end).trim();
            mTotal = Integer.parseInt(content);
            return mTotal;
        }


    private int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if(Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Log.d(TAG, "CPU Count: "+files.length);
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Print exception
           // Log.d(TAG, "CPU Count: Failed.");
            e.printStackTrace();
            //Default to return 1 core
            return 1;
        }
    }

    public String getSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return screenWidth +"X" +screenHeight;
    }

    private String getScreenSizeOfDevice2(Context context) {
        Point point = new Point();
        ((Activity)context).getWindowManager().getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        double x = Math.pow(point.x/ dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        //Log.d(TAG, "Screen inches : " + screenInches);
        return String.valueOf(screenInches);
    }
    public String getScreenSizeOfDevice(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        double x = Math.pow(screenWidth,2);
        double y = Math.pow(screenHeight,2);
        double diagonal = Math.sqrt(x+y);
        int dens=dm.densityDpi;
        double screenInches = diagonal/(double)dens;

        return "" + screenInches;

    }

    /**
     * 判断当前手机是否有ROOT权限
     * @return
     */
    public boolean isRoot(){
        boolean bool = false;

        try{
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())){
                bool = false;
            } else {
                bool = true;
            }
            //Log.d(TAG, "bool = " + bool);
        } catch (Exception e) {

        }
        return bool;
    }

    private String getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex)
        {

        }
        return null;
    }

    public String  getWifiIP(Context context) {


        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
          // wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        //EditText et = (EditText)findViewById(R.id.EditText01);
        //et.setText(ip);
        return ip;
    }
    private String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }






}
