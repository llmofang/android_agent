package com.llmofang.android.agent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRouteParams;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * Created by xu on 2015/2/4.
 */
public class LLMoFangProxyService {
    public static  Boolean whetherSetProxy=true;
    public static Proxy getProxy(ProxyURL proxyUrl)
    {
        if(proxyUrl != null)
        {
           return  new Proxy(Proxy.Type.HTTP,new InetSocketAddress(proxyUrl.getAddress(),proxyUrl.getPort()));
        }else{
            return null;
        }
    }

    public static boolean whetherSetProxy()
    {

        if(!LLMoFang.isControlCenterOK&&LLMoFangUtil.isExpire(LLMoFang.requestTokenExpire))
        {
            LLMoFang.initializeService.acquireInitData();
        }

        if(LLMoFang.errorRetry<=0)
        {
            LLMoFang.scheduledThreadPoolExecutor.schedule(new ProxyRetryTask(),LLMoFang.errorInterval, TimeUnit.SECONDS);
           // LLMoFang.scheduledThreadPoolExecutor.schedule(new ProxyRetryTask(),10, TimeUnit.SECONDS);
            Log.d(LLMoFang.TAG,"proxy_error_retry_task running.............");

                return false;

        }
        if (LLMoFang.connectivityAction!=-1)
        {
            if(LLMoFang.connectivityAction== ConnectivityManager.TYPE_WIFI)
            {
                if(LLMoFang.connectWifi==false)
                {
                   return false;
                }
            }
            if(LLMoFang.connectivityAction== ConnectivityManager.TYPE_MOBILE)
            {
                if(LLMoFang.connectCellular ==false)
                {
                    return false;
                }
            }
        }else {
            return false;
        }
        if(LLMoFang.connectivityAction==-1)
        {
            return false;
        }
        if(!LLMoFang.isllmofangInitialized)
        {
            return false;
        }

        if(LLMoFang.flow<=0)
        {
            return false;
        }

        if(!LLMoFang.isProxySeverOK)
        {
            return false;
        }
        return whetherSetProxy;
    }

    public static void httpclientSetProxy(HttpClient httpClient)
    {
        HttpHost proxy = new HttpHost(LLMoFang.httpProxyUrl.getAddress(), LLMoFang.httpProxyUrl.getPort());
        httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
    }

}
