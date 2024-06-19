package cn.cuiot.dmp.lease.controller;


import cn.cuiot.dmp.base.application.controller.BaseCurdController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.lease.dto.contract.TbContractIntentionParam;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionMoneyEntity;
import cn.cuiot.dmp.lease.entity.TbContractLogEntity;
import cn.cuiot.dmp.lease.service.TbContractIntentionBindInfoService;
import cn.cuiot.dmp.lease.service.TbContractIntentionService;
import cn.cuiot.dmp.lease.service.TbContractLogService;
import cn.cuiot.dmp.lease.vo.ContractBindInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.common.constant.AuditConstant.AUDIT_STATUS_WAITING;

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
    TbContractIntentionBindInfoService bindInfoService;

    /**
     * 保存草稿
     *
     * @param entity
     * @return
     */
    @Override
    public boolean create(@RequestBody @Valid TbContractIntentionEntity entity) {
        bindInfoService.createContractIntentionBind(entity);
        entity.setAuditStatus(AUDIT_STATUS_WAITING);
        boolean save = service.save(entity);
        contractLogService.saveLog("新增","新增了意向合同");
        return save;
    }


    /**
     * 提交
     *
     * @param entity
     * @return
     */
    public void commit(@RequestBody @Valid TbContractIntentionEntity entity) {


    }

    @Override
    public boolean update(@RequestBody TbContractIntentionEntity entity) {
        entity.setAuditStatus(AUDIT_STATUS_WAITING);
        return super.update(entity);
    }
}
