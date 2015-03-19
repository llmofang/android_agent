  package com.llmofang.android.agent;
  
 

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


  public class OkHttpInstrumentation
  {
//    public static HttpURLConnection open(HttpURLConnection connection)
//    {
//      if ((connection instanceof HttpsURLConnection))
//        return new HttpsURLConnectionExtension((HttpsURLConnection)connection);
//      if (connection != null) {
//        return new HttpURLConnectionExtension(connection);
//      }
//      return null;
//    }
//
//    public static HttpURLConnection openWithProxy(HttpURLConnection connection)
//    {
//      if ((connection instanceof HttpsURLConnection))
//        return new HttpsURLConnectionExtension((HttpsURLConnection)connection);
//      if (connection != null) {
//        return new HttpURLConnectionExtension(connection);
//      }
//      return null;
//    }


//    public static HttpURLConnection open_2 (OkUrlFactory factory,URL url)throws IOException
//    {
//        OkHttpClient client = factory.client();
//        HttpURLConnection connection;
//        if(LLMoFangProxyService.whetherSetProxy()) {
//            try{
//                client.setProxy(LLMoFangProxyService.getProxy(LLMoFang.httpProxyUrl));
//                connection = new OkUrlFactory(client).open(url);
//                if(connection.getResponseCode()==401)
//                {
//                    String responseData=LLMoFangUtil.ConvertToString(connection.getErrorStream());
//                    try {
//                        JSONObject jsonObject=new JSONObject(responseData);
//                        int code=jsonObject.getInt("code");
//                        LLMoFang.initializeService.acquireAppToken();
//                        connection.disconnect();
//                        throw new IOException();
//                    } catch (JSONException e) {
//                        return connection;
//                    }
//                }else {
//                    return connection;
//                }
//            }
//            catch (Exception e){
//                if (e.getMessage().equals(HttpInstrumentation.PROXYERRORMSG)) {
//                    LLMoFang.initializeService.acquireAppToken();
//                }else {
//                    LLMoFang.errorRetry = LLMoFang.errorRetry - 1;
//                }
//                throw new IOException();
//            }
//        }else {
//            connection = new OkUrlFactory(client).open(url);
//
//        }
//      return connection;
//    }


    public static Call newCall(OkHttpClient client,Request request)
    {
        if(LLMoFangProxyService.whetherSetProxy()) {
            client.setProxy(LLMoFangProxyService.getProxy(LLMoFang.httpProxyUrl));
            Headers headers=request.headers();
            request.newBuilder().addHeader(HttpInstrumentation.REQESTTOKEN_HEADER, LLMoFang.requestToken).build();
            return  client.newCall(request);

        }else {
            return client.newCall(request);
        }
    }

    public static Response execute(Call call) throws  IOException
    {
        try {
            Response response=  call.execute();
            if(response.code()==401){
                try {
                    JSONObject jsonObject=new JSONObject(response.body().toString());
                    int code=jsonObject.getInt("code");
                    LLMoFang.initializeService.acquireAppToken();
                    throw new IOException();
                } catch (JSONException e) {
                    LLMoFang.errorRetry = LLMoFang.errorRetry - 1;
                    return response;
                }
            }else{
                return response;
            }
        }catch (Exception e)
        {
            if (e.getMessage().equals(HttpInstrumentation.OKHTTPPROXYERRORMSG)||e.getMessage().equals(HttpInstrumentation.PROXYERRORMSG)){
                LLMoFang.initializeService.acquireAppToken();
            }else {
                LLMoFang.errorRetry = LLMoFang.errorRetry - 1;
            }
            throw new IOException();
        }

    }
  }

