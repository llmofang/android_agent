  package com.llmofang.android.agent;
  
 

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import com.squareup.okhttp.Request;

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


    public static HttpURLConnection open_2(OkUrlFactory factory,URL url)
    {
      OkHttpClient client=factory.client();
      client.setProxy(LLMoFangUtil.getProxy());
      HttpURLConnection connection= new OkUrlFactory(client).open(url);
      return connection;
    }


    public static Call newCall(OkHttpClient client,Request request)
    {
      client.setProxy(LLMoFangUtil.getProxy());
      return client.newCall(request);
    }
  }

