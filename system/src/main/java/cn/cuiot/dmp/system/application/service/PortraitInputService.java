package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.application.dto.AuthDaHuaResp;
import cn.cuiot.dmp.common.bean.external.YFPortraitInputBO;
import cn.cuiot.dmp.common.constant.ErrorCode;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.Aes;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.constant.PortraitInputConstant;
import cn.cuiot.dmp.system.infrastructure.entity.FootPlateInfoEntity;
import cn.cuiot.dmp.system.infrastructure.entity.PlatfromInfoEntity;
import cn.cuiot.dmp.system.infrastructure.entity.PortraitInputEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.*;
import cn.cuiot.dmp.system.infrastructure.entity.vo.PortraitInputVo;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.PortraitInputMapper;
import cn.cuiot.dmp.system.infrastructure.utils.MD5Util;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import static cn.cuiot.dmp.common.constant.CacheConst.SECRET_INFO_KEY;

/**
 *
 * @author pengjian
 * @since 2024-07-18
 */
@Service
@Slf4j
public class PortraitInputService extends ServiceImpl<PortraitInputMapper, PortraitInputEntity> {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PlatfromInfoService platfromInfoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FootPlateInfoService footPlateInfoService;

    /**
     * 保存录入信息
     * @param createDto
     * @return
     */
    public IdmResDTO createPortrait(PortraitInputCreateDto createDto) throws NoSuchAlgorithmException {

        String jsonObject = stringRedisTemplate.opsForValue().get(SECRET_INFO_KEY + createDto.getKid());
        stringRedisTemplate.delete(SECRET_INFO_KEY + createDto.getKid());
        if (StringUtils.isEmpty(jsonObject)) {
            throw new BusinessException(ResultCode.KID_EXPIRED_ERROR, "密钥ID已过期，请重新获取");
        }
        Aes aes = JSONObject.parseObject(jsonObject, Aes.class);
        // 密码解密
        if(StringUtils.isNotBlank(createDto.getPassword())){
            createDto.setPassword(aes.getDecodeValue(createDto.getPassword()));
        }

        if(StringUtils.isNotBlank(createDto.getCardNo())){
            createDto.setCardNo(aes.getDecodeValue(createDto.getCardNo()));
        }
        if(StringUtils.isNotBlank(createDto.getPhone())){
            createDto.setPhone(aes.getDecodeValue(createDto.getPhone()));
        }
        if(StringUtils.isNotBlank(createDto.getIdCardNo())){
            createDto.setIdCardNo(aes.getDecodeValue(createDto.getIdCardNo()));
        }

        //获取大华配置
        Long id = FootPlateInfoEnum.YF_PORTRAIT_INPUT.getId();
        String json = getBaseMapper().queryPlatfromInfo(LoginInfoHolder.getCurrentOrgId(), id);

        if(StringUtils.isBlank(json)){
            return IdmResDTO.error(ResultCode.PLATFORM_NOT_CONFIG.getCode(),ResultCode.PLATFORM_NOT_CONFIG.getMessage());
        }

        //json转Object
        YFPortraitInputBO bo = FootPlateInfoEnum.getObjectFromJsonById(id, json);
        PortraitInputInfoDto inputDto = new PortraitInputInfoDto();
        BeanUtils.copyProperties(bo, inputDto);

        //创建识别主体
        String admitGuid = createSubject(createDto, inputDto);
        //注册人像
        AuthDaHuaResp authDaHuaResp = faceRegister(inputDto, createDto, admitGuid);

        if(StringUtils.equals(PortraitInputConstant.RESULT_ERROR_DH,authDaHuaResp.getResult())){
            return IdmResDTO.error(ErrorCode.COMMON_FAILURE.getCode(),authDaHuaResp.getMsg());
        }
        //授权管理
        for(String device : PortraitInputConstant.AUTH_DEVICE_NOS){
            AuthDaHuaResp authResp = authManagement(inputDto, device, admitGuid);
            if(StringUtils.equals(PortraitInputConstant.RESULT_ERROR_DH,authResp.getResult())){
                return IdmResDTO.error(ErrorCode.COMMON_FAILURE.getCode(),authResp.getMsg());
            }

        }

        PortraitInputEntity entity = BeanMapper.map(createDto, PortraitInputEntity.class);

        baseMapper.insert(entity);


        return IdmResDTO.success();
    }

