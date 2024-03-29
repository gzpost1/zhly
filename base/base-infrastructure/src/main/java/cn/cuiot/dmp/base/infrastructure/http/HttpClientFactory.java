package cn.cuiot.dmp.base.infrastructure.http;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: lifei
 * @Description: http client 客户端工厂
 * @Date: 2020/9/22
 */
@Slf4j
@Service
public class HttpClientFactory {
    @Autowired
    HttpClientConfig httpClientConfig;

    private HttpClientService defaultHttpClientService;

    public HttpClientService getHttpClient() {

        return defaultHttpClientService;
    }

    /**
     * 默认构建不开双向检验的推送服务器，双向校验的随时定制
     */
    @PostConstruct
    private void init() {
        defaultHttpClientService = new HttpClientSyncServiceImpl(httpClientConfig);
    }


}