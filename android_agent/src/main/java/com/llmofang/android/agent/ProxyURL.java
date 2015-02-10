package com.llmofang.android.agent;

/**
 * Created by xu on 2015/2/4.
 */
public class ProxyURL {
    private String address;
    private int port;
    private String proxyurl;

    public ProxyURL(String url) {
        this.proxyurl=url;
        String[] strings=url.split(":");
        address=strings[0];
        port=Integer.parseInt(strings[1]);
    }


    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getProxyurl() {
        return proxyurl;
    }
}
