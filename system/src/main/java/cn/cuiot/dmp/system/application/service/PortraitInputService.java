package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.application.dto.AuthDaHuaResp;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.Aes;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.constant.PortraitInputConstant;
import cn.cuiot.dmp.system.infrastructure.entity.PlatfromInfoEntity;
import cn.cuiot.dmp.system.infrastructure.entity.PortraitInputEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PlatFromDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PortraitDhCreateDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PortraitInputCreateDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PortraitInputInfoDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.PortraitInputVo;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.PortraitInputMapper;
import cn.cuiot.dmp.system.infrastructure.utils.MD5Util;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.lettuce.core.ScriptOutputType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.record.DVALRecord;
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
import java.util.Map;

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
        createDto.setPassword(aes.getDecodeValue(createDto.getPassword()));

        createDto.setPhone(aes.getDecodeValue(createDto.getPhone()));

        createDto.setCardNo(aes.getDecodeValue(createDto.getCardNo()));
        createDto.setIdCardNo(aes.getDecodeValue(createDto.getIdCardNo()));

        //获取大华配置
        PortraitInputInfoDto inputDto =  getBaseMapper().queryPlatfromInfo(PortraitInputConstant.PLATFORM_TYPE_DH);
        //创建识别主体
        String admitGuid = createSubject(createDto, inputDto);
        //注册人像
        faceRegister(inputDto,createDto,admitGuid);

        PortraitInputEntity entity = BeanMapper.map(createDto, PortraitInputEntity.class);

        baseMapper.insert(entity);


        return IdmResDTO.success();
    }

    public void faceRegister(PortraitInputInfoDto inputDto,PortraitInputCreateDto createDto,String admitGuid) throws NoSuchAlgorithmException {
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
        }else{
            throw new RuntimeException("注册失败");
        }

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
            throw new RuntimeException("识别主体创建异常");
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
        IPage<PortraitInputVo> page = getBaseMapper().queryPortraitInputInfo(para);
        return IdmResDTO.success(page);
    }

    /**
     * 查询平台信息
     * @param para
     * @return
     */
    public IdmResDTO<List<PlatFromDto>> queryPlatformInfo(PortraitInputVo para) {
        List<PlatfromInfoEntity> entityList = platfromInfoService.list();
        List<PlatFromDto> platFromDtos = BeanMapper.mapList(entityList, PlatFromDto.class);
        return IdmResDTO.success(platFromDtos);
    }

    /**
     * 更新平台信息
     * @param dto
     * @return
     */
    public IdmResDTO updatePortraitInputInfo(PlatFromDto dto) {
        PlatfromInfoEntity map = BeanMapper.map(dto, PlatfromInfoEntity.class);
        platfromInfoService.updateById(map);
        return IdmResDTO.success();
    }
}
