package com.llmofang.android.agent;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by xu on 2015/2/5.
 */
public class ControlCenterRetryTask implements Runnable {
    @Override
    public void run() {
//        OkHttpClient client=new OkHttpClient();
//        Request request = new Request.Builder().url(LLMoFang.HEALTHCHECK)
//                .addHeader("Accept-Version", LLMoFang.INTERFACE_VERSION).build();
        HttpGet get=new HttpGet(LLMoFang.HEALTHCHECK);
        try {
            HttpResponse response = HttpUtil.request(get);
            //如果是200则控制中心正常
            if(response.getStatusLine().getStatusCode() ==200||response.getStatusLine().getStatusCode() ==404)
            {
                new InitializeService(LLMoFang.getAppid(),LLMoFang.getAppkey()).run();
            }else{
                LLMoFang.scheduledThreadPoolExecutor.schedule(new ControlCenterRetryTask(),LLMoFang.controlCenterRetrySchedule, TimeUnit.SECONDS);
            }
        } catch (IOException e) {
            LLMoFang.scheduledThreadPoolExecutor.schedule(new ControlCenterRetryTask(),LLMoFang.controlCenterRetrySchedule, TimeUnit.SECONDS);
        }

    }
}
