package cn.cuiot.dmp.externalapi.service.vendor.gw.dmp;

import cn.cuiot.dmp.common.bean.external.GWEntranceGuardBO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.BaseDmpReq;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.BaseDmpResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.config.DmpProperties;
import cn.cuiot.dmp.externalapi.service.vendor.gw.enums.DmpEntranceGuardResCode;
import cn.cuiot.dmp.externalapi.service.vendor.gw.utils.Sm3Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
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

        BaseDmpReq<T> baseDmpReq = new BaseDmpReq<T>();
        baseDmpReq.setData(data);
        baseDmpReq.setApp_id(appId);
        baseDmpReq.setTrans_id(map.get("trans_id").toString());
        baseDmpReq.setTimestamp(map.get("timestamp").toString());
        baseDmpReq.setToken(map.get("token").toString());

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
        return baseDmpResp;
    }
}
