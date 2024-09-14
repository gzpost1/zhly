package cn.cuiot.dmp.externalapi.service.vendor.gw.dmp;

import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.bean.external.GWEntranceGuardBO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.BaseDmpReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.BaseDmpResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.config.DmpProperties;
import cn.cuiot.dmp.externalapi.service.vendor.gw.enums.DmpEntranceGuardResCode;
import cn.cuiot.dmp.externalapi.service.vendor.gw.utils.Sm3Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * 格物-基础请求业务层
 *
 * @Author: zc
 * @Date: 2024-03-12
 */
@Slf4j
@Service
public class DmpApiService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DmpProperties dmpProperties;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 格物设备调用缓存key
     */
    private static final String CACHE_KEY = "gw:";

    /**
     * 格物请求
     *
     * @Param: gateway：接口地址
     * @Param: method：请求方法（POST,GET）
     * @Param: data：参数
     * @Param: reference：返回类型
     * @return: BaseDmpResp<R>
     */
    public <R, T> BaseDmpResp<R> postRequest(String gateway, T data, GWEntranceGuardBO bo,
                                             TypeReference<BaseDmpResp<R>> reference) {
        String appId = bo.getAppId();
        String appSecret = bo.getAppSecret();

        Map<String, Object> map;
        try {
            map = Sm3Utils.makeToken(appId, appSecret);
        } catch (IOException e) {
            log.error("DmpHttpApi request error", e);
            throw new BusinessException(ResultCode.ERROR, "请求格物门禁异常");
        }

        BaseDmpReq<T> baseDmpReq = new BaseDmpReq<>();
        baseDmpReq.setData(data);
        baseDmpReq.setApp_id(appId);
        baseDmpReq.setTrans_id(map.get("trans_id").toString());
        baseDmpReq.setTimestamp(map.get("timestamp").toString());
        baseDmpReq.setToken(map.get("token").toString());
        //设置requestId
        if (StringUtils.isNotBlank(bo.getRequestId())) {
            baseDmpReq.setRequestId(bo.getRequestId());
        }

        //接口地址
        String url = dmpProperties.getBaseurl() + gateway;
        log.info("request requestData{},url:{}", JsonUtil.writeValueAsString(baseDmpReq), url);

        BaseDmpResp<R> baseDmpResp;
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, baseDmpReq, String.class);
            log.info("responseEntity body:{}", responseEntity.getBody());

            baseDmpResp = JsonUtil.readValue(responseEntity.getBody(), reference);

        } catch (Exception e) {
            log.error("DmpHttpApi request error", e);
            throw new BusinessException(ResultCode.ERROR, "请求格物接口失败");
        }

        if (Objects.isNull(baseDmpResp)) {
            throw new BusinessException(ResultCode.ERROR, "请求格物失败，返回内容为空");
        }

        if (!baseDmpResp.isSuccess()) {
            if (Objects.nonNull(baseDmpResp.getCode())) {
                DmpEntranceGuardResCode itemByCode = DmpEntranceGuardResCode.getItemByCode(baseDmpResp.getCode());
                throw new BusinessException(itemByCode.getErrorCode(), baseDmpResp.getMessage());
            }
            throw new BusinessException(ResultCode.ERROR, "请求格物失败");
        }
        setCacheKey(baseDmpResp, bo);
        return baseDmpResp;
    }

    private void setCacheKey(BaseDmpResp<?> resp, GWEntranceGuardBO bo) {
        if (StringUtils.isBlank(bo.getDeviceKey())) {
            return;
        }
        String requestId;
        if (StringUtils.isNotBlank(requestId = resp.getRequestId()) && Objects.nonNull(resp.getData())) {
            String[] split = requestId.split("-");
            //业务类型
            String businessType = split[0];
            //模型key
            String modelKey = split[1];
            //数据id
            String dataId = split[2];

            Map<String, Object> map = JsonUtil.readValue(JsonUtil.writeValueAsString(resp.getData()), new TypeReference<Map<String, Object>>() {
            });
            if (MapUtils.isNotEmpty(map)) {
                String mapKey = "messageId";
                if (map.containsKey(mapKey)) {
                    String messageId = String.valueOf(map.get(mapKey));

                    //设置缓存（gw:entranceGuard:productKey_deviceKey:模型key:messageId:dataId）
                    String cacheKey = CACHE_KEY + businessType + ":" + bo.getProductKey() + "_" + bo.getDeviceKey() + ":" + modelKey + ":" + messageId;
                    redisUtil.set(cacheKey, dataId, Const.ONE_DAY_SECOND);
                }
            }
        }
    }
}
