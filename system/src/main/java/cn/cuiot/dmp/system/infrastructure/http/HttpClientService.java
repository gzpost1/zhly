package cn.cuiot.dmp.system.infrastructure.http;

import java.util.Map;
import org.apache.http.HttpEntity;

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/10/28
 */
public interface HttpClientService {
    /**
     * 是否为Null
     *
     * @return
     */
    boolean isNull();

    /**
     * 取消
     */
    void close();

    /**
     * doGet
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    String doGet(String url, Map<String, String> headers, Map<String, String> params);

    /**
     * POST请求
     * @param url
     * @param token
     * @param entity
     * @return
     */
    String doPost(String url, String token, HttpEntity entity);

    /**
     * HTTP-POST请求
     * @param url
     * @param headers
     * @param body
     * @return
     */
    String doPost(String url, Map<String, String> headers, String body);

}
