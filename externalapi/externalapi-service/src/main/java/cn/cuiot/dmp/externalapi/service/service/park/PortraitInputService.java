package cn.cuiot.dmp.externalapi.service.service.park;

import cn.cuiot.dmp.base.application.dto.AuthDaHuaResp;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.utils.MD5Util;
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
import cn.cuiot.dmp.externalapi.service.constant.PersonGroupRelationConstant;
import cn.cuiot.dmp.externalapi.service.constant.PortraitInputConstant;
import cn.cuiot.dmp.externalapi.service.entity.PersonGroupEntity;
import cn.cuiot.dmp.externalapi.service.entity.PersonGroupRelationEntity;
import cn.cuiot.dmp.externalapi.service.entity.park.FootPlateInfoEntity;
import cn.cuiot.dmp.externalapi.service.entity.park.PlatfromInfoEntity;
import cn.cuiot.dmp.externalapi.service.entity.park.PortraitInputEntity;
import cn.cuiot.dmp.externalapi.service.mapper.park.PortraitInputMapper;
import cn.cuiot.dmp.externalapi.service.query.*;
import cn.cuiot.dmp.externalapi.service.service.PersonGroupService;
import cn.cuiot.dmp.externalapi.service.service.TbPersonGroupRelationService;
import cn.cuiot.dmp.externalapi.service.vo.park.PortraitInputVo;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private TbPersonGroupRelationService personGroupRelationService;
    @Autowired
    private PersonGroupService personGroupService;

    @Autowired
    private SystemApiFeignService systemApiFeignService;

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
        String faceGuid = JSONObject.parseObject(JsonUtil.writeValueAsString(authDaHuaResp.getData())).getString("faceGuid");
        //授权管理
        List<PortraitAccessDto> portraitAccess = createDto.getPortraitAccess();
        for(PortraitAccessDto accessDto : portraitAccess){
            AuthDaHuaResp authResp = authManagement(inputDto, accessDto, admitGuid);
            if(StringUtils.equals(PortraitInputConstant.RESULT_ERROR_DH,authResp.getResult())){
                return IdmResDTO.error(ErrorCode.COMMON_FAILURE.getCode(),authResp.getMsg());
            }

        }

        PortraitInputEntity entity = BeanMapper.map(createDto, PortraitInputEntity.class);
        entity.setAdmitGuid(admitGuid);
        entity.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        entity.setFaceGuid(faceGuid);
        baseMapper.insert(entity);

        //保存分组关系
        PersonGroupRelationEntity relationEntity = new PersonGroupRelationEntity();
        relationEntity.setDataId(entity.getId());
        relationEntity.setBusinessType(PersonGroupRelationConstant.YF_ENTRANCE_GUARD);
        relationEntity.setPersonGroupId(entity.getPersonGroupId());
        personGroupRelationService.createOrUpdate(relationEntity);
        return IdmResDTO.success();
    }

    /**
     * 更新主体信息
     * @param createDto
     * @return
     */
    public IdmResDTO updatePortrait(PortraitInputCreateDto createDto) throws NoSuchAlgorithmException {
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

        //更新识别主体
         updateSubject(createDto, inputDto);
        PortraitInputEntity paramEntity = baseMapper.selectById(createDto.getId());
        //删除人像信息
        deleteFaceRegister(inputDto,paramEntity.getFaceGuid(),paramEntity.getAdmitGuid());
        //注册人像
        AuthDaHuaResp authDaHuaResp = faceRegister(inputDto, createDto, createDto.getAdmitGuid());

        if(StringUtils.equals(PortraitInputConstant.RESULT_ERROR_DH,authDaHuaResp.getResult())){
            return IdmResDTO.error(ErrorCode.COMMON_FAILURE.getCode(),authDaHuaResp.getMsg());
        }
        String faceGuid = JSONObject.parseObject(JsonUtil.writeValueAsString(authDaHuaResp.getData())).getString("faceGuid");

        //授权管理
        List<PortraitAccessDto> portraitAccess = createDto.getPortraitAccess();
        for(PortraitAccessDto accessDto : portraitAccess){
            AuthDaHuaResp authResp = authUpdateManagement(inputDto, accessDto, createDto.getAdmitGuid());
            if(StringUtils.equals(PortraitInputConstant.RESULT_ERROR_DH,authResp.getResult())){
                return IdmResDTO.error(ErrorCode.COMMON_FAILURE.getCode(),authResp.getMsg());
            }
        }
        PortraitInputEntity entity = BeanMapper.map(createDto, PortraitInputEntity.class);
        entity.setFaceGuid(faceGuid);
        baseMapper.updateById(entity);
        //更新分组信息
        PersonGroupRelationEntity relationEntity = new PersonGroupRelationEntity();
        relationEntity.setDataId(entity.getId());
        relationEntity.setBusinessType(PersonGroupRelationConstant.YF_ENTRANCE_GUARD);
        relationEntity.setPersonGroupId(entity.getPersonGroupId());
        personGroupRelationService.createOrUpdate(relationEntity);
        return IdmResDTO.success();

    }

    /**
     * 授权管理
     * @param inputDto
     * @param accessDto
     * @param admitGuid
     * @return
     * @throws NoSuchAlgorithmException
     */
    public AuthDaHuaResp authUpdateManagement(PortraitInputInfoDto inputDto,PortraitAccessDto accessDto,String admitGuid) throws NoSuchAlgorithmException {
        String token = getToken(inputDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set(PortraitInputConstant.CREATE_SUBJECT_TOKEN,token);
        headers.set(PortraitInputConstant.CREATE_SUBJECT_GUID,inputDto.getProjectGuid());
        JSONObject paramJson = new JSONObject();
        paramJson.put("deviceNo", accessDto.getDeviceNo());
        paramJson.put("admitGuids", admitGuid);

        paramJson.put("permission",accessDto.getAccessOptions());
        ResponseEntity<AuthDaHuaResp> responseEntity =
                restTemplate.exchange(PortraitInputConstant.AUTH_UPDATE_MANAGEMENT_URL, HttpMethod.POST,
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
     * 授权管理
     * @param inputDto
     * @param accessDto
     * @param admitGuid
     * @return
     * @throws NoSuchAlgorithmException
     */
    public AuthDaHuaResp authManagement(PortraitInputInfoDto inputDto,PortraitAccessDto accessDto,String admitGuid) throws NoSuchAlgorithmException {
        String token = getToken(inputDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set(PortraitInputConstant.CREATE_SUBJECT_TOKEN,token);
        headers.set(PortraitInputConstant.CREATE_SUBJECT_GUID,inputDto.getProjectGuid());
        JSONObject paramJson = new JSONObject();
        paramJson.put("deviceNo", accessDto.getDeviceNo());
        paramJson.put("admitGuids", admitGuid);

        paramJson.put("permission",accessDto.getAccessOptions());
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
     * 删除人像信息
     * @param inputDto
     * @param
     * @param admitGuid
     * @return
     * @throws NoSuchAlgorithmException
     */
    public AuthDaHuaResp deleteFaceRegister(PortraitInputInfoDto inputDto,String faceGuid ,String admitGuid) throws NoSuchAlgorithmException {
        String token = getToken(inputDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set(PortraitInputConstant.CREATE_SUBJECT_TOKEN,token);
        headers.set(PortraitInputConstant.CREATE_SUBJECT_GUID,inputDto.getProjectGuid());
        JSONObject paramJson = new JSONObject();
        paramJson.put("admitGuid", admitGuid);
        paramJson.put("faceGuid", faceGuid);
        ResponseEntity<AuthDaHuaResp> responseEntity =
                restTemplate.exchange(PortraitInputConstant.DELETE_SUBJECT_REGISTER, HttpMethod.POST,
                        new HttpEntity<>(paramJson,headers),
                        new ParameterizedTypeReference<AuthDaHuaResp>() {
                        });
        AuthDaHuaResp body = responseEntity.getBody();
        if(StringUtils.equals(body.getResult(),PortraitInputConstant.RESULT_DH)){
            log.info("删除人像成功："+JsonUtil.writeValueAsString(body));
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
     * 更新识别主体
     * @param paramDto
     * @param inputDto
     * @throws NoSuchAlgorithmException
     */
    public void  updateSubject(PortraitInputCreateDto paramDto,PortraitInputInfoDto inputDto) throws NoSuchAlgorithmException {
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
        paramJson.put("admitGuid", paramDto.getAdmitGuid());
        ResponseEntity<JSONObject> responseEntity =
                restTemplate.exchange(PortraitInputConstant.UPDATE_SUBJECT_URL, HttpMethod.POST,
                        new HttpEntity<>(paramJson,headers),
                        new ParameterizedTypeReference<JSONObject>() {
                        });
        JSONObject resp = responseEntity.getBody();
        String result = resp.getString("result");

        if(StringUtils.equals(result, PortraitInputConstant.RESULT_DH)){
            log.info("识别主体更新成功"+JsonUtil.writeValueAsString(resp));
        }else{
            throw new RuntimeException("识别主体更新异常"+ resp.getString("msg"));
        }
    }

    /**
     * 删除人像信息
     * @param batchPortraitDto
     * @return
     */
    public IdmResDTO deleteBatchPortrait(BatchPortraitDto batchPortraitDto) {
        List<Long> ids = batchPortraitDto.getIds();
        if(CollectionUtil.isEmpty(ids)){
            return IdmResDTO.success();
        }
        ids.stream().forEach(item->{
            IdParam idParam = new IdParam();
            idParam.setId(item);
            try {
                deletePortrait(idParam);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

        });
        return IdmResDTO.success();
    }
    /**
     * 删除人像信息
     * @param idParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IdmResDTO deletePortrait(IdParam idParam) throws NoSuchAlgorithmException {

        PortraitInputEntity entity = getBaseMapper().selectById(idParam.getId());

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
        //删除识别主体
        deleteSubject(entity.getAdmitGuid(),entity.getName(),inputDto);
        //删除人像信息
//        deleteFaceRegister(inputDto,entity.getAdmitGuid());
        //删除分组关系
        PersonGroupRelationEntity relationEntity = new PersonGroupRelationEntity();
        relationEntity.setDataId(entity.getId());
        relationEntity.setBusinessType(PersonGroupRelationConstant.YF_ENTRANCE_GUARD);
        personGroupRelationService.delete(relationEntity);

        getBaseMapper().deleteById(entity.getId());
        return IdmResDTO.success();
    }

    /**
     * 删除识别主体
     * @param
     * @param inputDto
     * @throws NoSuchAlgorithmException
     */
    public void  deleteSubject(String adminGuid,String name ,PortraitInputInfoDto inputDto) throws NoSuchAlgorithmException {
        String token = getToken(inputDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set(PortraitInputConstant.CREATE_SUBJECT_TOKEN,token);
        headers.set(PortraitInputConstant.CREATE_SUBJECT_GUID,inputDto.getProjectGuid());
        JSONObject paramJson = new JSONObject();
        paramJson.put("admitGuids", adminGuid);
        paramJson.put("name", name);

        ResponseEntity<JSONObject> responseEntity =
                restTemplate.exchange(PortraitInputConstant.DELETE_SUBJECT_URL, HttpMethod.POST,
                        new HttpEntity<>(paramJson,headers),
                        new ParameterizedTypeReference<JSONObject>() {
                        });
        JSONObject resp = responseEntity.getBody();
        String result = resp.getString("result");

        if(StringUtils.equals(result, PortraitInputConstant.RESULT_DH)){
            log.info("删除识别主体"+JsonUtil.writeValueAsString(resp));
        }else{
            throw new RuntimeException("删除识别主体异常"+ resp.getString("msg"));
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
        Page<PortraitInputVo> pages = baseMapper.queryPortraitInputPage(new Page<>(para.getPageNo(), para.getPageSize()), para);
        List<PortraitInputVo> vos = pages.getRecords();
        if(CollectionUtil.isEmpty(vos)){
            return pages;
        }
        List<Long> personGroupIds = vos.stream().filter(it -> Objects.nonNull(it.getPersonGroupId()))
                .map(PortraitInputVo::getPersonGroupId).collect(Collectors.toList());
        //查询分组信息
        if(CollectionUtil.isEmpty(personGroupIds)){
            return pages;
        }

        //查询创建人信息
        List<Long> userIds = vos.stream().filter(item -> Objects.nonNull(item.getUpdateUser())).map(PortraitInputVo::getUpdateUser).collect(Collectors.toList());
        Map<Long,String> userMap= getUserMap(userIds);
        List<PersonGroupEntity> personGroupEntities = Optional.ofNullable(personGroupService.queryPersonGroupByIds(personGroupIds))
                .orElse(new ArrayList<>());
        Map<Long, String> personGroupMap = Optional.ofNullable(personGroupEntities.stream()
                .collect(Collectors.toMap(PersonGroupEntity::getId, PersonGroupEntity::getName))).orElse(new HashMap<>());

        vos.stream().forEach(item->{
            item.setPersonGroupName(personGroupMap.get(item.getPersonGroupId()));
            item.setUpdateName(userMap.get(item.getUpdateUser()));
        });
        return pages;
    }


    public Map<Long,String> getUserMap(List<Long> userIds){
        BaseUserReqDto userReqDto = new BaseUserReqDto();
        userReqDto.setUserIdList(userIds);
        IdmResDTO<List<BaseUserDto>> listIdmResDTO = systemApiFeignService.lookUpUserList(userReqDto);
        List<BaseUserDto> data = Optional.ofNullable(listIdmResDTO.getData()).orElseThrow(()->new RuntimeException("用户信息不存在"));
        return data.stream().collect(Collectors.toMap(BaseUserDto::getId,BaseUserDto::getName ));
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

    /**
     * 查询人像录入详情
     * @param idParam
     * @return
     */
    public IdmResDTO<PortraitInputEntity> queryPortraitInputDetail(IdParam idParam) {

        PortraitInputEntity entity = getBaseMapper().selectById(idParam.getId());

        PersonGroupEntity personGroupEntity = personGroupService.queryForDetail(entity.getPersonGroupId());
        entity.setPersonGroupName(personGroupEntity.getName());
        return IdmResDTO.success(entity);
    }



}
