package com.llmofang.android.agent;



import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by xu on 2015/2/5.
 */
public class ProxyRetryTask implements Runnable {
    @Override
    public void run() {
//        OkHttpClient client=new OkHttpClient();
//        Request request = new Request.Builder().url(LLMoFang.HEALTHCHECK+LLMoFang.httpProxyUrl.getAddress())
//                .addHeader("Accept-Version", LLMoFang.INTERFACE_VERSION).build();
        HttpGet get=new HttpGet(LLMoFang.HEALTHCHECK+LLMoFang.httpProxyUrl.getAddress());
        try {
            HttpResponse response = HttpUtil.request(get);
            if(response.getStatusLine().getStatusCode()==200)
            {

                try {
                    String response_body = EntityUtils.toString(response.getEntity());
                    JSONObject result = new JSONObject(response_body);
                    if(result.getInt("status")==0)
                    {
                        LLMoFang.isProxySeverOK=true;
                        LLMoFang.errorRetry=LLMoFang.initialErrorRetry;
                    }else{
                        int interval=result.getInt("interval");
                        LLMoFang.scheduledThreadPoolExecutor.schedule(new ProxyRetryTask(),interval,TimeUnit.SECONDS);
                    }
                } catch (JSONException e) {
                   // e.printStackTrace();
                    LLMoFang.scheduledThreadPoolExecutor.schedule(new ControlCenterRetryTask(),LLMoFang.controlCenterRetrySchedule, TimeUnit.SECONDS);
                }
            }
        } catch (IOException e) {
            LLMoFang.scheduledThreadPoolExecutor.schedule(new ControlCenterRetryTask(),LLMoFang.controlCenterRetrySchedule, TimeUnit.SECONDS);
           // e.printStackTrace();
        }


    }
}
