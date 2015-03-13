package com.llmofang.android.agent;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
        OkHttpClient client=new OkHttpClient();
        Request request = new Request.Builder().url(LLMoFang.CONTROLCENTERSERVER_SYNCFLOW + LLMoFang.requestToken)
              .addHeader("Accept-Version", LLMoFang.INTERFACE_VERSION).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.code()==200)
            {
                String response_body = response.body().string();
                JSONObject result = new JSONObject(response_body);
                LLMoFang.flow=result.getLong("flow");
            }else {
                String response_body = response.body().string();
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
