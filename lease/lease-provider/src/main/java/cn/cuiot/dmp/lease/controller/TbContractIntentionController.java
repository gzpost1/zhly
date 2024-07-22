package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.base.infrastructure.dto.BaseVO;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomConfigDetailRspDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.SnowflakeIdWorkerUtil;
import cn.cuiot.dmp.lease.dto.contract.*;
import cn.cuiot.dmp.lease.entity.TbContractCancelEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import cn.cuiot.dmp.lease.service.*;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.cuiot.dmp.common.constant.AuditContractConstant.*;
import static cn.cuiot.dmp.common.constant.AuditContractConstant.CONTRACT_INTENTION_TYPE;
import static cn.cuiot.dmp.common.constant.AuditContractConstant.CONTRACT_LEASE_TYPE;
import static cn.cuiot.dmp.lease.service.BaseContractService.RELATE_INTENTION_TYPE;


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
    @Autowired
    TbContractLeaseRelateService leaseRelateService;
    @Autowired
    SystemApiFeignService systemApiFeignService;

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
        contractLogService.saveOperateLog(id, OPERATE_NEW, CONTRACT_INTENTION_TYPE);
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
        contractLogService.saveOperateLog(id, OPERATE_COMMIT, CONTRACT_INTENTION_TYPE);
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

        queryEntity.setContractLeaseId(contractLeaseId);
        //记录租赁合同关联信息
        leaseRelateService.saveLeaseRelated(queryEntity, RELATE_INTENTION_TYPE, contractLeaseId);
        baseContractService.handleAuditStatusByConfig(queryEntity, OPERATE_SIGN_CONTRACT);
        String operMsg = "签约了意向合同,关联的租赁合同为:"
                + contractLease.getName() + "(编号" + contractLease.getContractNo() + ")";
        String leaseOperMsg = "在意向合同中签约了本租赁合同" + System.lineSeparator() + "意向合同编码:" + queryEntity.getContractNo();
        contractLogService.saveLog(id, OPERATE_SIGN_CONTRACT, CONTRACT_INTENTION_TYPE, operMsg, contractLease.getContractNo(), null);
        //租赁合同也需要记录日志
        contractLogService.saveLog(contractLease.getId(), OPERATE_SIGN_LEASE_CONTRACT, CONTRACT_LEASE_TYPE,
                leaseOperMsg, queryEntity.getContractNo(), null);
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
        TbContractCancelEntity contractCancelEntity = param.getContractCancelEntity();
        String reasonId = contractCancelEntity.getReason();
        AssertUtil.notNull(id, "合同id不能为空");
        AssertUtil.notNull(contractCancelEntity, "退订信息不能为空");
        AssertUtil.notNull(contractCancelEntity.getDate(), "退定日期不能为空");
        AssertUtil.notNull(reasonId, "退定原因不能为空");
        TbContractIntentionEntity queryEntity = getContract(id);
        baseContractService.handleAuditStatusByConfig(queryEntity, OPERATE_CANCEL);
        contractCancelEntity.setIntentionContractId(id);
        contractCancelEntity.setId(SnowflakeIdWorkerUtil.nextId());
        contractCancelService.saveOrUpdate(contractCancelEntity);

        String cancelReson = getCancelReson(Long.valueOf(reasonId));

        String operMsg = "退定了意向合同" + System.lineSeparator() +
                "退定原因:" + cancelReson + System.lineSeparator() + "退定备注:" + contractCancelEntity.getRemark();
        contractLogService.saveLog(id, OPERATE_CANCEL, CONTRACT_INTENTION_TYPE, operMsg, null, contractCancelEntity.getPath());
        return service.updateById(queryEntity);
    }

    private String getCancelReson(Long id) {
        ArrayList<Long> param = Lists.newArrayList();
        param.add(id);
        CustomConfigDetailReqDTO dto = new CustomConfigDetailReqDTO();
        dto.setCustomConfigDetailIdList(param);
        IdmResDTO<List<CustomConfigDetailRspDTO>> listIdmResDTO = systemApiFeignService.batchQueryCustomConfigDetails(dto);
        List<CustomConfigDetailRspDTO> data = listIdmResDTO.getData();
        if (CollectionUtils.isNotEmpty(data)) {
            CustomConfigDetailRspDTO customConfigDetailRspDTO = data.get(0);
            return customConfigDetailRspDTO.getName();
        }
        return null;
    }

    private TbContractIntentionEntity getContract(Long id) {
        TbContractIntentionEntity contract = service.getById(id);
        AssertUtil.notNull(contract, "合同不存在!");
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
        TbContractCancelEntity contractCancelEntity = param.getContractCancelEntity();
        AssertUtil.notNull(contractCancelEntity, "作废信息不能为空");
        AssertUtil.notNull(contractCancelEntity.getRemark(), "作废备注不能为空");
        TbContractIntentionEntity queryEntity = getContract(id);
        baseContractService.handleAuditStatusByConfig(queryEntity, OPERATE_USELESS);
        Long useLessId = SnowflakeIdWorkerUtil.nextId();
        if (Objects.isNull(contractCancelEntity.getId())) {
            contractCancelEntity.setId(useLessId);
        }
        contractCancelEntity.setIntentionContractId(id);
        //记录作废备注
        contractCancelEntity.setType(1);
        contractCancelService.saveOrUpdate(contractCancelEntity);
        String operMsg = "作废了意向合同" + System.lineSeparator() +
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
        TbContractIntentionEntity queryEntity = getContract(param.getId());
        Integer contractStatus = queryEntity.getContractStatus();
        TbContractIntentionEntity auditContractIntentionEntity = (TbContractIntentionEntity) baseContractService.handleAuditContractStatus(queryEntity, param);
        contractLogService.saveAuditLogMsg(contractStatus, param, CONTRACT_INTENTION_TYPE);
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

    /**
     * 统计合同状态
     *
     * @return
     */
    @PostMapping("/statisticsContract")
    public List<BaseVO> statisticsContract() {
        return service.statisticsContract();
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
