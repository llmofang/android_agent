package com.llmofang.android.agent;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
        OkHttpClient client=new OkHttpClient();
        Request request = new Request.Builder().url(LLMoFang.HEALTHCHECK)
                .addHeader("Accept-Version", LLMoFang.INTERFACE_VERSION).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.code()==200||response.code()==404)
            {
                new InitializeService(LLMoFang.getAppid(),LLMoFang.getAppkey()).run();
            }
        } catch (IOException e) {
            LLMoFang.scheduledThreadPoolExecutor.schedule(new ControlCenterRetryTask(),LLMoFang.controlCenterRetrySchedule, TimeUnit.SECONDS);
        }

    }
}
