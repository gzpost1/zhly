package cn.cuiot.dmp.gateway.service;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 接口统一签名认证
 *
 * @author lixf
 */
public interface SignatureService {
    /**
     * 是否需要签名校验
     *
     * @param exchange exchange
     * @return 是否需要签名校验
     */
    boolean isNeedCheckSignature(ServerWebExchange exchange);


    /**
     * 校验签名
     *
     * @param exchange ex
     * @param chain chain
     * @return mono
     */
    Mono<Void> checkSignature(ServerWebExchange exchange, GatewayFilterChain chain);
}
