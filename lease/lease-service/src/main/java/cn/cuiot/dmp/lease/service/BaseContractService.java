package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.base.infrastructure.dto.req.AuditConfigTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.contract.AuditParam;
import cn.cuiot.dmp.lease.entity.BaseContractEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseRelateEntity;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static cn.cuiot.dmp.common.constant.AuditContractConstant.*;
import static cn.cuiot.dmp.common.constant.AuditContractConstant.AUDIT_CONFIG_INTENTION_USELESS;

/**
 * 租赁合同 服务实现类
 *
 * @author Mujun
 * @since 2024-06-19
 */
@Service
public class BaseContractService {
    //签约 关联合同 意向合同
    public static final int RELATE_INTENTION_TYPE = 1;
    //续租 关联合同 新生成续租合同的关联 续租
    public static final int RELATE_NEW_LEASE_TYPE = 2;
    //续租 关联合同 续租合同的原合同关联
    public static final int RELATE_ORI_LEASE_TYPE = 3;
    @Autowired
    SystemApiFeignService systemApiFeignService;
    @Autowired
    TbContractBindInfoService bindInfoService;
    @Autowired
    TbContractLeaseRelateService leaseRelateService;
    @Autowired
    TbContractLeaseService contractLeaseService;


    /**
     * 填充房屋信息
     *
     * @param contractEntity
     */
    public void fillBindHouseInfo(BaseContractEntity contractEntity) {
        if (Objects.isNull(contractEntity)) {
            return;
        }
        Integer type = CONTRACT_INTENTION_TYPE;
        if (contractEntity instanceof TbContractLeaseEntity) {
            type = CONTRACT_LEASE_TYPE;
        }
        List<HousesArchivesVo> housesArchivesVos = bindInfoService.queryBindHouseInfoByContractId(contractEntity.getId(), type);
        if (Objects.nonNull(housesArchivesVos)) {
            contractEntity.setHouseList(housesArchivesVos);
        }
    }

