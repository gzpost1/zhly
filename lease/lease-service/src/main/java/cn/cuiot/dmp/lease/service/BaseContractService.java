package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.base.infrastructure.dto.req.AuditConfigTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.contract.AuditParam;
import cn.cuiot.dmp.lease.entity.BaseContractEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.mapper.TbContractLeaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static cn.cuiot.dmp.common.constant.AuditConstant.*;
import static cn.cuiot.dmp.common.constant.AuditConstant.AUDIT_CONFIG_INTENTION_USELESS;

/**
 * 租赁合同 服务实现类
 *
 * @author Mujun
 * @since 2024-06-19
 */
@Service
public class BaseContractService  {
    @Autowired
    SystemApiFeignService systemApiFeignService;


    /**
     * 获取审核后的合同状态
     *
     * @param param
     * @return
     */
    public BaseContractEntity handleAuditContractStatus(BaseContractEntity entity, AuditParam param) {
        Integer auditStatus = handleAuditStatus(param.getAuditStatus());
        Integer contractStatus = handleContractStatus(entity.getContractStatus(), param.getAuditStatus(),
                entity.getBeginDate(), entity.getEndDate());
        entity.setAuditStatus(auditStatus);
        entity.setContractStatus(contractStatus);
        return entity;
    }

    /**
     * 根据审核通过与否 更改合同的审核状态
     *
     * @param auditStatus 审核 2.审核通过 3.审核不通过
     */
    public Integer handleAuditStatus(Integer auditStatus) {

        //审核通过
        if (Objects.equals(auditStatus, ContractEnum.AUDIT_PASS.getCode())) {
            auditStatus = ContractEnum.AUDIT_PASS.getCode();
        } else {
            auditStatus = ContractEnum.AUDIT_REFUSE.getCode();
        }
        return auditStatus;
    }

    /**
     * 根据审核配置设置合同通过还是审核中
     *
     * @param entity
     * @param operate
     */
    public void handleAuditStatusByConfig(BaseContractEntity entity, String operate) {

        Integer auditStatus;
        Integer contractStatus = null;
        String configName = null;
        //根据操作类型设置合同的中间状态
        switch (operate) {
            case OPERATE_COMMIT:
                configName = AUDIT_CONFIG_INTENTION_NEW;
                contractStatus = ContractEnum.STATUS_COMMITING.getCode();
                break;
            case OPERATE_SIGN_CONTRACT:
                configName = AUDIT_CONFIG_INTENTION_SIGN;
                contractStatus = ContractEnum.STATUS_SIGNING.getCode();
                break;
            case OPERATE_CANCEL:
                configName = AUDIT_CONFIG_INTENTION_CANCEL;
                contractStatus = ContractEnum.STATUS_CANCELING.getCode();
                break;
            case OPERATE_USELESS:
                configName = AUDIT_CONFIG_INTENTION_USELESS;
                contractStatus = ContractEnum.STATUS_USELESSING.getCode();
                break;
        }
        boolean needAudit = needAudit(configName);

        //如果不需要审核,则调用审核通过改变合同状态,需要审核这保持中间状态等待审核
        if (needAudit) {
            auditStatus = ContractEnum.AUDIT_WAITING.getCode();
        } else {
            //如果不需要审核,默认走通过合同流程
            auditStatus = ContractEnum.AUDIT_PASS.getCode();
            contractStatus = handleContractStatus(contractStatus, ContractEnum.AUDIT_PASS.getCode(), entity.getBeginDate(), entity.getEndDate());
        }
        entity.setAuditStatus(auditStatus);
        entity.setContractStatus(contractStatus);
    }

