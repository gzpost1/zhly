package cn.cuiot.dmp.externalapi.service.vendor.hik;

import cn.cuiot.dmp.common.bean.external.HIKEntranceGuardBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp.HikBaseResp;
import cn.cuiot.dmp.externalapi.service.vendor.hik.config.HikProperties;
import cn.cuiot.dmp.externalapi.service.vendor.hik.enums.ProtocolEnum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * 海康-基础请求业务层
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Slf4j
@Service
public class HikApiService {

    @Autowired
    private HikProperties hikProperties;

    private static final String CONTENT_TYPE = "application/json";

    /**
     * 向海康平台发起post请求
     *
     * @param bo   配置参数
     * @param path 接口地址
     * @param body 参数
     * @return String
     */
    public <R> HikBaseResp<R> postForHttp(HIKEntranceGuardBO bo, String path, String body, TypeReference<HikBaseResp<R>> reference) {
        check(bo);
        log.info("企业【" + bo.getCompanyId() + "】发起海康接口请求.........");
        return httpRequest(bo.getAk(), bo.getSk(), path, CONTENT_TYPE, body, ProtocolEnum.HTTP, reference);
    }

    /**
     * 向海康平台发起post请求
     *
     * @param bo   配置参数
     * @param path 接口地址
     * @param body 参数
     * @return String
     */
    public <R> HikBaseResp<R> postForHttps(HIKEntranceGuardBO bo, String path, String body, TypeReference<HikBaseResp<R>> reference) {
        check(bo);
        log.info("企业【" + bo.getCompanyId() + "】发起海康接口请求.........");
        return httpRequest(bo.getAk(), bo.getSk(), path, CONTENT_TYPE, body, ProtocolEnum.HTTPS, reference);
    }

    /**
     * 向海康平台发起post请求
     *
     * @param appKey    ak
     * @param appSecret sk
     * @param path      接口地址
     * @param body      参数
     * @return String
     */
    public <R> HikBaseResp<R> httpRequest(String appKey, String appSecret, String path, String contentType, String body, ProtocolEnum protocol, TypeReference<HikBaseResp<R>> reference) {

        // artemis网关服务器ip端口
        ArtemisConfig.host = hikProperties.getHost();
        // 秘钥appKey
        ArtemisConfig.appKey = appKey;
        // 秘钥appSecret
        ArtemisConfig.appSecret = appSecret;
        // 设置接口的URI地址
        String previewUrl = hikProperties.getArtemisPath() + path;

        HashMap<String, String> urlPath = Maps.newHashMap();
        urlPath.put(protocol.getGet(), previewUrl);
        log.info("request requestData{},url:{}", body, previewUrl);

        HikBaseResp<R> hikBaseResp;
        try {
            String resp = ArtemisHttpUtil.doPostStringArtemis(urlPath, body, null, null, contentType, null);
            log.info("responseEntity body:{}", resp);

            hikBaseResp = JsonUtil.readValue(resp, reference);

        } catch (Exception e) {
            log.error("HikApiService request error", e);
            throw new BusinessException(ResultCode.ERROR, "请求海康接口失败");
        }

        if (Objects.isNull(hikBaseResp)) {
            throw new BusinessException(ResultCode.ERROR, "请求海康接口失败，返回内容为空");
        }

        if (!hikBaseResp.isSuccess()) {
            if (Objects.nonNull(hikBaseResp.getCode())) {
                throw new BusinessException(ResultCode.ERROR, hikBaseResp.getMsg());
            }
            throw new BusinessException(ResultCode.ERROR, "请求海康接口失败");
        }
        return hikBaseResp;
    }

    /**
     * 数据校验
     *
     * @Param bo 参数
     */
    private void check(HIKEntranceGuardBO bo) {
        if (Objects.isNull(bo)) {
            throw new BusinessException(ResultCode.ERROR, "请求海康异常.....");
        }
        if (Objects.isNull(bo.getCompanyId())) {
            throw new BusinessException(ResultCode.ERROR, "请求海康异常，企业信息为空");
        }
        if (!Objects.equals(bo.getStatus(), EntityConstants.ENABLED)) {
            throw new BusinessException(ResultCode.ERROR, "请求海康异常，企业【" + bo.getCompanyId() + "】未启用配置");
        }
        if (StringUtils.isBlank(bo.getAk()) || StringUtils.isBlank(bo.getSk())) {
            throw new BusinessException(ResultCode.ERROR, "请求海康异常，企业【" + bo.getCompanyId() + "】未配置信息");
        }
    }
}