    /**
     * 获取审核后的合同状态
     *
     * @param param
     * @return
     */
    public BaseContractEntity handleAuditContractStatus(BaseContractEntity entity, AuditParam param) {
        Integer oriContractStatus = entity.getContractStatus();
        Integer auditStatus = handleAuditStatus(param.getAuditStatus());
        int type = CONTRACT_INTENTION_TYPE;
        //租赁合同
        if (entity instanceof TbContractLeaseEntity) {
            type = CONTRACT_LEASE_TYPE;
            //意向合同
        } else if (entity instanceof TbContractIntentionEntity) {
            type = CONTRACT_INTENTION_TYPE;
        }
        Integer contractStatus = handleContractStatus(entity, entity.getContractStatus(), param.getAuditStatus(), type,
                entity.getBeginDate(), entity.getEndDate());
        entity.setAuditStatus(auditStatus);
        entity.setContractStatus(contractStatus);

//        //签约失败
//        if (Objects.equals(auditStatus, ContractEnum.AUDIT_REFUSE) &&
//                Objects.equals(oriContractStatus, ContractEnum.STATUS_SIGNING)) {
//            TbContractIntentionEntity intentionEntity = (TbContractIntentionEntity) entity;
//            intentionEntity.setContractLeaseId(null);
//            //同时删除租赁合同的关联合同
//            leaseRelateService.removeRelated(intentionEntity.getContractLeaseId(), RELATE_INTENTION_TYPE);
//            //续约失败
//        } else if (Objects.equals(auditStatus, ContractEnum.AUDIT_REFUSE) && Objects.equals(oriContractStatus, ContractEnum.STATUS_RELETING)) {
//            TbContractLeaseEntity leaseEntity = (TbContractLeaseEntity) entity;
//            contractLeaseService.cancelReletBind(leaseEntity.getId());
//            leaseRelateService.removeRelated(leaseEntity.getId(),RELATE_NEW_LEASE_TYPE);
//            leaseRelateService.removeRelated(leaseEntity.getReletContractId(),RELATE_ORI_LEASE_TYPE);
//        }
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
        String configIntentionName = null;
        String configLeaseName = null;
        //根据操作类型设置合同的中间状态
        switch (operate) {
            case OPERATE_COMMIT:
                configIntentionName = AUDIT_CONFIG_INTENTION_NEW;
                configLeaseName = AUDIT_CONFIG_LEASE_NEW;
                contractStatus = ContractEnum.STATUS_COMMITING.getCode();
                break;
            case OPERATE_SIGN_CONTRACT:
                configIntentionName = AUDIT_CONFIG_INTENTION_SIGN;
                contractStatus = ContractEnum.STATUS_SIGNING.getCode();
                break;
            case OPERATE_CANCEL:
                configIntentionName = AUDIT_CONFIG_INTENTION_CANCEL;
                contractStatus = ContractEnum.STATUS_CANCELING.getCode();
                break;
            case OPERATE_USELESS:
                if (entity instanceof TbContractIntentionEntity) {
                    configIntentionName = AUDIT_CONFIG_INTENTION_USELESS;
                } else {
                    configIntentionName = AUDIT_CONFIG_LEASE_USELESS;
                }
                contractStatus = ContractEnum.STATUS_USELESSING.getCode();
                break;
            case OPERATE_LEASE_BACK:
                configLeaseName = AUDIT_CONFIG_LEASE_BACK;
                contractStatus = ContractEnum.STATUS_BACKING_LEASE.getCode();
                break;
            case OPERATE_CHANGE:
                configLeaseName = AUDIT_CONFIG_LEASE_CHANGE;
                contractStatus = ContractEnum.STATUS_CHANGING.getCode();
                break;
            case OPERATE_LEASE_RELET:
                configLeaseName = AUDIT_CONFIG_LEASE_RELET;
                contractStatus = ContractEnum.STATUS_RELETING.getCode();
                break;
        }
        boolean needAudit = true;
        int type = CONTRACT_INTENTION_TYPE;
        //租赁合同
        if (entity instanceof TbContractLeaseEntity) {
            needAudit = needAudit(configLeaseName, AuditConfigTypeEnum.LEASE_CONTRACT.getCode());
            type = CONTRACT_LEASE_TYPE;
            //意向合同
        } else if (entity instanceof TbContractIntentionEntity) {
            needAudit = needAudit(configIntentionName, AuditConfigTypeEnum.INTENTION_CONTRACT.getCode());
            type = CONTRACT_INTENTION_TYPE;
        }

        //如果不需要审核,则调用审核通过改变合同状态,需要审核这保持中间状态等待审核
        if (needAudit) {
            auditStatus = ContractEnum.AUDIT_WAITING.getCode();
        } else {
            //如果不需要审核,默认走通过合同流程
            auditStatus = ContractEnum.AUDIT_PASS.getCode();
            contractStatus = handleContractStatus(entity, contractStatus, ContractEnum.AUDIT_PASS.getCode(), type, entity.getBeginDate(), entity.getEndDate());
        }
        entity.setAuditStatus(auditStatus);
        entity.setContractStatus(contractStatus);
    }



