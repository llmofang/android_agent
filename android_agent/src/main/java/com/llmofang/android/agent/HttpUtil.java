package com.llmofang.android.agent;

import com.squareup.okhttp.Response;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by xu on 2015/3/16.
 */
public class HttpUtil {
    private final static int connectionTimeout = 5000;
    private final static int timeoutSocket = 5000;
    public static HttpResponse request(HttpUriRequest request)throws IOException {
        request.setHeader("Accept-Version", LLMoFang.INTERFACE_VERSION);
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters,connectionTimeout);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpResponse response = httpClient.execute(request);
       return  response;

    }
}
