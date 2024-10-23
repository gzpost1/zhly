package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.base.application.enums.BeanValidationGroup;
import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.base.infrastructure.dto.BaseVO;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.contract.*;
import cn.cuiot.dmp.lease.entity.TbContractCancelEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseBackEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseRelateEntity;
import cn.cuiot.dmp.lease.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.cuiot.dmp.common.constant.AuditContractConstant.*;
import static cn.cuiot.dmp.common.constant.AuditContractConstant.CONTRACT_LEASE_TYPE;
import static cn.cuiot.dmp.lease.service.BaseContractService.RELATE_NEW_LEASE_TYPE;
import static cn.cuiot.dmp.lease.service.BaseContractService.RELATE_ORI_LEASE_TYPE;

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
    @Autowired
    TbContractLeaseRelateService leaseRelateService;

    @RequiresPermissions
    @Override
    public PageResult<TbContractLeaseEntity> queryForPage(@RequestBody  TbContractLeaseParam params) {
        params.setOrgId(LoginInfoHolder.getCurrentOrgId());
        return super.queryForPage(params);
    }

    @RequiresPermissions
    @Override
    public List<TbContractLeaseEntity> list(@RequestBody TbContractLeaseEntity params) {
        params.setOrgId(LoginInfoHolder.getCurrentOrgId());
        return super.list(params);
    }

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
        contractLogService.saveOperateLog(id, OPERATE_NEW, CONTRACT_LEASE_TYPE);
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
        contractLogService.saveOperateLog(id, OPERATE_COMMIT, CONTRACT_LEASE_TYPE);
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
        Integer contractStatus = entity.getContractStatus();
        if (!Objects.equals(contractStatus, ContractEnum.STATUS_DARFT.getCode())) {
            baseContractService.handleAuditStatusByConfig(entity, OPERATE_CHANGE);
            contractLogService.saveOperateLog(id, OPERATE_CHANGE, CONTRACT_LEASE_TYPE);
        }
        return service.updateById(entity);
    }

    /**
     * 退租
     *
     * @param leaseBackEntity
     * @return
     */
    @RequiresPermissions
    @PostMapping("/leaseBack")
    public boolean leaseBack(@RequestBody @Valid TbContractLeaseBackEntity leaseBackEntity) {
        Long contractId = leaseBackEntity.getContractId();
        TbContractLeaseEntity queryEntity = getContract(contractId);
        baseContractService.handleAuditStatusByConfig(queryEntity, OPERATE_LEASE_BACK);
        Long leaseBackId = SnowflakeIdWorkerUtil.nextId();
        leaseBackEntity.setId(leaseBackId);
        leaseBackService.saveOrUpdate(leaseBackEntity);
        String remark = Optional.ofNullable(leaseBackEntity.getRemark()).orElse("无");
        String operMsg = "退租了租赁合同" + System.lineSeparator() + "退租说明:" + remark;
        contractLogService.saveLog(contractId, OPERATE_LEASE_BACK, CONTRACT_LEASE_TYPE, operMsg, String.valueOf(leaseBackId), leaseBackEntity.getPath());
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
        service.saveOrUpdate(contractLeaseReletEntity);

        //记录被续租合同的和续租合同关联关系
        leaseRelateService.saveLeaseRelated(contractLeaseReletEntity, RELATE_NEW_LEASE_TYPE, queryEntity.getId());
        //记录新的续租和原有合同关联关系
        leaseRelateService.saveLeaseRelated(queryEntity, RELATE_ORI_LEASE_TYPE, contractLeaseReletEntity.getId());
        baseContractService.handleAuditStatusByConfig(queryEntity, OPERATE_LEASE_RELET);
        String reletRemark = Optional.ofNullable(param.getContractLeaseReletEntity().getReletRemark()).orElse("无");
        String operMsg = "续租了租赁合同" + System.lineSeparator() + "续租新生成的合同编码:" + contractLeaseReletEntity.getContractNo() + System.lineSeparator()
                + "续租说明:" + reletRemark;
        contractLogService.saveLog(id, OPERATE_LEASE_RELET, CONTRACT_LEASE_TYPE, operMsg,
                String.valueOf(contractLeaseReletEntity.getContractNo()), contractLeaseReletEntity.getReletPath());
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
        contractLogService.saveLeaseLog(id, OPERATE_USELESS, operMsg);
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
        TbContractLeaseEntity auditContractIntentionEntity = (TbContractLeaseEntity) baseContractService.handleAuditContractStatus(queryEntity, param);
        contractLogService.saveAuditLogMsg(contractStatus, param, CONTRACT_LEASE_TYPE);
        return service.updateById(auditContractIntentionEntity);
    }

    /**
     * 关联合同信息
     *
     * @param param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryBindContract")
    public PageResult queryBindContract(@RequestBody @Valid LeaseBindParam param) {
        TbContractLeaseRelateParam queryParam = new TbContractLeaseRelateParam();
        queryParam.setContractId(param.getId());
        queryParam.setStatus(EntityConstants.ENABLED);
        param.setDescs(new String[]{TbContractLeaseRelateEntity.DATETIME});
        PageResult<TbContractLeaseRelateEntity> leaseRelateList = leaseRelateService.page(queryParam);
        return leaseRelateList;
    }

    /**
     * 统计合同状态
     *
     * @return
     */
    @RequiresPermissions
    @PostMapping("/statisticsContract")
    public List<BaseVO> statisticsContract() {
        return service.statisticsContract();
    }

    /**
     * 查询未关联意向合同的租赁合同
     * @param params
     * @return
     */
    @RequiresPermissions
    @PostMapping("/pageNoSigned")
    public PageResult<TbContractLeaseEntity> pageNoSigned(
            @RequestBody TbContractLeaseParam params) {
        params.setOrgId(LoginInfoHolder.getCurrentOrgId());
        return service.pageNoSigned(params);
    }
    /**
     * 生成新的续租合同
     *
     * @param param
     * @return
     */
    private TbContractLeaseEntity getContractLeaseEntity(ContractReletParam param) {
        //续租合同
        TbContractLeaseEntity contractLeaseReletEntity = param.getContractLeaseReletEntity();
        Long id = SnowflakeIdWorkerUtil.nextId();
        String contractNo = String.valueOf(SnowflakeIdWorkerUtil.nextId());
        AssertUtil.notNull(contractLeaseReletEntity.getReletDate(), "续租日期不能为空");
        String name = contractLeaseReletEntity.getName();
        contractLeaseReletEntity.setName("【续租】" + name);
        contractLeaseReletEntity.setId(id);
        contractLeaseReletEntity.setContractNo(contractNo);
        contractLeaseReletEntity.setAuditStatus(ContractEnum.AUDIT_WAITING_COMMIT.getCode());
        contractLeaseReletEntity.setContractStatus(ContractEnum.STATUS_DARFT.getCode());
        contractLeaseReletEntity.setReletContractId(param.getId());
        contractLeaseReletEntity.setCreateUser(null);
        contractLeaseReletEntity.setCreateTime(null);
        contractLeaseReletEntity.setUpdateUser(null);
        contractLeaseReletEntity.setUpdateTime(null);
        return contractLeaseReletEntity;
    }

    /**
     * 导出
     * @param pageQuery
     * @return
     * @throws Exception
     */
    @PostMapping("export")
    public IdmResDTO export(@RequestBody @Valid TbContractLeaseParam pageQuery) throws Exception {
        service.export(pageQuery);
        return IdmResDTO.success();
    }
}
