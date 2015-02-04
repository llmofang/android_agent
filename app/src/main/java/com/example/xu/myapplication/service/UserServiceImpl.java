package com.example.xu.myapplication.service;

import android.app.DownloadManager;
import android.util.Log;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import com.llmofang.android.agent.HttpInstrumentation;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * Created by think on 14-11-10.
 */
public class UserServiceImpl implements UserService {

    private static final String TAG = "UserServiceImpl";

    @Override
    public void userLogin(String username, String password) throws Exception {

//        Log.i(TAG, username);
//        Log.i(TAG, password);

        //Thread.sleep(3000);

//        HttpClient client = new DefaultHttpClient();
//        String uri = "http://www.baidu.com";
//        HttpGet get = new HttpGet(uri);
//        ResponseHandler<String> responseHandle = new BasicResponseHandler();
//
//
//        String responseBody =(String) client.execute(get,responseHandle);


      //  Log.i(TAG, responseBody);
//        URL url = new URL("http://www.baidu.com");
//        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//        connection.getURL().openConnection();
//        connection.connect();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(
//                connection.getInputStream()));
//
//
//        reader.close();
//        // 断开连接
//        connection.disconnect();





//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url("http://www.baidu.com").build();
//       // Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("10.0.0.10", 8080));
//        //client.setProxy(proxy);
//        Response response = client.newCall(request).execute();
//        System.out.println(response.body().string());
//        Log.i(TAG, response.body().string());


        OkHttpClient client = new OkHttpClient();
        URL url = new URL("http://www.baidu.com");
        //Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("115.238.145.51", 59901));
        HttpURLConnection connection = new OkUrlFactory(client).open(url);
        connection.connect();
        OutputStream out = null;
        InputStream in = null;
        try {
            // Write the request.
            connection.setRequestMethod("POST");
            out = connection.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            System.out.println("=============================");
            System.out.println("Contents of get request");
            System.out.println("=============================");
            String lines;
            while ((lines = reader.readLine()) != null) {
                System.out.println(lines);
                Log.i(TAG, lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
            System.out.println("=============================");
            System.out.println("Contents of get request ends");
            System.out.println("=============================");
            // out.write(body);
            out.close();

            // Read the response.
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Unexpected HTTP response: "
                        + connection.getResponseCode() + " " + connection.getResponseMessage());
            }
            in = connection.getInputStream();
        } finally {
            // Clean up.
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }
}
