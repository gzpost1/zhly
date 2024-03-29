package cn.cuiot.dmp.base.infrastructure.http;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContexts;

/**
 * @Author: lifei
 * @Description: httpclient 构造公共方法类
 * @Date: 2020/10/27
 */
@Slf4j
public class HttpClientUtils {

    private HttpClientUtils() {
        throw new BusinessException(ResultCode.CANNOT_CREATE_CONST_CLASS_OBJECT);
    }

    public static SSLContext buildSSLContext() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return SSLContexts.custom()
                //忽略掉对服务器端证书的校验
                .loadTrustMaterial((TrustStrategy) (chain, authType) -> true)
                .build();
    }

    /**
     * 默认构建信任域
     */

    private static SSLConnectionSocketFactory buildSSLSocketFactory() {
        try {
            SSLContext sslcontext = buildSSLContext();
            return new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},
                    null,
                    NoopHostnameVerifier.INSTANCE);
        } catch (Exception ex) {
            log.error("", ex);
            return null;
        }
    }

    public static SSLConnectionSocketFactory buildSSLSocketFactory(SSLContext sslContext) {
        try {
            return new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},
                    null,
                    NoopHostnameVerifier.INSTANCE);
        } catch (Exception ex) {
            log.error("", ex);
            return buildSSLSocketFactory();
        }
    }

    public static RequestConfig buildRequestConfig(HttpClientConfig httpClientConfig) {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(httpClientConfig.getConnectTimeout())
                .setConnectionRequestTimeout(httpClientConfig.getConnectionRequestTimeout())
                .setSocketTimeout(httpClientConfig.getResponseTimeout())
                .build();
        return config;
    }

}
