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
public class ClaimRewardRequest extends Request {

    public ClaimRewardRequest(String token, String identifier) {
        super(Constants.Get_API_HTTP(), Constants.Get_API_VERSION(), Request.servicename.activity, "mb");
        addNetUrl("?token="+token+"&identifier="+identifier);

    }

}
