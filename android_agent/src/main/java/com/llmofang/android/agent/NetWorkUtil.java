package com.llmofang.android.agent;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by xu on 2015/3/18.
 */
public class NetWorkUtil {
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    private static Context context=LLMoFang.applicationContext;
    public static int getNetworkType() {
        int netType = 0;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if (networkInfo == null) {
//            return netType;
//        }
//        int nType = networkInfo.getType();
//        if (nType == ConnectivityManager.TYPE_MOBILE) {
//            String extraInfo = networkInfo.getExtraInfo();
//            if(extraInfo!=null&&extraInfo.equals("")){
//                if (extraInfo.toLowerCase().equals("cmnet")) {
//                    netType = NETTYPE_CMNET;
//                } else {
//                    netType = NETTYPE_CMWAP;
//                }
//            }
//        } else if (nType == ConnectivityManager.TYPE_WIFI) {
//            netType = NETTYPE_WIFI;
//        }
//        return netType;
        //如果没有网络连接返回-1
        if (networkInfo==null)
        {
            return -1;
        }
        return networkInfo.getType();
    }

    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}
