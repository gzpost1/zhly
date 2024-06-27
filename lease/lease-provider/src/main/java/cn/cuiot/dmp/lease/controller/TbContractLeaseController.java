package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.base.application.enums.BeanValidationGroup;
import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.lease.dto.contract.*;
import cn.cuiot.dmp.lease.entity.TbContractCancelEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseBackEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.service.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Objects;
import java.util.Optional;

import static cn.cuiot.dmp.common.constant.AuditConstant.*;

/**
 * 租赁合同
 *
 * @author Mujun
 * @since 2024-06-19
 */
@Slf4j
@RestController
@RequestMapping("/contractLease")
public class TbContractLeaseController extends BaseCurdController<TbContractLeaseService, TbContractLeaseEntity, TbContractLeaseParam> {

    @Autowired
    TbContractLogService contractLogService;
    @Autowired
    TbContractCancelService contractCancelService;
    @Autowired
    TbContractBindInfoService bindInfoService;
    @Autowired
    TbContractIntentionService contractIntentionService;
    @Autowired
    BaseContractService baseContractService;
    @Autowired
    TbContractLeaseBackService leaseBackService;

    /**
     * 保存草稿
     *
     * @param entity
     * @return
     */
    @RequiresPermissions
    @PostMapping("/saveDarft")
    public boolean saveDarft(@RequestBody @Valid TbContractLeaseEntity entity) {
        Long id = SnowflakeIdWorkerUtil.nextId();
        entity.setId(id);
        entity.setAuditStatus(ContractEnum.AUDIT_WAITING_COMMIT.getCode());
        entity.setContractStatus(ContractEnum.STATUS_DARFT.getCode());
        contractLogService.saveLeaseLog(id, OPERATE_NEW, "新增了租赁合同");
        return service.save(entity);
    }

    /**
     * 提交
     *
     * @param entity
     * @return
     */
    @RequiresPermissions
    @PostMapping("/commit")
    public boolean commit(@RequestBody @Valid TbContractLeaseEntity entity) {

        Long id = Optional.ofNullable(entity.getId()).orElse(SnowflakeIdWorkerUtil.nextId());
        entity.setId(id);
        //需要审核
        baseContractService.handleAuditStatusByConfig(entity, OPERATE_COMMIT);
        contractLogService.saveLeaseLog(id, OPERATE_COMMIT, "提交了租赁合同");
        return service.saveOrUpdate(entity);
    }

    /**
     * 变更
     *
     * @param entity
     * @return
     */
    @Override
    public boolean update(@RequestBody @Validated(BeanValidationGroup.Update.class) TbContractLeaseEntity entity) {
        Long id = entity.getId();
        baseContractService.handleAuditStatusByConfig(entity, OPERATE_CHANGE);
        contractLogService.saveLeaseLog(id, OPERATE_CHANGE, "变更了租赁合同");
        return service.updateById(entity);
    }

