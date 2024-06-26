package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.lease.dto.contract.TbContractLeaseParam;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static cn.cuiot.dmp.common.constant.AuditConstant.OPERATE_COMMIT;

/**
 *
 * 租赁合同
 *
 * @author Mujun
 * @since 2024-06-19
 */
@Slf4j
@RestController
@RequestMapping("/contractLease")
    public class TbContractLeaseController extends BaseCurdController<TbContractLeaseService,TbContractLeaseEntity, TbContractLeaseParam> {

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
        contractLogService.saveLeaseLog(id, "新增", "新增了租赁合同");
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
        Long id = SnowflakeIdWorkerUtil.nextId();
        entity.setId(id);
        //需要审核
        baseContractService.handleAuditStatusByConfig(entity, OPERATE_COMMIT);
        contractLogService.saveLeaseLog(id, OPERATE_COMMIT, "提交了租赁合同");
        return service.save(entity);
    }


}
