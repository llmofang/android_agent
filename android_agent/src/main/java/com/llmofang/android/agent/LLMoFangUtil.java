package com.llmofang.android.agent;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

public class LLMoFangUtil {
//    public static Proxy getProxy() {
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.100", 8080));
//        return proxy;
//    }

    public static String Md5(String encodeDate) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(encodeDate.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long getExpireTime(String expireSeconds)
    {
       return getExpireTime(Integer.parseInt(expireSeconds));
    }


    public static long getExpireTime(int expireSeconds)
    {
        Calendar c=Calendar.getInstance();
        c.add(Calendar.SECOND, expireSeconds);
        return c.getTime().getTime();
    }

    public static boolean isExpire(Date expireTime)
    {
        long time=expireTime.getTime()-new Date().getTime();
        if(time>60){
            return false;
        }else{
            return true;
        }
    }
    public static String ConvertToString(InputStream is){
        ByteArrayOutputStream baos   =   new   ByteArrayOutputStream();
        int   i=-1;
        try {
            while((i=is.read())!=-1){
                baos.write(i);

            }
            return   baos.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return "";
    }

}