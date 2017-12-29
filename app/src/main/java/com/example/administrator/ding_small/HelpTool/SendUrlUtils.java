package com.example.administrator.ding_small.HelpTool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by Administrator on 2017/12/21.
 */

public class SendUrlUtils {
    /**
     * 发送GET请求
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws Exception
     */
    public static URLConnection sendGetRequest(String url,
                                               Map<String, String> params, Map<String, String> headers)
            throws Exception {
        StringBuilder buf = new StringBuilder(url);
        Set<Entry<String, String>> entrys = null;
        // 如果是GET请求，则请求参数在URL中
        if (params != null && !params.isEmpty()) {
            buf.append("?");
            entrys = params.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                buf.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }
            buf.deleteCharAt(buf.length() - 1);
        }
        URL url1 = new URL(buf.toString());
        HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
        conn.setRequestMethod("GET");
        // 设置请求头
        if (headers != null && !headers.isEmpty()) {
            entrys = headers.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        conn.getResponseCode();
        return conn;
    }
    /**
     * 发送POST请求
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws Exception
     */
    public static URLConnection sendPostRequest(String url,
                                                Map<String, String> params, Map<String, String> headers)
            throws Exception {
        StringBuilder buf = new StringBuilder();
        Set<Entry<String, String>> entrys = null;
        // 如果存在参数，则放在HTTP请求体，形如name=aaa&age=10
        if (params != null && !params.isEmpty()) {
            entrys = params.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                buf.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }
            buf.deleteCharAt(buf.length() - 1);
        }
        URL url1 = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("contentType","application/json; charset=utf-8");
        conn.setReadTimeout(300);
        conn.getResponseCode(); // 为了发送成功
        conn.connect();
        //System.out.println(conn);
        OutputStream out = conn.getOutputStream();
        out.write(buf.toString().getBytes("UTF-8"));
        out.flush();
        out.close();conn.getResponseCode(); // 为了发送成功

        InputStream in = conn.getInputStream();
        byte[] buffer = new byte[521];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for ( int len = 0; (len = in.read(buffer)) > 0;) {
            baos.write(buffer, 0, len);
        }
        String returnValue = new String(baos.toByteArray(), "utf-8" );
        System.out.println(returnValue);
        baos.flush();
        baos.close();
        in.close();
        conn.disconnect();
        return conn;
    }
}