    /**
     * 根据审核通过与否 更改合同的合同状态
     *
     * @param auditStatus 审核 2.审核通过 3.审核不通过
     */
    public Integer handleContractStatus(Integer contractStatus, Integer auditStatus, LocalDate beginDate, LocalDate endDate) {

        //签约,退定,作废成功 审核通过的按照审核状态修改合同状态为 已签约,已退定,已作废
        if (Objects.equals(auditStatus, ContractEnum.AUDIT_PASS.getCode())) {
            switch (ContractEnum.getEnumByCode(contractStatus)) {
                case STATUS_COMMITING:
                    contractStatus = handleContractStatusByDate(beginDate, endDate);
                    break;
                //草稿提交不审核的情况
                case STATUS_DARFT:
                    contractStatus = handleContractStatusByDate(beginDate, endDate);
                    break;
                //签约中
                case STATUS_SIGNING:
                    contractStatus = ContractEnum.STATUS_SIGNED.getCode();
                    break;
                //退定中
                case STATUS_CANCELING:
                    contractStatus = ContractEnum.STATUS_CANCELLED.getCode();
                    break;
                //作废中
                case STATUS_USELESSING:
                    contractStatus = ContractEnum.STATUS_USELESS.getCode();
                    break;
            }
            //签约,退定,作废审核不通过的按照合同日期修改合同状态
        } else if (!Objects.equals(contractStatus, ContractEnum.STATUS_COMMITING.getCode())
                && Objects.equals(auditStatus, ContractEnum.AUDIT_REFUSE.getCode())) {
            contractStatus = handleContractStatusByDate(beginDate, endDate);
            handleContractStatusByDate(beginDate, endDate);
            //提交中 未通过的合同改为草稿
        } else if (Objects.equals(contractStatus, ContractEnum.STATUS_COMMITING.getCode())
                && Objects.equals(auditStatus, ContractEnum.AUDIT_REFUSE.getCode())) {
            contractStatus = ContractEnum.STATUS_DARFT.getCode();
        }
        return contractStatus;
    }

    /**
     * 根据合同日期设置合同状态
     */
    public Integer handleContractStatusByDate(LocalDate beginDate, LocalDate endDate) {
        LocalDate nowDate = LocalDate.now();
        Integer contractStatus;
        if (endDate.isBefore(nowDate)) {
            //已过期
            contractStatus = ContractEnum.STATUS_EXPIRED.getCode();
        } else if (beginDate.isAfter(nowDate)) {
            //待执行
            contractStatus = ContractEnum.STATUS_WAITING.getCode();
        } else {
            //当前时间在合同日期中 执行中
            contractStatus = ContractEnum.STATUS_EXECUTING.getCode();
        }
        return contractStatus;
    }

    public boolean needAudit(String name) {
        AuditConfigRspDTO auditConfig = getAuditConfig(AuditConfigTypeEnum.INTENTION_CONTRACT.getCode(), name);
        Byte status = auditConfig.getStatus();
        if (status == 1) {
            return true;
        }
        return false;
    }


    public AuditConfigRspDTO getAuditConfig(Byte type, String name) {
        AuditConfigTypeReqDTO auditConfigTypeReqDTO = new AuditConfigTypeReqDTO();
        auditConfigTypeReqDTO.setAuditConfigType(type);
        auditConfigTypeReqDTO.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        auditConfigTypeReqDTO.setName(name);
        IdmResDTO<List<AuditConfigTypeRspDTO>> idmResDTO = systemApiFeignService.lookUpAuditConfig(auditConfigTypeReqDTO);
        if (Objects.nonNull(idmResDTO) && ResultCode.SUCCESS.getCode()
                .equals(idmResDTO.getCode())) {
            List<AuditConfigTypeRspDTO> data = idmResDTO.getData();
            if (CollectionUtils.isNotEmpty(data)) {
                AuditConfigTypeRspDTO auditConfigTypeRspDTO = data.get(0);
                List<AuditConfigRspDTO> auditConfigList = auditConfigTypeRspDTO.getAuditConfigList();
                if (CollectionUtils.isNotEmpty(auditConfigList)) {
                    return auditConfigList.get(0);
                }
            }
        }
        return null;
    }
}
