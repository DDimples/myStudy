package com.mystudy.web.common.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.*;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    public static final PoolingHttpClientConnectionManager CONNMGR;
    public static final CloseableHttpClient CLIENT;
    public static final int OK = 200;
    private static final int MAX_PER_ROUTE = 100;
    private static final int MAX_TOTAL_ROUTE = 1000;
    /**
     * 自定义HTTP状态-Socket超时
     */
    public static final int STATUS_SOCKET_TIMEOUT = 604;
    /**
     * 自定义HTTP状态-连接超时
     */
    public static final int STATUS_CONNECT_TIMEOUT = 600;
    /**
     * 自定义HTT状态-其它错误
     */
    public static final int STATUS_CODE_OTHER = 603;

    /**
     * 自定义HTT状态-无状态
     */
    public static final int STATUS_CODE_NULL = 601;

    private static final String CHARSET = "UTF-8"; //默认字符编码
    /**
     * 连接超时时间,默认10秒
     */
    private int connectionTimeOut = 10000;
    /**
     * 读数据超时时间,默认10秒
     */
    private int socketTimeOut = 10000;

    private List<Header> headers;

    static {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
                .getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory
                .getSocketFactory();
        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create().
                        register("http", plainsf).register("https", sslsf).build();

        CONNMGR = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        CONNMGR.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        CONNMGR.setMaxTotal(MAX_TOTAL_ROUTE);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                                        int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext
                        .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
//        HttpParams pms = new BasicHttpParams();
//        HttpProtocolParams.setVersion(pms, HttpVersion.HTTP_1_1);
//        HttpProtocolParams
//                .setUserAgent(
//                        pms,
//                        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.204 Safari/534.16");
//        HttpClientParams.setCookiePolicy(pms, CookiePolicy.IGNORE_COOKIES);
//        CLIENT = HttpClientBuilder.create().build();
        CLIENT = HttpClientBuilder.create()
                .setConnectionManager(CONNMGR)
                .setRetryHandler(httpRequestRetryHandler)
                .build();
    }


    /**
     * 新增参数的字符串
     *
     * @param url
     * @param paramsStr
     * @return
     */
    public HttpResult get(String url, String paramsStr) {
        if (StringUtil.isBlank(paramsStr)) {
            return httpExecute(getHttpByNullParams(url));
        }
        return httpExecute(getHttpGet(url, paramsStr));
    }

    public HttpResult getWithCookie(String url, String paramsStr, CookieStore cookieStore) {
        HttpGet httpGet = null;
        if (StringUtil.isBlank(paramsStr)) {
            httpGet = this.getHttpByNullParams(url);
        } else {
            httpGet = this.getHttpGet(url, paramsStr);
        }
        return this.httpExecuteWithCookie(httpGet, cookieStore);
    }

    /**
     * get方式
     * 如果没有超时时间进行限制，则采用默认的超时时间
     *
     * @param url
     * @param params
     * @return
     */
    public HttpResult get(String url, List<NameValuePair> params) {
        if (params == null || params.size() < 1) {
            return httpExecute(getHttpByNullParams(url));
        }
        return httpExecute(getHttpGet(url, params));
    }


    protected HttpGet getHttpGet(String url, String paramsStr) {
        StringBuffer buffer = new StringBuffer(url);
        if (StringUtil.containsNone(url, "?")) {
            buffer.append("?");
        }
        buffer.append(paramsStr);
        return new HttpGet(buffer.toString());
    }

    protected HttpGet getHttpGet(String url, List<NameValuePair> params) {
        String paramStr = URLEncodedUtils.format(params, CHARSET);
        StringBuffer buffer = new StringBuffer(url);
        if (StringUtil.containsNone(url, "?")) {
            buffer.append("?");
        }
        buffer.append(paramStr);
        return new HttpGet(buffer.toString());
    }

    protected HttpGet getHttpByNullParams(String url) {
        StringBuffer buffer = new StringBuffer(url);
        return new HttpGet(buffer.toString());
    }

    /**
     * post方式
     * 如果没有超时时间进行限制，则采用默认的超时时间
     *
     * @param url
     * @param params
     * @return
     */
    public HttpResult post(String url, List<NameValuePair> params) {
        return httpExecute(getHttpPost(url, params));
    }

    public HttpResult post(String url, String content) {
        return httpExecute(getHttpPost(url, content));
    }

    protected HttpPost getHttpPost(String url, List<NameValuePair> params) {
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new UrlEncodedFormEntity(params, CHARSET));
            return post;
        } catch (UnsupportedEncodingException e) {
            logger.error("getHttpPost UnsupportedEncodingException", e);
        }
        return null;
    }

    protected HttpPost getHttpPost(String url, String content) {
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(content, CHARSET));
        return post;

    }

    private void setHeader(HttpRequestBase request) {
        if (headers != null) {
            for (Header header : headers) {
                if (header != null) {
                    request.addHeader(header);
                }
            }
        }
    }

    public HttpResult httpExecute(HttpRequestBase request) {
        return this.httpExecuteWithCookie(request, null);
    }

    public HttpResult httpExecuteWithCookie(HttpRequestBase request, CookieStore cookieStore) {
        HttpEntity entity = null;
        HttpResult result = new HttpResult();
        try {
            setHeader(request);
            setTimeOut(request);
//            if (cookieStore != null) CLIENT.setCookieStore(cookieStore);

            HttpResponse response = CLIENT.execute(request);
            StatusLine status = response.getStatusLine();
            entity = response.getEntity();
            result.setHeaders(response.getAllHeaders());
            result.setStatusCode(status != null ? status.getStatusCode() : STATUS_CODE_NULL);
            if (status != null && status.getStatusCode() == OK) {
                result.setContent(EntityUtils.toString(entity, "utf-8"));
            } else {
                result.setContent("");
            }
        } catch (SocketTimeoutException e) {
            result.setStatusCode(STATUS_SOCKET_TIMEOUT);
            logger.error(
                    "HttpExecute SocketTimeoutException " + request.getURI(), e);
        } catch (ConnectTimeoutException e) {
            result.setStatusCode(STATUS_CONNECT_TIMEOUT);
            logger.error(
                    "HttpExecute ConnectTimeoutException " + request.getURI(),
                    e);
        } catch (Exception e) {
            result.setStatusCode(STATUS_CODE_OTHER);
            logger.error("HttpExecute exception " + request.getURI(), e);
        } finally {
            if (request != null) {
                request.abort();
            }
        }
        return result;
    }

    private void setTimeOut(HttpRequestBase request) {
        RequestConfig requestConfig = RequestConfig.custom()
                // the socket timeout (SO_TIMEOUT) in milliseconds
                .setSocketTimeout(socketTimeOut)
                        // the timeout in milliseconds until a connection is established.
                .setConnectTimeout(connectionTimeOut)
                        // the timeout in milliseconds used when requesting a connection from the connection pool.
                .setConnectionRequestTimeout(connectionTimeOut)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();
        request.setConfig(requestConfig);
    }

    public List<Header> getHeaders() {
        if (headers == null) {
            headers = new ArrayList<Header>();
        }
        return headers;
    }

    public int getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(int connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public int getSocketTimeOut() {
        return socketTimeOut;
    }

    public void setSocketTimeOut(int socketTimeOut) {
        this.socketTimeOut = socketTimeOut;
    }

    private static Scheme getHttpsSupportScheme() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return new Scheme("https", 443, ssf);
        } catch (NoSuchAlgorithmException e) {
            logger.error("getHttpsSupportScheme NoSuchAlgorithmException.", e);
        } catch (KeyManagementException e) {
            logger.error("getHttpsSupportScheme KeyManagementException.", e);
        }
        return null;
    }

    public class HttpResult {
        private Header[] headers;
        private String content;
        private int statusCode = 200;

        public Header[] getHeaders() {
            return headers;
        }

        public void setHeaders(Header[] headers) {
            this.headers = headers;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
    }

}
