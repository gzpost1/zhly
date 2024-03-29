package cn.cuiot.dmp.gateway.service.impl;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.gateway.config.SignatureConfig;
import cn.cuiot.dmp.gateway.dto.SignatureConfigDto;
import cn.cuiot.dmp.gateway.service.SignatureService;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 接口签名服务
 *
 * @author lixf
 */
@Service
@Slf4j
public class SignatureServiceImpl implements SignatureService {

    @Resource
    private SignatureConfig signatureConfig;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String APPLICATION_JSON = "application/json";

    @Override
    public boolean isNeedCheckSignature(ServerWebExchange exchange) {
        if (Boolean.FALSE.equals(signatureConfig.getStatus())) {
            return false;
        }

        ServerHttpRequest request = exchange.getRequest();
        String contentType = request.getHeaders().getFirst(CONTENT_TYPE);
        String method = request.getMethodValue();
        String path = request.getURI().getPath();

        if (!path.startsWith("/community-system")) {
            return false;
        }

        // 只校验post请求且content-type为json
        return null != contentType &&
                HttpMethod.POST.name().equalsIgnoreCase(method) &&
                contentType.contains(APPLICATION_JSON) &&
                (request.getHeaders().containsKey("token"));
    }

    @Override
    public Mono<Void> checkSignature(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String timestamp = request.getHeaders().getFirst("Timestamp");
        String signature = request.getHeaders().getFirst("Signature");
        String signatureNonce = request.getHeaders().getFirst("Signature-Nonce");
        String clientId = request.getHeaders().getFirst("Client-Id");

        // 参数检查
        SignatureConfigDto configInfo = getConfigInfo(clientId);
        boolean result = checkParam(timestamp, signature, signatureNonce, clientId);
        if (!result) {
            log.error("签名参数校验失败：{}", JSON.toJSONString(request.getHeaders()));
            throw new BusinessException(ResultCode.SIGN_ERROR);
        }

        AtomicBoolean signResult = new AtomicBoolean(false);
        //ServerRequest serverRequest = new DefaultServerRequest(exchange);
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                .flatMap(body -> {
                    String sign = generatorSign(timestamp, signatureNonce, body, configInfo);
                    signResult.set(sign.equals(signature));
                    return Mono.just(body);
                });

        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    if (!signResult.get()) {
                        throw new BusinessException(ResultCode.SIGN_ERROR);
                    }
                    ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
                            exchange.getRequest()) {
                        @Override
                        public HttpHeaders getHeaders() {
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.putAll(super.getHeaders());
                            if (headers.getContentLength() > 0) {
                                httpHeaders.setContentLength(headers.getContentLength());
                            } else {
                                httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                            }
                            return httpHeaders;
                        }

                        @Override
                        public Flux<DataBuffer> getBody() {
                            return outputMessage.getBody();
                        }
                    };

                    return chain.filter(exchange.mutate().request(decorator).build());
                }));
    }

    private String generatorSign(String timestamp, String nonce, String body, SignatureConfigDto configDto) {
        String content = timestamp + configDto.getClientId() + nonce + body;
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, configDto.getAccessKey().getBytes(StandardCharsets.UTF_8)).hmac(content);
        return Base64.encode(hmac);
    }

    /**
     * 参数校验
     *
     * @param timestamp      时间
     * @param signature      签名
     * @param signatureNonce 随机值
     * @param clientId       字段
     * @return 是否成功
     */
    private boolean checkParam(String timestamp, String signature, String signatureNonce, String clientId) {
        try {
            if (StringUtils.isEmpty(timestamp)) {
                log.error("timestamp不能为空");
                return false;
            }
            DateTime time = DateUtil.parse(timestamp, "yyyy-MM-dd HH:mm:ss");
            if (time.isBefore(DateUtil.offsetMinute(new Date(), -1)) ||
                    time.isAfter(DateUtil.offsetMinute(new Date(), 1))) {
                log.error("时间校验失败:{}", time);
            }

            if (StringUtils.isEmpty(signature)) {
                log.error("signature不能为空");
                return false;
            }

            return checkSignatureNonce(signatureNonce, clientId);
        } catch (Exception e) {
            log.error("校验签名参数失败:", e);
        }

        return false;
    }

    private SignatureConfigDto getConfigInfo(String clientId) {
        List<SignatureConfigDto> app = signatureConfig.getApp();
        Optional<SignatureConfigDto> config = app.stream().filter(a -> a.getClientId().equals(clientId)).findFirst();
        if (!config.isPresent()) {
            log.error("clientId不合法：{}", clientId);
            throw new BusinessException(ResultCode.SIGN_ERROR);
        }

        return config.get();
    }

    boolean checkSignatureNonce(String signatureNonce, String clientId) {
        if (StringUtils.isEmpty(signatureNonce) || signatureNonce.length() > 64) {
            log.error("signatureNonce不合法:{}", signatureNonce);
            return false;
        }

        String key = CacheConst.SIGNATURE_NONCE_KEY + clientId + signatureNonce;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            log.error("signatureNonce已存在，禁止重放:{}", key);
            return false;
        }

        stringRedisTemplate.opsForValue().set(key, "1", 5, TimeUnit.MINUTES);
        return true;
    }
}
