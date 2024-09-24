package cn.cuiot.dmp.externalapi.provider.controller.sms;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.service.impl.ApiSystemServiceImpl;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.OrganizationReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.OrganizationRespDTO;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.sms.entity.SmsSignEntity;
import cn.cuiot.dmp.sms.entity.SmsTemplateEntity;
import cn.cuiot.dmp.sms.query.*;
import cn.cuiot.dmp.sms.service.*;
import cn.cuiot.dmp.sms.vendor.SmsApiFeignService;
import cn.cuiot.dmp.sms.vendor.resp.SmsBaseResp;
import cn.cuiot.dmp.sms.vo.SmsSendRecordVO;
import cn.cuiot.dmp.sms.vo.SmsStatisticsVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 短信管理
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsApiFeignService smsApiFeignService;
    @Autowired
    private SmsTemplateService smsTemplateService;
    @Autowired
    private SmsSignService signService;
    @Autowired
    private SmsSendRecordService sendRecordService;
    @Autowired
    private SmsSignRelationService signRelationService;
    @Autowired
    private ApiSystemServiceImpl apiSystemService;
    @Autowired
    private SmsConfigService smsConfigService;

    /**
     * 基础功能-短信管理-短信配置
     *
     * @Param 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "saveConfig", operationName = "保存短信配置", serviceType = "sms", serviceTypeName = "短信管理")
    @PostMapping("/saveConfig")
    public IdmResDTO<?> saveConfig(@RequestBody @Valid SmsConfigDto dto) {
        smsConfigService.saveConfig(dto);
        return IdmResDTO.success();
    }

    /**
     * 基础功能-短信管理-短信统计-剩余短信
     *
     * @Param param 数据id
     */
    @RequiresPermissions
    @PostMapping("/getBalance")
    public IdmResDTO<Integer> getBalance() {
        SmsBaseResp<Integer> resp = smsApiFeignService.getBalance();
        if (Objects.isNull(resp) || !Objects.equals(resp.getCode(), EntityConstants.NO.intValue())) {
            log.error("resp：" + JsonUtil.writeValueAsString(resp));
            throw new BusinessException(ResultCode.ERROR, "请求第三方短信余额异常");
        }
        return IdmResDTO.success(resp.getData());
    }

    /**
     * 基础功能-短信管理-短信统计-短信共使用
     *
     * @Param param 数据id
     */
    @RequiresPermissions
    @PostMapping("/statisticsUsed")
    public IdmResDTO<Long> statisticsUsed(@RequestBody SmsStatisticsQuery query) {
        return IdmResDTO.success(sendRecordService.statisticsUsed(query));
    }

    /**
     * 基础功能-短信管理-短信统计-企业短信使用列表
     *
     * @Param param 数据id
     */
    @RequiresPermissions
    @PostMapping("/statisticsPage")
    public IdmResDTO<IPage<SmsStatisticsVO>> statisticsPage(@RequestBody SmsStatisticsQuery query) {
        return IdmResDTO.success(sendRecordService.statisticsPage(query));
    }

    /**
     * 基础功能-短信管理-短信模板-分页
     *
     * @return IPage
     * @Param query 参数
     */
    @RequiresPermissions
    @PostMapping("/queryTemplateForPage")
    public IdmResDTO<IPage<SmsTemplateEntity>> queryTemplateForPage(@RequestBody SmsTemplatePageQuery query) {
        return IdmResDTO.success(smsTemplateService.queryForPage(query));
    }

    /**
     * 基础功能-短信管理-短信模板-创建模板
     *
     * @Param dto 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "createTemplate", operationName = "创建短信模板", serviceType = "sms", serviceTypeName = "短信管理")
    @PostMapping("/createTemplate")
    public IdmResDTO<?> createTemplate(@RequestBody @Valid SmsTemplateCreateDto dto) {
        smsTemplateService.create(dto);
        return IdmResDTO.success();
    }

    /**
     * 基础功能-短信管理-短信模板-删除模板
     *
     * @Param param 数据id
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteTemplate", operationName = "删除短信模板", serviceType = "sms", serviceTypeName = "短信管理")
    @PostMapping("/deleteTemplate")
    public IdmResDTO<?> deleteTemplate(@RequestBody @Valid IdParam param) {
        smsTemplateService.delete(param.getId());
        return IdmResDTO.success();
    }

    /**
     * 基础功能-短信管理-签名分页
     *
     * @Param param 参数
     */
    @RequiresPermissions
    @PostMapping("/querySignForPage")
    public IdmResDTO<IPage<SmsSignEntity>> querySignForPage(@RequestBody PageQuery query) {
        return IdmResDTO.success(signService.queryForPage(query));
    }

    /**
     * 基础功能-短信管理-签名创建
     *
     * @Param param 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "createSign", operationName = "创建短信签名", serviceType = "sms", serviceTypeName = "短信管理")
    @PostMapping("/createSign")
    public IdmResDTO<?> createSign(@RequestBody @Valid SmsSignCreateDto dto) {
        signService.create(dto);
        return IdmResDTO.success();
    }

    /**
     * 基础功能-短信管理-签名启停用
     *
     * @Param param 参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "signUpdateStatus", operationName = "短信签名启停用", serviceType = "sms", serviceTypeName = "短信管理")
    @PostMapping("/signUpdateStatus")
    public IdmResDTO<?> signUpdateStatus(@RequestBody @Valid UpdateStatusParam param) {
        signService.updateStatus(param);
        return IdmResDTO.success();
    }

    /**
     * 基础功能-短信管理-签名删除
     *
     * @Param param 数据id
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteSign", operationName = "删除短信签名", serviceType = "sms", serviceTypeName = "短信管理")
    @PostMapping("/deleteSign")
    public IdmResDTO<?> deleteSign(@RequestBody @Valid IdParam param) {
        signService.delete(param.getId());
        return IdmResDTO.success();
    }

    /**
     * 基础功能-短信管理-发送签名设置
     *
     * @Param param 数据id
     */
    @RequiresPermissions
    @LogRecord(operationCode = "sendSignSet", operationName = "发送签名设置", serviceType = "sms", serviceTypeName = "短信管理")
    @PostMapping("/sendSignSet")
    public IdmResDTO<?> sendSignSet(@RequestBody @Valid SmsSendSignSetQuery query) {
        signRelationService.sendSignSet(query);
        return IdmResDTO.success();
    }

    /**
     * 基础功能-短信管理-发送记录
     *
     * @return IPage
     * @Param pageQuery 参数
     */
    @RequiresPermissions
    @PostMapping("/sendRecord")
    public IdmResDTO<IPage<SmsSendRecordVO>> sendRecord(SmsSendRecordPageQuery pageQuery) {
        return IdmResDTO.success(sendRecordService.sendRecord(pageQuery));
    }

    /**
     * 基础功能-短信管理-根据企业名称获取企业id
     *
     * @return IPage
     * @Param pageQuery 参数
     */
    @RequiresPermissions
    @PostMapping("/queryCompanyIdByName")
    public IdmResDTO<List<OrganizationRespDTO>> queryCompanyIdByName(@RequestBody @Valid SmsStatisticsCompanyQuery query) {
        OrganizationReqDTO reqDTO = new OrganizationReqDTO();
        reqDTO.setCompanyName(query.getCompanyName());
        return IdmResDTO.success(apiSystemService.queryOrganizationList(reqDTO));
    }
}
