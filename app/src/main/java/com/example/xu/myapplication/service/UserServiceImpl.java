package com.example.xu.myapplication.service;

import android.net.Proxy;
import android.util.Log;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import com.llmofang.android.agent.HttpInstrumentation;

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

        HttpClient client = new DefaultHttpClient();
        String uri = "http://www.baidu.com";
        HttpGet get = new HttpGet(uri);
        ResponseHandler<String> responseHandle = new BasicResponseHandler();


        String responseBody =(String) client.execute(get,responseHandle);

        // URL urll=new URL("www.baidu.com");
       // urll.openConnection();
        Log.i(TAG, responseBody);
//
//        if (username.equals("tom") && password.equals("123")) {
//            Log.i(TAG,"Username and Password all right!");
//
//        } else {
//            throw new ServiceRulesException("UserName or Password Error!");
//        }


    }
}
