package cn.cuiot.dmp.externalapi.service.vendor.watermeter.service;

import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.bean.external.SDKDWaterMeterBO;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.feign.SystemApiService;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean.*;
import cn.cuiot.dmp.externalapi.service.vendor.watermeter.constant.WaterMeterConstant;
import com.github.houbb.heaven.util.lang.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 物联网水表（山东科德）服务
 *
 * @author gxp
 * @date 2024/8/21 14:20
 */
@Service
@Slf4j
public class WaterMeterService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SystemApiService systemApiService;


    /**
     * 获取上报数据
     *
     * @param req
     * @return
     */
    public WaterMeterPage<WaterMeterReportDataResp> queryReportData(WaterMeterReportDataQueryReq req) {
        ResponseEntity<WaterMeterPage<WaterMeterReportDataResp>> responseEntity =
                restTemplate.exchange(buildUrl(WaterMeterConstant.QUERY_REPORT_DATA_LIST, BeanUtil.beanToMap(req)), HttpMethod.POST,
                        new HttpEntity<>(null, null),
                        new ParameterizedTypeReference<WaterMeterPage<WaterMeterReportDataResp>>() {
                        });
        WaterMeterPage<WaterMeterReportDataResp> body = responseEntity.getBody();
        if (Objects.isNull(body)) {
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), "获取上报数据返回为空");
        }
        if (!body.success()) {
            log.error("获取上报数据：" + JsonUtil.writeValueAsString(body));
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), body.getMsg());
        }
        return body;
    }

    /**
     * 下发阀控指令
     *
     * @param req
     * @return
     */
    public WaterMeterCommandControlResp deviceCommandV2(WaterMeterCommandControlReq req) {
        ResponseEntity<WaterMeterCommandControlResp> responseEntity =
                restTemplate.exchange(buildUrl(WaterMeterConstant.CREATE_SUBJECT_URL, BeanUtil.beanToMap(req)), HttpMethod.POST,
                        new HttpEntity<>(null, null),
                        new ParameterizedTypeReference<WaterMeterCommandControlResp>() {
                        });
        WaterMeterCommandControlResp body = responseEntity.getBody();
        if (Objects.isNull(body)) {
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), "下发阀控指令返回为空");
        }
        if (!body.success()) {
            log.error("下发阀控指令：" + JsonUtil.writeValueAsString(body));
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), "下发阀控指令返回异常");
        }
        return body;
    }

    /**
     * 构建请求地址
     *
     * @param method
     * @return
     */
    private String buildUrl(String method, Map<String, Object> paramsMap) {
        //获取并使用山东科德-物联网水表配置
        List<PlatfromInfoRespDTO> configInfoList = systemApiService
                .queryPlatfromInfoPage(new PlatfromInfoReqDTO(FootPlateInfoEnum.SDKD_WATER_METER.getId(), LoginInfoHolder.getCurrentOrgId()))
                .getRecords();
        if (CollectionUtils.isEmpty(configInfoList)) {
            throw new BusinessException(ResultCode.PLATFORM_NOT_CONFIG);
        }
        //json转Object
        SDKDWaterMeterBO configInfo = FootPlateInfoEnum.getObjectFromJsonById(FootPlateInfoEnum.SDKD_WATER_METER.getId(), configInfoList.get(0).getData());
        //构建url
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(configInfo.getIp()).append(method)
                .append(WaterMeterConstant.URL_LABEL).append(WaterMeterConstant.WATER_METER_HEADER_KEY_FIELD).append(WaterMeterConstant.URL_PARAMS_EQUAL_LABEL).append(configInfo.getKey());
        if(Objects.nonNull(paramsMap) && !paramsMap.isEmpty()){
            for(String key : paramsMap.keySet()){
                stringBuilder.append(WaterMeterConstant.URL_PARAMS_SEPARATE_LABEL).append(key).append(WaterMeterConstant.URL_PARAMS_EQUAL_LABEL).append(paramsMap.get(key));
            }
        }
        return stringBuilder.toString();
    }

}
