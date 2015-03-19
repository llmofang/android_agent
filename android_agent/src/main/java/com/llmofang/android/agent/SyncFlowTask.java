package com.llmofang.android.agent;

import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by xu on 2015/2/5.
 */
public class SyncFlowTask implements Runnable {
    @Override
    public void run() {
//        OkHttpClient client=new OkHttpClient();
//        Request request = new Request.Builder().url(LLMoFang.CONTROLCENTERSERVER_SYNCFLOW + LLMoFang.requestToken)
//              .addHeader("Accept-Version", LLMoFang.INTERFACE_VERSION).build();
        HttpGet get=new HttpGet(LLMoFang.CONTROLCENTERSERVER_SYNCFLOW + LLMoFang.requestToken);

        try {
            HttpResponse response =HttpUtil.request(get);
            if(response.getStatusLine().getStatusCode()==200)
            {
                String response_body = EntityUtils.toString(response.getEntity());
                JSONObject result = new JSONObject(response_body);
                LLMoFang.flow=result.getLong("available_flow");
            }else {
                String response_body = EntityUtils.toString(response.getEntity());
                JSONObject result = new JSONObject(response_body);
                int code=result.getInt("code");
                //requestToken过期
                if(code==107)
                {
                    LLMoFang.initializeService.acquireInitData();
                    this.run();
                }
                Log.i("[llmofang]", response_body);
            }
        } catch (IOException e) {
            LLMoFang.scheduledThreadPoolExecutor.schedule(new ControlCenterRetryTask(),LLMoFang.controlCenterRetrySchedule, TimeUnit.SECONDS);
        } catch (JSONException e) {
            LLMoFangUtil.showToast("流量魔方出错，已暂停服务，请稍后重启app重试");
            LLMoFangProxyService.whetherSetProxy=false;
        }

    }
}