    /**
     * 根据审核通过与否 更改合同的合同状态
     *
     * @param auditStatus 审核 2.审核通过 3.审核不通过
     * @param type        合同类型 1.意向合同 2.租赁合同
     */
    public Integer handleContractStatus(BaseContractEntity entity, Integer contractStatus, Integer auditStatus, Integer type, LocalDate beginDate, LocalDate endDate) {

        //审核通过
        if (Objects.equals(auditStatus, ContractEnum.AUDIT_PASS.getCode())) {
            switch (ContractEnum.getEnumByCode(contractStatus)) {
                case STATUS_COMMITING:
                    contractStatus = handleContractStatusByDate(type, beginDate, endDate);
                    break;
                //草稿提交不审核的情况
                case STATUS_DARFT:
                    contractStatus = handleContractStatusByDate(type, beginDate, endDate);
                    break;
                //签约中
                case STATUS_SIGNING:
                    TbContractIntentionEntity intentionEntity = (TbContractIntentionEntity) entity;
                    leaseRelateService.enableRelate(intentionEntity.getContractLeaseId(), RELATE_INTENTION_TYPE, intentionEntity.getId());
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
                //变更中
                case STATUS_CHANGING:
                    contractStatus = handleContractStatusByDate(type, beginDate, endDate);
                    break;
                //退租中
                case STATUS_BACKING_LEASE:
                    contractStatus = ContractEnum.STATUS_END.getCode();
                    break;
                //续租中
                case STATUS_RELETING:
                    TbContractLeaseEntity leaseEntity = (TbContractLeaseEntity) entity;
                    leaseRelateService.enableRelate(leaseEntity.getId(),RELATE_NEW_LEASE_TYPE,leaseEntity.getReletContractId());
                    leaseRelateService.enableRelate(leaseEntity.getReletContractId(),RELATE_ORI_LEASE_TYPE,leaseEntity.getId());
                    contractStatus = ContractEnum.STATUS_RELET.getCode();
                    break;
            }
            //   审核失败
        } else if (Objects.equals(auditStatus, ContractEnum.AUDIT_REFUSE.getCode())) {
            switch (ContractEnum.getEnumByCode(contractStatus)) {
                case STATUS_COMMITING:
                    contractStatus = ContractEnum.STATUS_DARFT.getCode();
                    break;
                //除了提交中被拒绝改为草稿,其余都是走时间判断状态
                case STATUS_SIGNING:
                    TbContractIntentionEntity intentionEntity = (TbContractIntentionEntity) entity;
                    intentionEntity.setContractLeaseId(null);
                    //同时删除租赁合同的关联合同
                    leaseRelateService.removeRelated(intentionEntity.getContractLeaseId(), RELATE_INTENTION_TYPE);
                    contractStatus = handleContractStatusByDate(type, beginDate, endDate);
                    break;
                case STATUS_RELETING:
                    TbContractLeaseEntity leaseEntity = (TbContractLeaseEntity) entity;
                    contractLeaseService.cancelReletBind(leaseEntity.getId());
                    leaseRelateService.removeRelated(leaseEntity.getId(),RELATE_NEW_LEASE_TYPE);
                    leaseRelateService.removeRelated(leaseEntity.getReletContractId(),RELATE_ORI_LEASE_TYPE);
                    contractStatus = handleContractStatusByDate(type, beginDate, endDate);
                    break;
                default:
                    contractStatus = handleContractStatusByDate(type, beginDate, endDate);
                    break;
            }
        }
        return contractStatus;
    }

    /**
     * 根据合同日期设置合同状态
     */
    public Integer handleContractStatusByDate(Integer type, LocalDate beginDate, LocalDate endDate) {
        LocalDate nowDate = LocalDate.now();
        Integer contractStatus;
        if (endDate.isBefore(nowDate)) {
            //已过期
            if (Objects.equals(type, CONTRACT_INTENTION_TYPE)) {
                contractStatus = ContractEnum.STATUS_EXPIRED.getCode();
            } else {
                contractStatus = ContractEnum.STATUS_EXPIRED_WAITING.getCode();
            }
        } else if (beginDate.isAfter(nowDate)) {
            //待执行
            contractStatus = ContractEnum.STATUS_WAITING.getCode();
        } else {
            //当前时间在合同日期中 执行中
            contractStatus = ContractEnum.STATUS_EXECUTING.getCode();
        }
        return contractStatus;
    }

    public boolean needAudit(String name, Byte type) {
        AuditConfigRspDTO auditConfig = getAuditConfig(type, name);
        Byte status = auditConfig.getStatus();
        if (status == 1) {
            return true;
        }
        return false;
    }

    /**
     * 根据审核配置判断是否需要审核
     *
     * @param type
     * @param name
     * @return
     */
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
