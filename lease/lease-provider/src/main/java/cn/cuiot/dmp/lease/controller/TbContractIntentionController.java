package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.lease.dto.contract.*;
import cn.cuiot.dmp.lease.entity.TbContractCancelEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

import static cn.cuiot.dmp.common.constant.AuditConstant.*;


/**
 * 意向合同
 *
 * @author Mujun
 * @since 2024-06-12
 */
@Slf4j
@RestController
@RequestMapping("/contractIntention")
public class TbContractIntentionController extends BaseCurdController<TbContractIntentionService, TbContractIntentionEntity, TbContractIntentionParam> {


    @Autowired
    TbContractLogService contractLogService;
    @Autowired
    TbContractCancelService contractCancelService;
    @Autowired
    TbContractBindInfoService bindInfoService;
    @Autowired
    BaseContractService baseContractService;
    @Autowired
    TbContractLeaseService contractLeaseService;

    /**
     * 保存草稿
     *
     * @param entity
     * @return
     */
    @RequiresPermissions
    @PostMapping("/saveDarft")
    public boolean saveDarft(@RequestBody @Valid TbContractIntentionEntity entity) {
        Long id = SnowflakeIdWorkerUtil.nextId();
        entity.setId(id);
        entity.setAuditStatus(ContractEnum.AUDIT_WAITING_COMMIT.getCode());
        entity.setContractStatus(ContractEnum.STATUS_DARFT.getCode());
        contractLogService.saveIntentionLog(id, OPERATE_NEW, "新增了意向合同");
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
    public boolean commit(@RequestBody @Valid TbContractIntentionEntity entity) {
        Long id = Optional.ofNullable(entity.getId()).orElse(SnowflakeIdWorkerUtil.nextId());
        entity.setId(id);
        //需要审核
        baseContractService.handleAuditStatusByConfig(entity, OPERATE_COMMIT);
        contractLogService.saveIntentionLog(id, OPERATE_COMMIT, "提交了意向合同");
        return service.saveOrUpdate(entity);
    }


    /**
     * 签约
     *
     * @param param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/signContract")
    public boolean signContract(@RequestBody @Valid ContractParam param) {
        Long id = param.getId();
        Long contractLeaseId = param.getLeaseId();
        AssertUtil.notNull(contractLeaseId, "租赁合同id不能为空");
        TbContractLeaseEntity contractLease = contractLeaseService.getById(contractLeaseId);
        AssertUtil.notNull(contractLease, "租赁合同不存在");
        TbContractIntentionEntity queryEntity = getContract(id);
        baseContractService.handleAuditStatusByConfig(queryEntity, OPERATE_SIGN_CONTRACT);
        queryEntity.setContractLeaseId(contractLeaseId);
        String operMsg = "签约了意向合同,关联的租赁合同为:"
                + contractLease.getName() + "(编号" + contractLease.getContractNo() + ")";
        String leaseOperMsg = "在意向合同中签约了本租赁合同" + System.lineSeparator() + "意向合同编码:" + queryEntity.getContractNo();
        contractLogService.saveLog(id, OPERATE_SIGN_CONTRACT, TbContractLogService.TYPE_INTERNTION, operMsg, String.valueOf(contractLease.getId()), null);
        contractLogService.saveLog(contractLease.getId(), OPERATE_SIGN_LEASE_CONTRACT, TbContractLogService.TYPE_LEASE,
                leaseOperMsg, String.valueOf(queryEntity.getId()), null);
        return service.updateById(queryEntity);
    }


    /**
     * 退定
     *
     * @param param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/cancel")
    public boolean cancel(@RequestBody @Valid ContractParam param) {
        Long id = param.getId();
        TbContractCancelEntity contractCancelParam = param.getContractCancelEntity();
        AssertUtil.notNull(id, "合同id不能为空");
        AssertUtil.notNull(contractCancelParam, "退订信息不能为空");
        AssertUtil.notNull(contractCancelParam.getDate(), "退定日期不能为空");
        AssertUtil.notNull(contractCancelParam.getReason(), "退定原因不能为空");
        TbContractIntentionEntity queryEntity = getContract(id);
        baseContractService.handleAuditStatusByConfig(queryEntity, OPERATE_CANCEL);
        contractCancelParam.setIntentionContractId(id);
        contractCancelParam.setId(SnowflakeIdWorkerUtil.nextId());
        contractCancelService.saveOrUpdate(contractCancelParam);
        String operMsg = "退定了意向合同" + System.lineSeparator() +
                "退定原因:" + contractCancelParam.getReason() + System.lineSeparator() + "退定备注:" + contractCancelParam.getRemark();
        contractLogService.saveIntentionLog(id, OPERATE_CANCEL, operMsg);
        return service.updateById(queryEntity);
    }

    private TbContractIntentionEntity getContract(Long id) {
        TbContractIntentionEntity contract = service.getById(id);
        AssertUtil.notNull(contract,"合同不存在!");
        return contract;
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
        TbContractCancelEntity contractCancelParam = param.getContractCancelEntity();
        AssertUtil.notNull(contractCancelParam, "作废信息不能为空");
        AssertUtil.notNull(contractCancelParam.getRemark(), "作废备注不能为空");
        TbContractIntentionEntity queryEntity = getContract(id);
        baseContractService.handleAuditStatusByConfig(queryEntity, OPERATE_USELESS);
        //记录作废备注
        contractCancelParam.setType(1);
        contractCancelService.saveOrUpdate(param);
        String operMsg = "作废了意向合同" + System.lineSeparator() +
                "作废备注:" + contractCancelParam.getRemark();
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
        TbContractIntentionEntity queryEntity = getContract(param.getId());
        Integer contractStatus = queryEntity.getContractStatus();
        TbContractIntentionEntity auditContractIntentionEntity = (TbContractIntentionEntity) baseContractService.handleAuditContractStatus(queryEntity, param);
        contractLogService.saveAuditLogMsg(contractStatus, param);
        return service.updateById(auditContractIntentionEntity);
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
        TbContractIntentionEntity queryEntity = getContract(id);
        queryEntity.setFollowUp(String.valueOf(param.getFollowUpId()));
        String operMsg = "分配了意向合同,分配给" + param.getFollowUpName();
        contractLogService.saveIntentionLog(id, OPERATE_ALLOCATION, operMsg);
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
        TbContractIntentionEntity queryEntity = getContract(id);
        queryEntity.setLabel(param.getLabel());
        return service.saveOrUpdate(queryEntity);
    }

    @Override
    public boolean update(@RequestBody TbContractIntentionEntity entity) {
        checkUpdate(entity.getId());
        entity.setAuditStatus(ContractEnum.AUDIT_WAITING.getCode());
        return service.updateById(entity);
    }

    private void checkUpdate(Long id) {
        TbContractIntentionEntity queryInfo = getContract(id);
        AssertUtil.isFalse(!Objects.equals(queryInfo.getContractStatus(), ContractEnum.STATUS_DARFT.getCode()), "只有草稿状态的合同才能编辑");
    }
}