    /**
     * 授权管理
     * @param inputDto
     * @param deviceNo
     * @param admitGuid
     * @return
     * @throws NoSuchAlgorithmException
     */
    public AuthDaHuaResp authManagement(PortraitInputInfoDto inputDto,String deviceNo,String admitGuid) throws NoSuchAlgorithmException {
        String token = getToken(inputDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set(PortraitInputConstant.CREATE_SUBJECT_TOKEN,token);
        headers.set(PortraitInputConstant.CREATE_SUBJECT_GUID,inputDto.getProjectGuid());
        JSONObject paramJson = new JSONObject();
        paramJson.put("deviceNo", deviceNo);
        paramJson.put("admitGuids", admitGuid);

        JSONObject subParamJson = new JSONObject();
        subParamJson.put("idCardFacePermission",2);
        paramJson.put("permission",subParamJson);
        ResponseEntity<AuthDaHuaResp> responseEntity =
                restTemplate.exchange(PortraitInputConstant.AUTH_MANAGEMENT_URL, HttpMethod.POST,
                        new HttpEntity<>(paramJson,headers),
                        new ParameterizedTypeReference<AuthDaHuaResp>() {
                        });
        AuthDaHuaResp body = responseEntity.getBody();
        if(!StringUtils.equals(body.getResult(),PortraitInputConstant.RESULT_DH)){
            log.info("设备授权失败："+JsonUtil.writeValueAsString(body));
        }
        return body;
    }
    /**
     * 注册
     * @param inputDto
     * @param createDto
     * @param admitGuid
     * @return
     * @throws NoSuchAlgorithmException
     */
    public AuthDaHuaResp faceRegister(PortraitInputInfoDto inputDto,PortraitInputCreateDto createDto,String admitGuid) throws NoSuchAlgorithmException {
        String token = getToken(inputDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set(PortraitInputConstant.CREATE_SUBJECT_TOKEN,token);
        headers.set(PortraitInputConstant.CREATE_SUBJECT_GUID,inputDto.getProjectGuid());
        JSONObject paramJson = new JSONObject();
        paramJson.put("url", createDto.getUrl());
        paramJson.put("admitGuid", admitGuid);
        ResponseEntity<AuthDaHuaResp> responseEntity =
                restTemplate.exchange(PortraitInputConstant.CREATE_SUBJECT_REGISTER, HttpMethod.POST,
                        new HttpEntity<>(paramJson,headers),
                        new ParameterizedTypeReference<AuthDaHuaResp>() {
                        });
        AuthDaHuaResp body = responseEntity.getBody();
        if(StringUtils.equals(body.getResult(),PortraitInputConstant.RESULT_DH)){
            log.info("注册成功："+JsonUtil.writeValueAsString(body));
        }
        return body;
    }
    /**
     * 识别主体创建
     * @param paramDto
     * @param inputDto
     * @throws NoSuchAlgorithmException
     */
    public String  createSubject(PortraitInputCreateDto paramDto,PortraitInputInfoDto inputDto) throws NoSuchAlgorithmException {
        String token = getToken(inputDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set(PortraitInputConstant.CREATE_SUBJECT_TOKEN,token);
        headers.set(PortraitInputConstant.CREATE_SUBJECT_GUID,inputDto.getProjectGuid());
        JSONObject paramJson = new JSONObject();
        paramJson.put("name", paramDto.getName());
        paramJson.put("phone", paramDto.getPhone());
        paramJson.put("tag", paramDto.getTag());
        paramJson.put("cardNo", paramDto.getCardNo());
        paramJson.put("idCardNo", paramDto.getIdCardNo());
        paramJson.put("password", paramDto.getPassword());

        ResponseEntity<JSONObject> responseEntity =
                restTemplate.exchange(PortraitInputConstant.CREATE_SUBJECT_URL, HttpMethod.POST,
                        new HttpEntity<>(paramJson,headers),
                        new ParameterizedTypeReference<JSONObject>() {
                        });
        JSONObject resp = responseEntity.getBody();
        String result = resp.getString("result");

        if(StringUtils.equals(result, PortraitInputConstant.RESULT_DH)){
            log.info("识别主体创建成功"+JsonUtil.writeValueAsString(resp));
            return resp.getJSONObject("data").getString("admitGuid");

        }else{
            throw new RuntimeException("识别主体创建异常"+ resp.getString("msg"));
        }
    }
    /**
     * 获取鉴权token
     * @return
     */
    public String getToken(PortraitInputInfoDto inputDto) throws NoSuchAlgorithmException {

        long timestamp = new Date().getTime();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((inputDto.getAppKey() + timestamp + inputDto.getAppSecret()).getBytes());
        byte[] digest = md.digest();
        String sign = MD5Util.toHexString(digest).toLowerCase();
        HttpHeaders headers = new HttpHeaders();
        headers.set(PortraitInputConstant.APP_KEY_DH,inputDto.getAppKey());
        headers.set(PortraitInputConstant.SIGN_DH,sign);
        headers.set(PortraitInputConstant.TIMESTAMP_DH,String.valueOf(timestamp));
        String url = PortraitInputConstant.PLATFORM_AUTH.replace("{projectGuid}", inputDto.getProjectGuid());
        ResponseEntity<AuthDaHuaResp> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<AuthDaHuaResp>() {
                        });
        AuthDaHuaResp resp = responseEntity.getBody();
        if(StringUtils.equals(resp.getResult(), PortraitInputConstant.RESULT_DH)){
            return (String) resp.getData();
        }else{
            throw new RuntimeException("获取token异常");
        }

    }

    /**
     * 分页查询
     * @param para
     * @return
     */
    public IdmResDTO<IPage<PortraitInputVo>> queryPortraitInputInfo(PortraitInputVo para) {
        para.setCreateUser(LoginInfoHolder.getCurrentUserId());
        Page<PortraitInputVo> page = getBaseMapper().queryPortraitInputInfo
                (new Page<>(para.getPageNo()
                        ,para.getPageSize()),para);
        return IdmResDTO.success(page);
    }

    /**
     * 分页查询
     * @param para
     * @return
     */
    public IPage<PortraitInputVo> queryPortraitInputPage(PortraitInputDTO para) {
        para.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        return baseMapper.queryPortraitInputPage(new Page<>(para.getPageNo(),para.getPageSize()), para);
    }

    /**
     * 查询平台信息
     * @param para
     * @return
     */
    public IdmResDTO<List<FootPlateDto>> queryPlatformInfo(PortraitInputVo para) {
        List<FootPlateInfoEntity> entityList = footPlateInfoService.list();
        List<FootPlateDto> platFromDtos = BeanMapper.mapList(entityList, FootPlateDto.class);
        return IdmResDTO.success(platFromDtos);
    }

    /**
     * 更新平台信息
     * @param dto
     * @return
     */
    public IdmResDTO updatePortraitInputInfo(PlatFromDto dto) {
        PlatfromInfoEntity map = BeanMapper.map(dto, PlatfromInfoEntity.class);
        platfromInfoService.saveOrUpdate(map);
        return IdmResDTO.success();
    }

    /**
     * 查询企业填写的平台信息
     * @param queryDto
     * @return
     */
    public IdmResDTO<FootPlateCompanyDto> queryFootPlateCompanyInfo(FootPlateCompanyDto queryDto) {

        LambdaQueryWrapper<PlatfromInfoEntity> lw = new LambdaQueryWrapper<>();
        lw.eq(PlatfromInfoEntity::getPlatformId,queryDto.getPlatformId()).eq(PlatfromInfoEntity::getCompanyId,queryDto.getCompanyId());
        List<PlatfromInfoEntity> list = platfromInfoService.list(lw);
        if(CollectionUtil.isEmpty(list)){
            return IdmResDTO.success();
        }
        FootPlateCompanyDto map = BeanMapper.map(list.get(0), FootPlateCompanyDto.class);
        return  IdmResDTO.success(map);
    }
}
