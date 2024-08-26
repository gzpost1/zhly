package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.application.dto.AuthDaHuaResp;
import cn.cuiot.dmp.common.bean.external.YFEntranceGuardBO;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.constant.PortraitInputConstant;
import cn.cuiot.dmp.system.infrastructure.entity.bean.UniUbiCommandReq;
import cn.cuiot.dmp.system.infrastructure.entity.bean.UniUbiDeviceQueryReq;
import cn.cuiot.dmp.system.infrastructure.entity.bean.UniUbiDeviceRespInfo;
import cn.cuiot.dmp.system.infrastructure.entity.bean.UniUbiPage;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PortraitInputInfoDto;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * 门禁（宇泛）服务
 *
 * @author gxp
 * @date 2024/8/21 14:20
 */
@Service
@Slf4j
public class UniUbiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PortraitInputService portraitInputService;


    /**
     * 分页查询设备信息
     *
     * @param uniUbiDeviceQueryReq
     * @return
     */
    public UniUbiPage<UniUbiDeviceRespInfo> queryDevicePageV2(UniUbiDeviceQueryReq uniUbiDeviceQueryReq) {
        ResponseEntity<AuthDaHuaResp> responseEntity =
                restTemplate.exchange(PortraitInputConstant.QUERY_DEVICE_PAGE_V2, HttpMethod.POST,
                        new HttpEntity<>(uniUbiDeviceQueryReq, buildHttpHeader()),
                        new ParameterizedTypeReference<AuthDaHuaResp>() {
                        });
        AuthDaHuaResp body = responseEntity.getBody();
        if(Objects.isNull(body)){
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), "分页查询设备信息返回为空");
        }
        if (!StringUtils.equals(body.getResult(), PortraitInputConstant.RESULT_DH)) {
            log.error("分页查询设备信息：" + JsonUtil.writeValueAsString(body));
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), "分页查询设备信息返回异常");
        }
        return JSONObject.parseObject(JSONObject.toJSONString(body.getData()), new TypeReference<UniUbiPage<UniUbiDeviceRespInfo>>(){});
    }

    /**
     * 设备操作指令下发（重启/重置）
     *
     * @param uniUbiCommandReq
     * @return
     */
    public Boolean deviceCommandV2(UniUbiCommandReq uniUbiCommandReq) {
        ResponseEntity<AuthDaHuaResp> responseEntity =
                restTemplate.exchange(PortraitInputConstant.DEVICE_COMMAND_V2, HttpMethod.POST,
                        new HttpEntity<>(uniUbiCommandReq, buildHttpHeader()),
                        new ParameterizedTypeReference<AuthDaHuaResp>() {
                        });
        AuthDaHuaResp body = responseEntity.getBody();
        if(Objects.isNull(body)){
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), "设备操作指令下发返回为空");
        }
        if (!StringUtils.equals(body.getResult(), PortraitInputConstant.RESULT_DH)) {
            log.error("设备操作指令下发：" + JsonUtil.writeValueAsString(body));
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), "设备操作指令下发返回异常");
        }
        return body.getSuccess();
    }

    /**
     * 构建请求头
     *
     * @return
     */
    private HttpHeaders buildHttpHeader(){
        //获取并使用门禁（宇泛）配置
        String json = portraitInputService.getBaseMapper().queryPlatfromInfo(LoginInfoHolder.getCurrentOrgId(), FootPlateInfoEnum.YF_ENTRANCE_GUARD.getId());

        if(StringUtils.isBlank(json)){
            throw new BusinessException(ResultCode.PLATFORM_NOT_CONFIG);
        }
        //json转Object
        YFEntranceGuardBO bo = FootPlateInfoEnum.getObjectFromJsonById(FootPlateInfoEnum.YF_ENTRANCE_GUARD.getId(), json);
        PortraitInputInfoDto configInfo = BeanMapper.copyBean(bo, PortraitInputInfoDto.class);
        String token;
        try {
            token = portraitInputService.getToken(configInfo);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION.code(), "获取token签名异常");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set(PortraitInputConstant.CREATE_SUBJECT_TOKEN, token);
        headers.set(PortraitInputConstant.CREATE_SUBJECT_GUID, configInfo.getProjectGuid());
        return headers;
    }


//    public static void main(String[] args) throws NoSuchAlgorithmException {
//        long timestamp = new Date().getTime();
//        System.out.println(timestamp);
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(("718F60A2B83148C7800908DE657CB174" + timestamp + "4403EA87E44F4BDA91540B7231889493").getBytes());
//        byte[] digest = md.digest();
//        String sign = MD5Util.toHexString(digest).toLowerCase();
//        System.out.println(sign);
//    }

}
