  package com.llmofang.android.agent;
  
 


import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;


  public final class HttpInstrumentation
  {
      public static  final String REQESTTOKEN_HEADER="Llmf-Proxy-Authorization";
      public static final int REQUESTTOKEN_EXPIRED=107;
      public static  final  String PROXYERRORMSG="HTTP/1.1 407 ProxyAuthRequired";
      public static  final  String OKHTTPPROXYERRORMSG="Unexpected response code for CONNECT: 401";
    @ReplaceCallSite
    public static URLConnection openConnection(URL url) throws IOException {
        if(LLMoFangProxyService.whetherSetProxy()) {
            if (url.getProtocol().equals("http")) {
                Proxy proxy = LLMoFangProxyService.getProxy(LLMoFang.httpProxyUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
                connection.setRequestProperty(REQESTTOKEN_HEADER, LLMoFang.requestToken);
                return  connection;
            } else if (url.getProtocol().equals("https")) {
                Proxy proxy = LLMoFangProxyService.getProxy(LLMoFang.httpsProxyUrl);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(proxy);
                connection.setRequestProperty(REQESTTOKEN_HEADER, LLMoFang.requestToken);
                return  connection;

            } else {
                return url.openConnection();
            }
        }else {
            return url.openConnection();
        }

    }


      @ReplaceCallSite
      public static URLConnection openConnectionWithProxy(URL url,Proxy proxy) throws IOException {

          if(LLMoFangProxyService.whetherSetProxy()) {
              if (url.getProtocol().equals("http")) {
                  Proxy llmofangProxy = LLMoFangProxyService.getProxy(LLMoFang.httpProxyUrl);
                  HttpURLConnection connection = (HttpURLConnection) url.openConnection(llmofangProxy);
                  connection.setRequestProperty("X-LLMoFang-OrginalProxy", proxy.toString());
                  connection.setRequestProperty(REQESTTOKEN_HEADER, LLMoFang.requestToken);
                  return  connection;
              } else if (url.getProtocol().equals("https")) {
                  Proxy llmofangProxy = LLMoFangProxyService.getProxy(LLMoFang.httpsProxyUrl);
                  HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(llmofangProxy);
                  connection.setRequestProperty("X-LLMoFang-OrginalProxy", proxy.toString());
                  connection.setRequestProperty(REQESTTOKEN_HEADER, LLMoFang.requestToken);
                  return  connection;
              } else {
                  return url.openConnection();
              }
          }else {
              return url.openConnection();
          }



      }
      @ReplaceCallSite
      public static void connect(HttpsURLConnection connection) throws IOException {
          try {
              connection.connect();
              if (connection.getResponseCode() == 401) {
                  String responseData = LLMoFangUtil.ConvertToString(connection.getErrorStream());
                  try {
                      JSONObject jsonObject = new JSONObject(responseData);
                      int code = jsonObject.getInt("code");
                      LLMoFang.initializeService.acquireAppToken();
                      connection.disconnect();
                      throw new IOException();
                  } catch (JSONException e) {
                  }
              }
          }catch(IOException e){
                  if (e.getMessage().equals(PROXYERRORMSG)) {
                      LLMoFang.initializeService.acquireAppToken();
                  } else {
                     proxyErrorHandler();
                  }
                  throw new IOException();
              }
          }

      @ReplaceCallSite
      public static void connect(HttpURLConnection connection) throws IOException {
          try {
              if (connection.getResponseCode() == 401) {
                  String responseData = LLMoFangUtil.ConvertToString(connection.getErrorStream());
                  try {
                      JSONObject jsonObject = new JSONObject(responseData);
                      int code = jsonObject.getInt("code");
                      LLMoFang.initializeService.acquireAppToken();
                      connection.disconnect();
                      throw new IOException();
                  } catch (JSONException e) {
                  }
              }

          }catch (IOException e) {
              if (e.getMessage() .equals(PROXYERRORMSG)) {
                  LLMoFang.initializeService.acquireAppToken();
              }else {
                  proxyErrorHandler();
              }
              throw new IOException();
          }

      }

    @ReplaceCallSite
    public static void connect(URLConnection connection) throws IOException {
        if(connection instanceof  HttpURLConnection)
        {

            if (connection instanceof  HttpsURLConnection) {
                connect((HttpsURLConnection)connection);
            }else {
                connect((HttpURLConnection)connection);
            }
        }
    }



    @ReplaceCallSite
    public static HttpResponse execute(HttpClient httpClient, HttpHost target, HttpRequest request, HttpContext context) throws IOException

    {
        if(LLMoFangProxyService.whetherSetProxy()) {
            try{
                setOriginProxy(target, request);
                LLMoFangProxyService.httpclientSetProxy(httpClient);
                request.addHeader(REQESTTOKEN_HEADER, LLMoFang.requestToken);
                HttpResponse response=httpClient.execute(target,request,context);
                return responseHandler(response);
            }catch (IOException e )
            {
                proxyErrorHandler();
                throw new IOException();
            }
        }else {
            return  httpClient.execute(target,request,context);
        }
    }




      @ReplaceCallSite
    public static <T> T execute(HttpClient httpClient, HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
      throws IOException, ClientProtocolException
    {
        if(LLMoFangProxyService.whetherSetProxy()) {
            try{
            setOriginProxy(target, request);
            LLMoFangProxyService.httpclientSetProxy(httpClient);
            request.addHeader(REQESTTOKEN_HEADER, LLMoFang.requestToken);
            HttpResponse response=httpClient.execute(target,request,context);

            return responseHandler.handleResponse(responseHandler(response));
        }catch (Exception e )
        {
            proxyErrorHandler();
            throw new IOException();
        }
        }else{

        return  httpClient.execute(target,request,responseHandler,context);
        }
    }

    @ReplaceCallSite()
    public static <T> T execute(HttpClient httpClient, HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws Exception {
        if(LLMoFangProxyService.whetherSetProxy()) {
            try{
                setOriginProxy(target, request);
                LLMoFangProxyService.httpclientSetProxy(httpClient);
                request.addHeader(REQESTTOKEN_HEADER, LLMoFang.requestToken);
                HttpResponse response=httpClient.execute(target,request);

                return responseHandler.handleResponse(responseHandler(response));
            }catch (Exception e )
            {
                proxyErrorHandler();
                throw new IOException();
            }
        }else{

            return  httpClient.execute(target,request,responseHandler);
        }
    }

    @ReplaceCallSite
    public static HttpResponse execute(HttpClient httpClient, HttpHost target, HttpRequest request) throws IOException
    {

        if(LLMoFangProxyService.whetherSetProxy()) {
            try{
                setOriginProxy(target, request);
                LLMoFangProxyService.httpclientSetProxy(httpClient);
                request.addHeader(REQESTTOKEN_HEADER, LLMoFang.requestToken);
                HttpResponse response=httpClient.execute(target,request);

                return responseHandler(response);
            }catch (Exception e )
            {
                proxyErrorHandler();
                throw new IOException();
            }
        }else{

            return  httpClient.execute(target,request);
        }

    }

    @ReplaceCallSite
    public static HttpResponse execute(HttpClient httpClient, HttpUriRequest request, HttpContext context) throws IOException
    {
        if(LLMoFangProxyService.whetherSetProxy()) {
            try{
                setOriginProxy(httpClient, request);
                LLMoFangProxyService.httpclientSetProxy(httpClient);
                request.addHeader(REQESTTOKEN_HEADER, LLMoFang.requestToken);
                HttpResponse response=httpClient.execute(request,context);

                return responseHandler(response);
            }catch (Exception e )
            {
                proxyErrorHandler();
                throw new IOException();
            }
        }else{

            return  httpClient.execute(request,context);
        }
    }

    @ReplaceCallSite
    public static <T> T execute(HttpClient httpClient, HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
      throws IOException, ClientProtocolException
    {
        if(LLMoFangProxyService.whetherSetProxy()) {
            try{
                setOriginProxy(httpClient, request);
                LLMoFangProxyService.httpclientSetProxy(httpClient);
                request.addHeader(REQESTTOKEN_HEADER, LLMoFang.requestToken);
                HttpResponse response=httpClient.execute(request,context);

                return responseHandler.handleResponse(responseHandler(response));
            }catch (Exception e )
            {
                proxyErrorHandler();
                throw new IOException();
            }
        }else{

            return  httpClient.execute(request,responseHandler,context);
        }

    }

    @ReplaceCallSite
    public static <T> T execute(HttpClient httpClient, HttpUriRequest request, ResponseHandler<? extends T> responseHandler)
      throws IOException, ClientProtocolException
    {
        if(LLMoFangProxyService.whetherSetProxy()) {
            try{
                setOriginProxy(httpClient, request);
                LLMoFangProxyService.httpclientSetProxy(httpClient);
                request.addHeader(REQESTTOKEN_HEADER, LLMoFang.requestToken);
                HttpResponse response=httpClient.execute(request);

                return responseHandler.handleResponse(responseHandler(response));
            }catch (Exception e )
            {
                proxyErrorHandler();
                throw new IOException();
            }
        }else{

            return  httpClient.execute(request,responseHandler);
        }
    }

    @ReplaceCallSite
    public static HttpResponse execute(HttpClient httpClient, HttpUriRequest request) throws IOException {
        if(LLMoFangProxyService.whetherSetProxy()) {
            try{
                setOriginProxy(httpClient, request);
                LLMoFangProxyService.httpclientSetProxy(httpClient);
                request.addHeader(REQESTTOKEN_HEADER, LLMoFang.requestToken);
                HttpResponse response=httpClient.execute(request);

                return responseHandler(response);
            }catch (Exception e )
            {
                proxyErrorHandler();
                throw new IOException();
            }
        }else{

            return  httpClient.execute(request);
        }
    }

      private static void setOriginProxy(HttpClient httpClient, HttpRequest request) {
          HttpHost host= (HttpHost) httpClient.getParams().getParameter("http.route.default-proxy");
          if(host!=null) {
              request.addHeader("X-LLMoFang-OrginalProxy", host.toString());
          }
      }

      private static void setOriginProxy(HttpHost host,HttpRequest request)
      {
          if(host!=null) {
              request.addHeader("X-LLMoFang-OrginalProxy", host.toString());
          }
      }

      private static HttpResponse responseHandler(HttpResponse response) throws IOException {
         if(response.getStatusLine().getStatusCode()==401){
              try {
                  String responseData = EntityUtils.toString(response.getEntity());
                  JSONObject responseJson = new JSONObject(responseData);
                  int code = responseJson.getInt("code");
                  LLMoFang.initializeService.acquireAppToken();
                  throw new IOException();
              }
              catch (JSONException jsonexception){
                  return response;
              }


          }else {
              return response;
          }
      }

      public static void proxyErrorHandler()
      {
          LLMoFang.errorRetry = LLMoFang.errorRetry - 1;
          LLMoFangUtil.showToast("流量魔方服务器出错请重新刷新");
      }

  }

