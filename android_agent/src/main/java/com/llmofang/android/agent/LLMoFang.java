package com.llmofang.android.agent;



import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class LLMoFang {
    private static String phoneNumber;
    private static String imei;
    private static String imsi;
    private static String appid;
    private static String appkey;
    public static String apptoken;
    public static int connectivityAction=-1;
    public static long apptokenExpire;
    public static Context applicationContext;
    public static String requestToken;
    public static long requestTokenExpire;
    public static ProxyURL httpProxyUrl;
    public static ProxyURL httpsProxyUrl;
    public static ProxyURL spdyProxyUrl;
    public static int errorRetry;
    public static int errorInterval;
    public static boolean connectCellular;
    public static boolean connectWifi;
    public static int llmofangStatus;
    public static long flow;
    public static long syncFlowSchedule=3000;
    public static int controlCenterRetrySchedule=600;
    public static boolean isllmofangInitialized=false;
    public static final String INTERFACE_VERSION="1.0.0";
    public static final String SDK_VERSION="1.0.0";
    public static final String TAG="[llmofang]";
    public static boolean isProxySeverOK=true;
    public static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor=new ScheduledThreadPoolExecutor(1);
    public static final String CONTROLCENTERSERVER="http://10.1.1.100:4399";
    public static final String CONTROLCENTERSERVER_APPTOKENURL=CONTROLCENTERSERVER+"/app/token";
    public static final String CONTROLCENTERSERVER_INITURL=CONTROLCENTERSERVER+"/app/runtime/";
    public static final String CONTROLCENTERSERVER_SYNCFLOW=CONTROLCENTERSERVER+"/flow/available";
    public static final String HEALTHCHECK=CONTROLCENTERSERVER+"/server/healthcheck/";
    public static  InitializeService initializeService;


    private LLMoFang() {
    }




    public static void initialize(Context context,String appID,String appKey)  {

        TelephonyManager tm = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
        phoneNumber = tm.getLine1Number();
        imei = tm.getSimSerialNumber();
        applicationContext=context;
        imsi = tm.getSubscriberId();
        appid=appID;
        appkey=appKey;
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if(info!=null) {
            connectivityAction = info.getType();
        }
        Log.i(TAG,"phoneNumber:"+phoneNumber+"||imie:"+imei+"||imsi:"+imsi);
        if(imsi != null || imsi.length() > 0) {
             initializeService = new InitializeService(appid, appkey);
            new Thread(initializeService).start();
        }else{
            LLMoFangUtil.showToast("无法取得用户信息初始化失败,流量魔方启动失败");
        }

    }

    public Proxy getProxy()
    {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.100", 8080));
        return  proxy;
    }

    public static String getImei() {
        return imei;
    }

    public static String getImsi() {
        return imsi;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static String getAppid() {
        return appid;
    }

    public static String getAppkey() {
        return appkey;
    }

    public static String getVersion()
    {
        return SDK_VERSION;
    }
}
