package com.example.xu.myapplication.service;


import android.util.Log;


import com.llmofang.android.agent.LLMoFangUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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

        HttpClient client = new DefaultHttpClient();
        String uri = "http://www.baidu.com";
        HttpGet get = new HttpGet(uri);
        ResponseHandler<String> responseHandle = new BasicResponseHandler();


        HttpResponse response = client.execute(get);
        String responseBody= EntityUtils.toString(response.getEntity());

        Log.i(TAG, responseBody);
//        URL url = new URL("http://www.baidu.com");
//        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
//
//       String s= LLMoFangUtil.ConvertToString(connection.getInputStream());
//        Log.i("[llmofang]",s);
//        // 断开连接
//        connection.disconnect();





//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url("http://www.baidu.com").build();
//       // Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("10.0.0.10", 8080));
//        //client.setProxy(proxy);
//        Response response = client.newCall(request).execute();
//        System.out.println(response.body().string());
//        Log.i(TAG, response.body().string());


//        OkHttpClient client = new OkHttpClient();
//        URL url = new URL("http://www.baidu.com");
//        //Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("115.238.145.51", 59901));
//        HttpURLConnection connection = new OkUrlFactory(client).open(url);
//        connection.connect();
//        OutputStream out = null;
//        InputStream in = null;
//        try {
//            // Write the request.
//            connection.setRequestMethod("POST");
//            out = connection.getOutputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            System.out.println("=============================");
//            System.out.println("Contents of get request");
//            System.out.println("=============================");
//            String lines;
//            while ((lines = reader.readLine()) != null) {
//                System.out.println(lines);
//                Log.i(TAG, lines);
//            }
//            reader.close();
//            // 断开连接
//            connection.disconnect();
//            System.out.println("=============================");
//            System.out.println("Contents of get request ends");
//            System.out.println("=============================");
//            // out.write(body);
//            out.close();
//
//            // Read the response.
//            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                throw new IOException("Unexpected HTTP response: "
//                        + connection.getResponseCode() + " " + connection.getResponseMessage());
//            }
//            in = connection.getInputStream();
//        } finally {
//            // Clean up.
//            if (out != null) out.close();
//            if (in != null) in.close();
//        }
    }
}
