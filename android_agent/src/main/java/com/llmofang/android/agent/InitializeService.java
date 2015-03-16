package com.llmofang.android.agent;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by xu on 2015/2/3.
 */
public class InitializeService implements Runnable{
    private String appId;
    private String appKey;
    private  OkHttpClient client=new OkHttpClient();
    public InitializeService(String appid,String appkey) {
        appId=appid;
        appKey=appkey;
    }


    @Override
    public void run() {

            acquireAppToken();
            acquireInitData();
           // LLMoFang.scheduledThreadPoolExecutor.scheduleAtFixedRate(new SyncFlowTask(),LLMoFang.syncFlowSchedule,LLMoFang.syncFlowSchedule, TimeUnit.SECONDS);
           new SyncFlowTask().run();
            LLMoFangUtil.showToast("流量魔方初始化成功,可用流量为"+LLMoFang.flow+"b");
    }

    public  void acquireAppToken()  {

//        Request request = new Request.Builder().url(LLMoFang.CONTROLCENTERSERVER_APPTOKENURL)
//                .addHeader("Accept-Version", LLMoFang.INTERFACE_VERSION)
//                .addHeader("X-llmf-Application-Id", appId)
//                .addHeader("X-llmf-REST-API-Key",appKey).build();
        HttpGet get=new HttpGet(LLMoFang.CONTROLCENTERSERVER_APPTOKENURL);
        get.setHeader("X-llmf-Application-Id", appId);
        get.setHeader("X-llmf-REST-API-Key",appKey);

        HttpResponse response = null;
        try {
            client.setConnectTimeout(10,TimeUnit.SECONDS);
            response = HttpUtil.request(get);
            if(response.getStatusLine().getStatusCode()==200)
            {
                String response_body = EntityUtils.toString(response.getEntity());
                JSONObject result = new JSONObject(response_body);
                LLMoFang.apptoken=result.get("app_token").toString();
                LLMoFang.apptokenExpire=LLMoFangUtil.getExpireTime(result.get("expire").toString());
                Log.i("[llmofang]",response_body );

            }else{
                LLMoFang.scheduledThreadPoolExecutor.schedule(new ControlCenterRetryTask(),LLMoFang.controlCenterRetrySchedule,TimeUnit.SECONDS);
            }
        } catch (IOException e) {
            Log.i(LLMoFang.TAG,"acquireAppToken error network error"+e.getMessage());
            LLMoFangUtil.showToast("流量魔方初始化失败");
            LLMoFang.scheduledThreadPoolExecutor.schedule(new ControlCenterRetryTask(),LLMoFang.controlCenterRetrySchedule,TimeUnit.SECONDS);
          //  e.printStackTrace();
        } catch (JSONException e) {
            Log.i(LLMoFang.TAG,"acquireAppToken error json error"+e.getMessage());
            LLMoFangUtil.showToast("数据解析错误，流量魔方初始化失败");
           // e.printStackTrace();
        }

    }

    public  void acquireInitData()  {

        HttpResponse response = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone_number", LLMoFang.getPhoneNumber())
                    .put("phone_imei", LLMoFang.getImei())
                    .put("phone_imsi", LLMoFang.getImsi())
                    .put("system", "Android")
                    .put("sdk_version", LLMoFang.SDK_VERSION);
            String userInfoBase64 = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.DEFAULT);
            userInfoBase64=userInfoBase64.replaceAll("\\\n","");
//            Request request = new Request.Builder().url(LLMoFang.CONTROLCENTERSERVER_INITURL + LLMoFang.apptoken)
//                    .addHeader("Accept-Version", LLMoFang.INTERFACE_VERSION)
//                    .addHeader("X-llmf-User-Info", userInfoBase64).build();
//            response = client.newCall(request).execute();
            HttpGet get=new HttpGet(LLMoFang.CONTROLCENTERSERVER_INITURL + LLMoFang.apptoken);
            get.setHeader("X-llmf-User-Info", userInfoBase64);
            if (response.getStatusLine().getStatusCode() == 200) {
                String response_body =EntityUtils.toString(response.getEntity());
                JSONObject result = new JSONObject(response_body);
                LLMoFang.requestToken=result.getString("request_token");
                LLMoFang.requestTokenExpire=LLMoFangUtil.getExpireTime(result.getInt("expire"));

                JSONObject servers=result.getJSONObject("servers");
                LLMoFang.httpProxyUrl=new ProxyURL(servers.getString("http"));
                LLMoFang.httpsProxyUrl=new ProxyURL(servers.getString("https"));
                LLMoFang.spdyProxyUrl=new ProxyURL(servers.getString("spdy"));

                JSONObject errorPolicy=result.getJSONObject("error_policy");
                LLMoFang.errorRetry=errorPolicy.getInt("retry");
                LLMoFang.errorInterval=errorPolicy.getInt("interval");


                JSONObject networkPolicy=result.getJSONObject("network_policy");
                LLMoFang.connectCellular=networkPolicy.getBoolean("cellular");
                LLMoFang.connectWifi=networkPolicy.getBoolean("wifi");

                JSONObject connectPolicy=result.getJSONObject("connect_policy");
                LLMoFang.syncFlowSchedule=connectPolicy.getInt("update");
                LLMoFang.isllmofangInitialized=true;
                Log.i("[llmofang]", response_body);

            }else {
                String response_body = EntityUtils.toString(response.getEntity());
                JSONObject result = new JSONObject(response_body);
                int code=result.getInt("code");
                //appToken过期
                if(code==106)
                {
                    acquireAppToken();
                    acquireInitData();
                    new SyncFlowTask().run();
                }
                Log.i("[llmofang]", response_body);
            }
        } catch (IOException e) {
            Log.i(LLMoFang.TAG,"acquireInitData error network error"+e.getMessage());
            LLMoFang.scheduledThreadPoolExecutor.schedule(new ControlCenterRetryTask(),LLMoFang.controlCenterRetrySchedule,TimeUnit.SECONDS);
            LLMoFangUtil.showToast("流量魔方初始化失败");
        } catch (JSONException e) {
            Log.i(LLMoFang.TAG,"acquireInitData error json error"+e.getMessage());
            LLMoFangUtil.showToast("数据解析错误，流量魔方初始化失败");
        }

    }
}
