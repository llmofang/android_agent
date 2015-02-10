package com.llmofang.android.agent;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by xu on 2015/2/5.
 */
public class SyncFlowTask implements Runnable {
    @Override
    public void run() {
        OkHttpClient client=new OkHttpClient();
        Request request = new Request.Builder().url(LLMoFang.CONTROLCENTERSERVER_SYNCFLOW + LLMoFang.apptoken)
              .addHeader("Accept-Version", LLMoFang.INTERFACE_VERSION).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.code()==200)
            {
                String response_body = response.body().string();
                JSONObject result = new JSONObject(response_body);
                LLMoFang.flow=result.getLong("flow");
            }else {
                //requestToken过期
                if (response.code()==107)
                {
                    LLMoFang.initializeService.acquireInitData();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
