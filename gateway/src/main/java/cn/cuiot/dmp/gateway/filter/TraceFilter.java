package cn.cuiot.dmp.gateway.filter;

import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 生成日志链路追踪id，并传入header中
 *
 * @author: wuyongchong
 * @date: 2024/5/15 15:49
 */
@Component
public class TraceFilter implements WebFilter, Ordered {

    private static final String TRACE_ID = "traceId";

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        //链路追踪id
        String traceId = IdUtil.fastSimpleUUID();
        MDC.put(TRACE_ID, traceId);
        try {
            ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                    .headers(h -> h.add(TRACE_ID, traceId))
                    .build();
            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        } finally {
            MDC.remove(TRACE_ID);
        }
    }
}
