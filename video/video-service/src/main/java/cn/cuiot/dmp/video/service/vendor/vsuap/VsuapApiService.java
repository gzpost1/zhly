package cn.cuiot.dmp.video.service.vendor.vsuap;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.video.service.vendor.bean.resp.vsuap.VsuapBaseResp;
import cn.cuiot.dmp.video.service.vendor.config.VideoProperties;
import cn.cuiot.dmp.video.service.vendor.enums.VsuapResCode;
import cn.cuiot.dmp.video.service.vendor.util.VsuapSignUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 云智眼-基础请求业务层
 *
 * @Author: zc
 * @Date: 2024-03-12
 */
@Slf4j
@Service
public class VsuapApiService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private VideoProperties videoProperties;

    /**
     * 云智眼请求
     *
     * @Param: gateway：接口地址
     * @Param: method：请求方法（POST,GET）
     * @Param: data：参数
     * @Param: reference：返回类型
     * @return: VsuapBaseResp<R>
     */
    public <R, T> VsuapBaseResp<R> request(String gateway, HttpMethod method, T data, TypeReference<VsuapBaseResp<R>> reference) {
        HttpHeaders headers = new HttpHeaders();
        if (method == HttpMethod.POST) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        //构造请求参数
        Map<String, String> headerMap = new HashMap<>();
        //AK
        headerMap.put("accessKey", videoProperties.getVsuapAccessKey());
        //加签算法，目前仅支持HmacSHA256
        headerMap.put("algorithm", "HmacSHA256");
        //当前时间的毫秒字符串
        headerMap.put("requestTime", System.currentTimeMillis() + "");

        //签名需要的map参数
        Map<String, Object> map = new HashMap<>(headerMap);

        //构造请求参数
        String dataStr = null;
        if (Objects.nonNull(data)) {
            dataStr = JsonUtil.writeValueAsString(data);
            Map json2Map = JsonUtil.readValue(dataStr, HashMap.class);
            map.putAll(json2Map);
        }
        String secretKey = videoProperties.getVsuapSecretKey();
        String sign = VsuapSignUtil.signByHmacSHA256(secretKey, map);
        headerMap.put("sign", sign);

        //设置自定义请求头
        headerMap.keySet().forEach(key -> headers.set(key, headerMap.get(key)));
        HttpEntity httpEntity = new HttpEntity<>(data, headers);

        String url = videoProperties.getVsuapBaseUrl() + gateway;
        log.info("request head:{},requestData{},url:{}", JsonUtil.writeValueAsString(headerMap), dataStr, url);

        VsuapBaseResp<R> vsuapResponse;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, httpEntity, String.class);
            log.info("responseEntity body:{}", responseEntity.getBody());

            vsuapResponse = JsonUtil.readValue(responseEntity.getBody(), reference);

        } catch (Exception e) {
            log.error("VideoHttpApi request error", e);
            throw new BusinessException(ResultCode.ERROR, "请求云智眼接口失败");
        }

        if (Objects.isNull(vsuapResponse)) {
            throw new BusinessException(ResultCode.ERROR, "请求云智眼失败，返回内容为空");
        }

        if (!vsuapResponse.isSuccess()) {
            if (Objects.nonNull(vsuapResponse.getCode())) {
                VsuapResCode itemByCode = VsuapResCode.getItemByCode(vsuapResponse.getCode());
                throw new BusinessException(itemByCode.getErrorCode(), vsuapResponse.getMessage());
            }
            throw new BusinessException(ResultCode.ERROR, "请求云智眼失败");
        }
        return vsuapResponse;
    }
}
