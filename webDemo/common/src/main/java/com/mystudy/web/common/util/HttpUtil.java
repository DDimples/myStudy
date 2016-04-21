package com.mystudy.web.common.util;


import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.IOException;

/**
 * @Title: HttpUtil.java
 * @Description: Http协议帮助类
 */
public class HttpUtil {

    static HttpClient httpClient;
    static{
        PoolingClientConnectionManager connectionManager=new PoolingClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(20);
        connectionManager.setMaxTotal(100);
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUserAgent(httpParams, "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.204 Safari/534.16");
        HttpClientParams.setCookiePolicy(httpParams, "ignoreCookies");
        httpClient=new DefaultHttpClient(connectionManager,httpParams);
    }

    public HttpResponse get(String url) throws IOException {
        return httpClient.execute(new HttpGet(url));
    }

}