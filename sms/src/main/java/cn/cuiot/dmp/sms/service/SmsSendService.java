package cn.cuiot.dmp.sms.service;

import cn.cuiot.dmp.base.application.service.impl.ApiSystemServiceImpl;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.bean.external.SmsWocloudBO;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.FootPlateInfoEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.sms.entity.SmsSendRecordEntity;
import cn.cuiot.dmp.sms.entity.SmsSignEntity;
import cn.cuiot.dmp.sms.entity.SmsTemplateEntity;
import cn.cuiot.dmp.sms.enums.SmsThirdStatusEnum;
import cn.cuiot.dmp.sms.feign.ApiExternalapiService;
import cn.cuiot.dmp.sms.query.SmsSendQuery;
import cn.cuiot.dmp.sms.vendor.SmsApiFeignService;
import cn.cuiot.dmp.sms.vendor.req.SmsSendReq;
import cn.cuiot.dmp.sms.vendor.resp.SmsBaseResp;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

/**
 * 短信发送 业务层
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@Slf4j
@Service
public class SmsSendService {

    @Autowired
    private SmsApiFeignService smsApiFeignService;
    @Autowired
    private SmsSendRecordService sendRecordService;
    @Autowired
    private SmsTemplateService smsTemplateService;
    @Autowired
    private ApiSystemServiceImpl apiSystemService;
    @Autowired
    private ApiExternalapiService apiExternalapiService;
    @Autowired
    private SmsSignService signService;

    /**
     * 发送短信
     *
     * @Param query 参数
     */
    public void sendMsg(SmsSendQuery query) {
        log.info("短信发送接收参数..............{}", JsonUtil.writeValueAsString(query));

        if (Objects.isNull(query.getCompanyId())) {
            throw new BusinessException(ResultCode.ERROR, "短信发送失败，所属企业不能为空");
        }
        if (Objects.isNull(query.getStdTemplate())) {
            throw new BusinessException(ResultCode.ERROR, "短信发送失败，标准短信模板不能为空");
        }
        if (StringUtils.isBlank(query.getMobile())) {
            throw new BusinessException(ResultCode.ERROR, "短信发送失败，手机号不能为空");
        }
        if (CollectionUtils.isEmpty(query.getParams())) {
            throw new BusinessException(ResultCode.ERROR, "短信发送失败，短信参数不能为空");
        }

        // 平台企业id
        Long platformCompanyId = 1L;
        if (!Objects.equals(query.getCompanyId(), platformCompanyId)) {
            // 校验企业是是否启用发送短信
            checkPlatformStatus(query.getCompanyId());
        }

        // 获取短信模板
        SmsTemplateEntity redisTemplate = smsTemplateService.getRedisTemplate(query.getStdTemplate());
        if (Objects.isNull(redisTemplate) || !Objects.equals(redisTemplate.getThirdStatus(), SmsThirdStatusEnum.SUCCESS_AUDIT.getCode())) {
            throw new BusinessException(ResultCode.ERROR, "短信发送失败，模板不存在");
        }

        // 获取短信签名
        SmsSignEntity redisSign = signService.getRedisSign(query.getCompanyId(), platformCompanyId);
        if (Objects.isNull(redisSign) || !Objects.equals(redisSign.getThirdStatus(), SmsThirdStatusEnum.SUCCESS_AUDIT.getCode()) || Objects.equals(redisSign.getStatus(), EntityConstants.DISABLED)) {
            throw new BusinessException(ResultCode.ERROR, "短信发送失败，签名不存在");
        }

        // 构造短信内容
        String content = fillTemplate(redisTemplate.getContent(), query.getParams());

        // 获取部门编码路径
        DepartmentDto departmentDto = apiSystemService.lookUpDepartmentInfo(null, null, query.getCompanyId());

        try {
            SmsSendReq req = new SmsSendReq();
            req.setMobile(query.getMobile());
            req.setContent(content);
            req.setTemplateId(redisTemplate.getThirdTemplate() + "");
            req.setSignName(redisSign.getSign());
            SmsBaseResp<String> resp = smsApiFeignService.send(req);

            SmsSendRecordEntity record = new SmsSendRecordEntity();
            record.setId(IdWorker.getId());
            record.setCompanyId(query.getCompanyId());
            record.setTaskId(resp.getData());
            record.setPhone(query.getMobile());
            record.setSmsType(redisTemplate.getSmsType());
            record.setThirdTemplate(redisTemplate.getThirdTemplate());
            record.setSign(redisSign.getSign());
            record.setContent(content);
            record.setCreateTime(new Date());
            record.setContentCharging(contentChargingCompute(record.getContent()));
            if (Objects.nonNull(departmentDto)) {
                record.setDeptPath(departmentDto.getPath());
            }
            sendRecordService.create(record);

        } catch (Exception e) {
            log.info("发送选项异常.........");
            e.printStackTrace();
            throw new BusinessException(ResultCode.ERROR);
        }
    }

    /**
     * 内容计费计算
     *
     * @return 内容计费（条）
     * @Param content 内容
     */
    private static Integer contentChargingCompute(String content) {
        if (StringUtils.isBlank(content)) {
            return 0;
        }
        int length = 70;
        double result = (double) content.length() / length;
        return (int) Math.ceil(result);
    }

    /**
     * 填充短信模板
     *
     * @param template 短信模板
     * @param params   参数列表
     * @return 填充后的短信
     */
    public static String fillTemplate(String template, List<String> params) {
        // 定义正则表达式匹配占位符 {sX}
        Pattern pattern = compile("\\{s(\\d+)}");
        Matcher matcher = pattern.matcher(template);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            // 获取占位符的编号 X
            int index = Integer.parseInt(matcher.group(1)) - 1;

            // 获取相应编号的参数，超出范围则返回空字符串
            String replacement = (index < params.size()) ? params.get(index) : "";

            // 将占位符替换为对应参数
            matcher.appendReplacement(result, replacement);
        }

        // 将剩余部分追加到结果中
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * 校验企业对接配置的短信启停用状态
     *
     * @Param companyId 企业id
     */
    private void checkPlatformStatus(Long companyId) {
        PlatfromInfoReqDTO dto = new PlatfromInfoReqDTO();
        dto.setCompanyId(companyId);
        PlatfromInfoRespDTO smsRedis = apiExternalapiService.queryPlatfromSmsRedis(dto);

        if (Objects.isNull(smsRedis) || StringUtils.isBlank(smsRedis.getData())) {
            throw new BusinessException(ResultCode.ERROR, "获取平台对接系统选项异常");
        }

        SmsWocloudBO bo = FootPlateInfoEnum.getObjectFromJsonById(FootPlateInfoEnum.SMS_WOCLOUD.getId(), smsRedis.getData());

        if (Objects.isNull(bo.getStatus())) {
            throw new BusinessException(ResultCode.ERROR, "企业【" + companyId + "】请求短信失败，status为空");
        }

        if (Objects.equals(bo.getStatus(), EntityConstants.DISABLED)) {
            throw new BusinessException(ResultCode.ERROR, "企业【" + companyId + "】请求短信失败，未启用发送短信配置");
        }
    }
}
