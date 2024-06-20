package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.lease.dto.contract.*;
import cn.cuiot.dmp.lease.entity.TbContractCancelEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.service.TbContractCancelService;
import cn.cuiot.dmp.lease.service.TbContractBindInfoService;
import cn.cuiot.dmp.lease.service.TbContractIntentionService;
import cn.cuiot.dmp.lease.service.TbContractLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

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

    /**
     * 保存草稿
     *
     * @param param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/saveDarft")
    public boolean saveDarft(@RequestBody @Valid ContractParam param) {
        TbContractIntentionEntity entity = param.getContractIntentionEntity();
        Long id = SnowflakeIdWorkerUtil.nextId();
        entity.setId(id);
        entity.setAuditStatus(ContractEnum.AUDIT_WAITING_COMMIT.getCode());
        entity.setContractStatus(ContractEnum.STATUS_DARFT.getCode());
        contractLogService.saveLog(id, "新增", "新增了意向合同");
        return service.save(entity);
    }


    /**
     * 提交
     *
     * @param param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/commit")
    public boolean commit(@RequestBody @Valid ContractParam param) {
        TbContractIntentionEntity entity = param.getContractIntentionEntity();
        Long id = SnowflakeIdWorkerUtil.nextId();
        entity.setId(id);
        //需要审核
        service.handleAuditStatusByConfig(entity, OPERATE_COMMIT);
        contractLogService.saveLog(id, OPERATE_COMMIT, "提交了意向合同");
        return service.save(entity);
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
        TbContractLeaseEntity contractLease = param.getContractLeaseEntity();
        AssertUtil.notNull(contractLease, "关联租赁合同信息不能为空");
        AssertUtil.notNull(contractLease.getId(), "关联租赁合id不能为空");
        AssertUtil.notNull(contractLease.getName(), "关联租赁合同名称不能为空");
        AssertUtil.notNull(contractLease.getContractNo(), "关联租赁合同编号不能为空");
        TbContractIntentionEntity queryEntity = service.getById(id);
        service.handleAuditStatusByConfig(queryEntity, OPERATE_SIGN_CONTRACT);
        queryEntity.setContractLeaseId(contractLease.getId());
        contractLogService.saveLog(id, OPERATE_SIGN_CONTRACT, "签约了意向合同,关联的租赁合同为:"
                + contractLease.getName()+"(编号"+contractLease.getContractNo()+")", String.valueOf(contractLease.getId()), null);
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
        AssertUtil.notNull(id, "退定合同id不能为空");
        AssertUtil.notNull(contractCancelParam, "退订信息不能为空");
        AssertUtil.notNull(contractCancelParam.getDate(), "退定日期不能为空");
        AssertUtil.notNull(contractCancelParam.getReason(), "退定原因不能为空");
        TbContractIntentionEntity queryEntity = service.getById(id);
        service.handleAuditStatusByConfig(queryEntity, OPERATE_CANCEL);
        contractCancelParam.setContractNo(queryEntity.getContractNo());
        contractCancelParam.setId(queryEntity.getId());
        contractCancelService.saveOrUpdate(contractCancelParam);
        contractLogService.saveLog(id, OPERATE_CANCEL, "退定了意向合同" + System.lineSeparator() +
                "退定原因:" + contractCancelParam.getReason() + System.lineSeparator() + "退定备注:" + contractCancelParam.getRemark());
        return service.updateById(queryEntity);
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
        TbContractIntentionEntity queryEntity = service.getById(id);
        service.handleAuditStatusByConfig(queryEntity, OPERATE_USELESS);
        //记录作废备注
        contractCancelParam.setType(1);
        contractCancelService.saveOrUpdate(param);
        contractLogService.saveLog(id, OPERATE_USELESS, "作废了意向合同" + System.lineSeparator() +
                "作废备注:" + contractCancelParam.getRemark());
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
        TbContractIntentionEntity auditContractIntentionEntity = service.handleAuditContractStatus(param);
        contractLogService.saveAuditLogMsg(param);
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
        TbContractIntentionEntity queryEntity = service.getById(id);
        queryEntity.setFollowUp(String.valueOf(param.getFollowUpId()));
        contractLogService.saveLog(id, OPERATE_ALLOCATION, "分配了意向合同,分配给" + param.getFollowUpName());
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
        TbContractIntentionEntity queryEntity = service.getById(id);
        queryEntity.setLabel(param.getLabel());
        return service.saveOrUpdate(queryEntity);
    }

    @Override
    public boolean update(@RequestBody TbContractIntentionEntity entity) {
        checkUpdate(entity.getId());
        entity.setAuditStatus(ContractEnum.AUDIT_WAITING.getCode());
        return super.update(entity);
    }

    private void checkUpdate(Long id) {
        TbContractIntentionEntity queryInfo = service.getById(id);
        AssertUtil.isFalse(Objects.equals(queryInfo.getContractStatus(), ContractEnum.STATUS_DARFT.getCode()), "只有草稿状态的合同才能编辑");
    }

//    private boolean save(TbContractIntentionEntity entity) {
//        Long code = SnowflakeIdWorkerUtil.nextId();
//        entity.setContractNo(String.valueOf(code));
//        bindInfoService.createContractIntentionBind(entity);
//        boolean save = service.save(entity);
//        return save;
//    }

    @Override
    public boolean delete(@RequestBody @Valid IdParam params) {
        Long id = params.getId();
        bindInfoService.removeByContractId(id);
        //操作日志暂时保留
        //        contractLogService.removeByContractId(id);
        return super.delete(params);
    }
}