    /**
     * 退租
     *
     * @param param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/leaseBack")
    public boolean leaseBack(@RequestBody @Valid ContractParam param) {
        Long id = param.getId();
        TbContractLeaseBackEntity contractLeaseBackEntity = param.getContractLeaseBackEntity();
        AssertUtil.notNull(id, "合同id不能为空");
        AssertUtil.notNull(contractLeaseBackEntity, "退租信息不能为空");
        TbContractLeaseEntity queryEntity = getContract(id);
        baseContractService.handleAuditStatusByConfig(queryEntity, OPERATE_LEASE_BACK);
        Long leaseBackId = SnowflakeIdWorkerUtil.nextId();
        contractLeaseBackEntity.setContractId(id);
        contractLeaseBackEntity.setId(leaseBackId);
        leaseBackService.saveOrUpdate(contractLeaseBackEntity);
        String operMsg = "退租了租赁合同" + System.lineSeparator() + "退租说明:" + contractLeaseBackEntity.getRemark();
        contractLogService.saveLog(id, OPERATE_LEASE_BACK, TbContractLogService.TYPE_LEASE, operMsg, String.valueOf(leaseBackId), null);
        return service.updateById(queryEntity);
    }

    private TbContractLeaseEntity getContract(Long id) {
        TbContractLeaseEntity contract = service.getById(id);
        AssertUtil.notNull(contract, "合同不存在!");
        return contract;
    }

    /**
     * 续租
     *
     * @param param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/leaseRelet")
    public boolean leaseRelet(@RequestBody @Valid ContractReletParam param) {
        Long id = param.getId();
        TbContractLeaseEntity queryEntity = getContract(id);
        TbContractLeaseEntity contractLeaseReletEntity = getContractLeaseEntity(param);
        service.save(contractLeaseReletEntity);

        baseContractService.handleAuditStatusByConfig(queryEntity, OPERATE_LEASE_RELET);
        String operMsg = "续租了租赁合同" + System.lineSeparator() + "续租新生成的合同编码:" + contractLeaseReletEntity.getContractNo() + System.lineSeparator()
                + "续租说明:" + param.getContractLeaseReletEntity().getReletRemark();
        contractLogService.saveLog(id, OPERATE_LEASE_RELET, TbContractLogService.TYPE_LEASE, operMsg,
                String.valueOf(contractLeaseReletEntity.getId()), null);
        return service.updateById(queryEntity);
    }

    /**
     * 分配
     *
     * @param param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/allocation")
    public boolean allocation(@RequestBody @Valid AllocationParam param) {
        Long id = param.getId();
        TbContractLeaseEntity queryEntity = getContract(id);
        queryEntity.setFollowUp(String.valueOf(param.getFollowUpId()));
        String operMsg = "分配了租赁合同,分配给" + param.getFollowUpName();
        contractLogService.saveLeaseLog(id, OPERATE_ALLOCATION, operMsg);
        return service.saveOrUpdate(queryEntity);
    }

    /**
     * 打标签
     *
     * @param param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/label")
    public boolean label(@RequestBody @Valid LabelParam param) {
        Long id = param.getId();
        TbContractLeaseEntity queryEntity = getContract(id);
        queryEntity.setLabel(param.getLabel());
        return service.saveOrUpdate(queryEntity);
    }

    /**
     * 作废
     *
     * @param param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/useless")
    public boolean useless(@RequestBody @Valid ContractParam param) {
        Long id = param.getId();
        TbContractCancelEntity contractCancelEntity = param.getContractCancelEntity();

        AssertUtil.notNull(contractCancelEntity, "作废信息不能为空");
        AssertUtil.notNull(contractCancelEntity.getRemark(), "作废备注不能为空");
        TbContractLeaseEntity queryEntity = getContract(id);
        baseContractService.handleAuditStatusByConfig(queryEntity, OPERATE_USELESS);
        contractCancelEntity.setLeaseContractId(id);
        //记录作废备注
        contractCancelEntity.setType(1);
        contractCancelService.saveOrUpdate(contractCancelEntity);
        String operMsg = "作废了租赁合同" + System.lineSeparator() +
                "作废备注:" + contractCancelEntity.getRemark();
        contractLogService.saveIntentionLog(id, OPERATE_USELESS, operMsg);
        return service.updateById(queryEntity);
    }


    /**
     * 审核  2.审核通过 3.审核不通过
     *
     * @return
     */
    @RequiresPermissions
    @PostMapping("/audit")
    public boolean audit(@RequestBody @Valid AuditParam param) {
        Long id = param.getId();
        TbContractLeaseEntity queryEntity = getContract(id);
        Integer contractStatus = queryEntity.getContractStatus();
        TbContractIntentionEntity auditContractIntentionEntity = (TbContractIntentionEntity) baseContractService.handleAuditContractStatus(queryEntity, param);
        //如果是续租中不通过,这取消关联
        if (Objects.equals(contractStatus, ContractEnum.STATUS_RELETING.getCode())
                && Objects.equals(param.getAuditStatus(), ContractEnum.AUDIT_REFUSE.getCode())) {
            service.cancelReletBind(id);
        }
        contractLogService.saveAuditLogMsg(contractStatus, param);
        return service.updateById(auditContractIntentionEntity);
    }

    private TbContractLeaseEntity getContractLeaseEntity(ContractReletParam param) {
        //续租合同
        TbContractLeaseEntity contractLeaseReletEntity = param.getContractLeaseReletEntity();
        Long id = SnowflakeIdWorkerUtil.nextId();
        Long contractNo = SnowflakeIdWorkerUtil.nextId();
        String name = contractLeaseReletEntity.getName();
        contractLeaseReletEntity.setName("【续租】" + name);
        contractLeaseReletEntity.setId(id);
        contractLeaseReletEntity.setContractNo(String.valueOf(contractNo));
        contractLeaseReletEntity.setAuditStatus(ContractEnum.AUDIT_WAITING_COMMIT.getCode());
        contractLeaseReletEntity.setContractStatus(ContractEnum.STATUS_DARFT.getCode());
        contractLeaseReletEntity.setReletContractId(param.getId());
        return contractLeaseReletEntity;
    }

}
