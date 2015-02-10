package com.llmofang.android.agent;

import android.util.Base64;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
         LLMoFang.scheduledThreadPoolExecutor.scheduleAtFixedRate(new SyncFlowTask(),LLMoFang.syncFlowSchedule,LLMoFang.syncFlowSchedule, TimeUnit.SECONDS);

    }

    public  void acquireAppToken()  {

        Request request = new Request.Builder().url(LLMoFang.CONTROLCENTERSERVER_APPTOKENURL)
                .addHeader("Accept-Version", LLMoFang.INTERFACE_VERSION)
                .addHeader("X-llmf-Application-Id", appId)
                .addHeader("X-llmf-REST-API-Key",appKey).build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if(response.code()==200)
            {
                String response_body = response.body().string();
                JSONObject result = new JSONObject(response_body);
                LLMoFang.apptoken=result.get("app_token").toString();
                LLMoFang.apptokenExpire=LLMoFangUtil.getExpireTime(result.get("expire").toString());
                Log.i("[llmofang]",response_body );

            }
        } catch (IOException e) {
            Log.i(LLMoFang.TAG,"acquireAppToken error network error"+e.getMessage());
            LLMoFang.scheduledThreadPoolExecutor.schedule(new ControlCenterRetryTask(),LLMoFang.controlCenterRetrySchedule,TimeUnit.SECONDS);
            e.printStackTrace();
        } catch (JSONException e) {
            Log.i(LLMoFang.TAG,"acquireAppToken error json error"+e.getMessage());
            e.printStackTrace();
        }

    }

    public  void acquireInitData()  {

        Response response = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone_number", LLMoFang.getPhoneNumber())
                    .put("phone_imei", LLMoFang.getImei())
                    .put("phone_imsi", LLMoFang.getImsi())
                    .put("system", "Android")
                    .put("sdk_version", LLMoFang.SDK_VERSION);
            String userInfoBase64 = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.DEFAULT);
            userInfoBase64=userInfoBase64.replaceAll("\\\n","");
            Request request = new Request.Builder().url(LLMoFang.CONTROLCENTERSERVER_INITURL + LLMoFang.apptoken)
                    .addHeader("Accept-Version", LLMoFang.INTERFACE_VERSION)
                    .addHeader("X-llmf-User-Info", userInfoBase64).build();
            response = client.newCall(request).execute();
            if (response.code() == 200) {
                String response_body = response.body().string();
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

                JSONObject connectPolicy=result.getJSONObject("network_policy");
                LLMoFang.connectCellular=connectPolicy.getBoolean("cellular");
                LLMoFang.connectWifi=connectPolicy.getBoolean("wifi");
                LLMoFang.isllmofangInitialized=true;
                Log.i("[llmofang]", response_body);

            }else {
                String response_body = response.body().string();
                JSONObject result = new JSONObject(response_body);
                int code=result.getInt("code");
                //appToken过期
                if(code==106)
                {
                    acquireAppToken();
                    acquireInitData();
                }
                Log.i("[llmofang]", response_body);
            }
        } catch (IOException e) {
            Log.i(LLMoFang.TAG,"acquireInitData error network error"+e.getMessage());
            LLMoFang.scheduledThreadPoolExecutor.schedule(new ControlCenterRetryTask(),LLMoFang.controlCenterRetrySchedule,TimeUnit.SECONDS);
            e.printStackTrace();
        } catch (JSONException e) {
            Log.i(LLMoFang.TAG,"acquireInitData error json error"+e.getMessage());
            e.printStackTrace();
        }

    }
}
