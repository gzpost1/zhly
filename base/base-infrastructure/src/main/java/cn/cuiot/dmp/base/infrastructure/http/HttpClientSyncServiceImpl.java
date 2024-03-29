package cn.cuiot.dmp.base.infrastructure.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: lifei
 * @Description: http 客户端同步模块
 * @Date: 2020/10/27
 */
@Slf4j
@Service
public class HttpClientSyncServiceImpl implements HttpClientService {

    private CloseableHttpClient closeableHttpClient;

    private HttpClientConfig httpClientConfig;

    public HttpClientSyncServiceImpl() {
    }

    public HttpClientSyncServiceImpl(HttpClientConfig httpClientConfig) {
        this.httpClientConfig = httpClientConfig;
        try {
            log.info("init http client start, default config is {}", httpClientConfig);
            SSLContext sslcontext = HttpClientUtils.buildSSLContext();

            SSLConnectionSocketFactory trustAll = HttpClientUtils.buildSSLSocketFactory(sslcontext);
            // 配置同时支持 HTTP 和 HTTPS
            // 一个httpClient对象对于https仅会选用一个SSLConnectionSocketFactory
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().
                    register("http", PlainConnectionSocketFactory.getSocketFactory()).
                    register("https", trustAll).build();
            // 初始化连接管理器
            PoolingHttpClientConnectionManager poolConnManager = buildPoolConnManager(socketFactoryRegistry);
            RequestConfig config = buildHttpClient();
            closeableHttpClient = HttpClients.custom()
                    // 设置连接池管理
                    .setConnectionManager(poolConnManager)
                    .setDefaultRequestConfig(config).build();
            log.info("init default http client finish");
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private PoolingHttpClientConnectionManager buildPoolConnManager(Registry<ConnectionSocketFactory> socketFactoryRegistry) {
        PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 同时最多连接数
        poolConnManager.setMaxTotal(httpClientConfig.getPollMaxTotal());
        // 设置最大路由
        poolConnManager.setDefaultMaxPerRoute(httpClientConfig.getPollMaxPeerRouter());
        return poolConnManager;
    }


    private RequestConfig buildHttpClient() {
        return RequestConfig.custom().setConnectTimeout(httpClientConfig.getConnectTimeout())
                .setConnectionRequestTimeout(httpClientConfig.getConnectionRequestTimeout())
                .setSocketTimeout(httpClientConfig.getResponseTimeout())
                .build();
    }

    private String getUrlWithParams(String url, Map<String, String> params) {
        boolean first = true;
        StringBuilder sb = new StringBuilder(url);
        for (String key : params.keySet()) {
            char ch = '&';
            if (first) {
                ch = '?';
                first = false;
            }
            String value = params.get(key);
            if (value != null) {
                try {
                    String paramValue = URLEncoder.encode(value, "UTF-8");
                    sb.append(ch).append(key).append("=").append(paramValue);
                } catch (UnsupportedEncodingException e) {
                    log.error("", e);
                }
            } else {
                log.error("key {} value is empty", key);
            }
        }
        return sb.toString();
    }


    @Override
    public String doGet(String url, Map<String, String> headers, Map<String, String> params) {
        // *) 构建GET请求头
        String apiUrl = getUrlWithParams(url, params);
        HttpGet httpGet = new HttpGet(apiUrl);
        log.info("send request to {}", apiUrl);
        // *) 设置header信息
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }

        try (CloseableHttpResponse response = closeableHttpClient.execute(httpGet)) {
            if (response == null || response.getStatusLine() == null) {
                return null;
            }

            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entityRes = response.getEntity();
            if (entityRes != null) {
                String resp = EntityUtils.toString(entityRes, "UTF-8");
                if (statusCode != HttpStatus.SC_OK) {
                    log.info("request failed, cause {}", resp);
                }
                return resp;
            }
            return null;
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    @Override
    public String doPost(String url, String token, HttpEntity entity) {
        log.info("send to url {}", url);
        String result = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Authentication",token);
            httpPost.setEntity(entity);
            // 执行提交
            HttpResponse response = closeableHttpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("", e);
        }

        return result;
    }

    @Override
    public String doPost(String url, Map<String, String> headers, String body) {
        log.info("send to url {}", url);
        String result = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            // *) 设置header信息
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            HttpEntity entity = new StringEntity(body, ContentType.create("application/json", "utf-8"));

            httpPost.setEntity(entity);
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .build();

            httpPost.setConfig(config);
            // 执行提交
            HttpResponse response = closeableHttpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("", e);
        }

        return result;
    }

    @Override
    public boolean isNull() {
        return closeableHttpClient == null;
    }

    @Override
    public void close() {
        try {
            closeableHttpClient.close();
        } catch (IOException e) {
            log.error("", e);
        }
    }

}
