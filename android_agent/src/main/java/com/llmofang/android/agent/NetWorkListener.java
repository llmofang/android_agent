package com.llmofang.android.agent;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by xu on 2015/2/5.
 */
public class NetWorkListener extends Service {
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    public static  final int NONETWORK=-1;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d(LLMoFang.TAG, "网络状态已经改变");
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if(info != null && info.isAvailable()) {
                    LLMoFang.connectivityAction=info.getType();
                    String name = info.getTypeName();
                    Log.d(LLMoFang.TAG, "当前网络名称：" + name);
                } else {
                    LLMoFang.connectivityAction=NONETWORK;
                    Log.d(LLMoFang.TAG, "没有可用网络");
                }
            }
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

}
