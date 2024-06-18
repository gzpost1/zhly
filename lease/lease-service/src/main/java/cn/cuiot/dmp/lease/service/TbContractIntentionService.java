package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.AuditConfigTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.common.bean.PageQuery;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.contract.AuditParam;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.mapper.TbContractIntentionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static cn.cuiot.dmp.common.constant.AuditConstant.*;
import static cn.cuiot.dmp.common.constant.AuditConstant.AUDIT_CONFIG_INTENTION_USELESS;

/**
 * 意向合同 服务实现类
 *
 * @author Mujun
 * @since 2024-06-12
 */
@Service
public class TbContractIntentionService extends BaseMybatisServiceImpl<TbContractIntentionMapper, TbContractIntentionEntity> {

    @Autowired
    TbContractIntentionBindInfoService bindInfoService;
    @Autowired
    SystemApiFeignService systemApiFeignService;

    @Override
    public List<TbContractIntentionEntity> list(TbContractIntentionEntity params) {
        List<TbContractIntentionEntity> list = super.list(params);
        list.forEach(c -> {
            fullInfo(c);
        });
        return list;
    }

    /**
     * 填充房屋信息,跟进人和合同人信息
     * @param c
     */
    private void fullInfo(TbContractIntentionEntity c) {
        fullBindHouseInfo(c);
    }

    @Override
    public PageResult<TbContractIntentionEntity> page(PageQuery param) {
        PageResult<TbContractIntentionEntity> page = super.page(param);
        page.getRecords().forEach(c -> {
            fullInfo(c);
        });
        return page;
    }

    @Override
    public TbContractIntentionEntity getById(Serializable id) {
        TbContractIntentionEntity intentionEntity = super.getById(id);
        fullBindHouseInfo(intentionEntity);
        return intentionEntity;
    }

    public TbContractIntentionEntity getByContractNo(String contractNo) {
        LambdaQueryWrapper<TbContractIntentionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractIntentionEntity::getContractNo, contractNo);
        queryWrapper.last("limit 1");
        TbContractIntentionEntity intentionEntity = baseMapper.selectOne(queryWrapper);
        fullBindHouseInfo(intentionEntity);
        return intentionEntity;
    }

    private void fullBindHouseInfo(TbContractIntentionEntity intentionEntity) {
        List<HousesArchivesVo> housesArchivesVos = bindInfoService.queryBindHouseInfoByContractId(intentionEntity.getId());
        if (Objects.nonNull(housesArchivesVos)) {
            intentionEntity.setHouseList(housesArchivesVos);
        }
    }


    /**
     * 获取审核后的合同状态
     *
     * @param param
     * @return
     */
    public TbContractIntentionEntity handleAuditContractStatus(AuditParam param) {
        TbContractIntentionEntity queryEntity = getById(param.getId());
        handleAuditStatus(queryEntity, param.getAuditStatus());
        handleContractStatus(queryEntity, param.getAuditStatus());
        return queryEntity;
    }

    /**
     * 根据审核通过与否 更改合同的审核状态
     *
     * @param entity
     * @param auditStatus 审核 2.审核通过 3.审核不通过
     */
    public void handleAuditStatus(TbContractIntentionEntity entity, Integer auditStatus) {
        //审核通过
        if (Objects.equals(auditStatus, ContractEnum.AUDIT_PASS.getCode())) {
            entity.setAuditStatus(ContractEnum.AUDIT_PASS.getCode());
        } else {
            entity.setAuditStatus(ContractEnum.AUDIT_REFUSE.getCode());
        }
    }

    /**
     * 根据审核通过与否 更改合同的合同状态
     *
     * @param auditStatus 审核 2.审核通过 3.审核不通过
     */
    public void handleContractStatus(TbContractIntentionEntity entity, Integer auditStatus) {
//        Integer auditType = param.getAuditType();
        Integer contractStatus = entity.getContractStatus();

        //签约,退定,作废成功 审核通过的按照审核状态修改合同状态为 已签约,已退定,已作废
        if (Objects.equals(auditStatus, ContractEnum.AUDIT_PASS.getCode())) {
            switch (ContractEnum.getEnumByCode(contractStatus)) {
                case STATUS_COMMITING:
                    handleContractStatusByDate(entity);
                    //草稿提交不审核的情况
                case STATUS_DARFT:
                    handleContractStatusByDate(entity);
                    //签约中
                case STATUS_SIGNING:
                    entity.setContractStatus(ContractEnum.STATUS_SIGNED.getCode());
                    entity.setCantractDate(LocalDate.now());
                    break;
                //退定中
                case STATUS_CANCELING:
                    entity.setContractStatus(ContractEnum.STATUS_CANCELLED.getCode());
                    break;
                //作废中
                case STATUS_USELESSING:
                    entity.setContractStatus(ContractEnum.STATUS_USELESS.getCode());
                    break;
            }
            //签约,退定,作废审核不通过的按照合同日期修改合同状态
        } else if (!Objects.equals(contractStatus, ContractEnum.STATUS_COMMITING.getCode())
                && Objects.equals(auditStatus, ContractEnum.AUDIT_REFUSE.getCode())) {
            handleContractStatusByDate(entity);
            //提交中 未通过的合同改为草稿
        } else if (Objects.equals(contractStatus, ContractEnum.STATUS_COMMITING.getCode())
                && Objects.equals(auditStatus, ContractEnum.AUDIT_REFUSE.getCode())) {
            entity.setContractStatus(ContractEnum.STATUS_DARFT.getCode());
        }
    }

    /**
     * 根据审核配置设置合同通过还是审核中
     *
     * @param entity
     * @param operate
     */
    public void handleAuditStatusByConfig(TbContractIntentionEntity entity, String operate) {

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
        entity.setContractStatus(contractStatus);
        boolean needAudit = needAudit(configName);

        //如果不需要审核,则调用审核通过改变合同状态,需要审核这保持中间状态等待审核
        if (needAudit) {
            auditStatus = ContractEnum.AUDIT_WAITING.getCode();
        } else {
            auditStatus = ContractEnum.AUDIT_PASS.getCode();
            handleContractStatus(entity, ContractEnum.AUDIT_PASS.getCode());
        }
        entity.setAuditStatus(auditStatus);

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

    /**
     * 根据合同日期设置合同状态
     */
    public void handleContractStatusByDate(TbContractIntentionEntity entity) {
        LocalDate nowDate = LocalDate.now();
        LocalDate beginDate = entity.getBeginDate();
        LocalDate endDate = entity.getEndDate();
        if (endDate.isBefore(nowDate)) {
            //已过期
            entity.setContractStatus(ContractEnum.STATUS_EXPIRED.getCode());
            //当前时间在合同日期中
        } else if (beginDate.isAfter(nowDate)) {
            //待执行
            entity.setContractStatus(ContractEnum.STATUS_WAITING.getCode());
        } else {
            //执行中
            entity.setContractStatus(ContractEnum.STATUS_EXECUTING.getCode());
        }
    }
}
