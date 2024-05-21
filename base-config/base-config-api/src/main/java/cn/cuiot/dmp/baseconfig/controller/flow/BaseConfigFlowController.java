package cn.cuiot.dmp.baseconfig.controller.flow;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.dto.BusinessAndOrgDto;
import cn.cuiot.dmp.base.application.dto.BusinessAndOrgNameDto;
import cn.cuiot.dmp.base.application.service.SystemUtilService;
import cn.cuiot.dmp.base.infrastructure.dto.BatcheOperation;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.baseconfig.flow.dto.*;
import cn.cuiot.dmp.baseconfig.flow.service.TbFlowConfigService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants.PROCESS_PREFIX;

/**
 * 基础功能-系统配置-初始化配置-流程配置
 *
 * @Description 任务相关
 * @Date 2024/4/22 20:32
 * @Created by libo
 */
@RestController
@RequestMapping("/baseconfig/flow")
public class BaseConfigFlowController {
    @Resource
    private SystemUtilService systemUtilService;

    @Autowired
    private TbFlowConfigService tbFlowConfigService;
    @Resource
    private RepositoryService repositoryService;

    /**
     * 工单获取流程分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForWorkOrderPage")
    public IdmResDTO<IPage<TbFlowPageDto>> queryForWorkOrderPage(@RequestBody TbFlowConfigQuery query) {
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        IPage<TbFlowPageDto> tbFlowPageDtoIPage = tbFlowConfigService.queryForWorkOrderPage(query);
        if(Objects.nonNull(tbFlowPageDtoIPage) && CollectionUtils.isNotEmpty(tbFlowPageDtoIPage.getRecords())){
            tbFlowPageDtoIPage.getRecords().stream().forEach(e -> {
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(PROCESS_PREFIX + e.getId()).latestVersion().singleResult();
                AssertUtil.notNull(processDefinition,"该流程暂未接入Flowable,请重试");
                e.setProcessDefinitionId(processDefinition.getId());

            });
        }
        return IdmResDTO.success().body(tbFlowPageDtoIPage);
    }

    /**
     * 工单获取流程详情
     *
     * @param idParam
     * @return
     */
    @PostMapping("/queryForWorkDetail")
    public IdmResDTO<FlowEngineVo> queryForWorkDetail(@RequestBody @Valid IdParam idParam) {
        FlowEngineVo flowEngineVo = tbFlowConfigService.queryForDetail(idParam.getId());
        AssertUtil.notNull(flowEngineVo,"流程配置不存在");
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(PROCESS_PREFIX + flowEngineVo.getId()).latestVersion().singleResult();
        AssertUtil.notNull(processDefinition,"该流程暂未接入Flowable,请重试");
        flowEngineVo.setProcessDefinitionId(processDefinition.getId());
        return IdmResDTO.success().body(flowEngineVo);
    }


    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<TbFlowPageDto>> queryForPage(@RequestBody TbFlowConfigQuery query) {
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());

        IPage<TbFlowPageDto> tbFlowPageDtoIPage = tbFlowConfigService.queryForPage(query);
        systemUtilService.fillOrgNameAndBusinessName(tbFlowPageDtoIPage);

        return IdmResDTO.success().body(tbFlowPageDtoIPage);
    }

    /**
     * 获取详情
     *
     * @param idParam
     * @return
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<FlowEngineVo> queryForDetail(@RequestBody @Valid IdParam idParam) {
        return IdmResDTO.success().body(tbFlowConfigService.queryForDetail(idParam.getId()));
    }


    /**
     * 创建
     *
     * @param createDto
     * @return
     */
    @RequiresPermissions
    @PostMapping("/create")
    @LogRecord(operationCode = "flowCreate", operationName = "流程配置创建", serviceType = ServiceTypeConst.BASE_CONFIG)
    public IdmResDTO create(@RequestBody @Valid FlowEngineInsertDto createDto) {

        tbFlowConfigService.saveFlow(createDto);

        return IdmResDTO.success();
    }

    /**
     * 更新
     *
     * @param updateDto
     * @return
     */
    @RequiresPermissions
    @PostMapping("/update")
    @LogRecord(operationCode = "flowUpdate", operationName = "流程配置修改", serviceType = ServiceTypeConst.BASE_CONFIG)
    public IdmResDTO update(@RequestBody @Valid FlowEngineUpdateDto updateDto) {

        tbFlowConfigService.updateFlow(updateDto);

        return IdmResDTO.success();
    }

    /**
     * 删除
     *
     * @param deleteParam
     * @return
     */
    @RequiresPermissions
    @PostMapping("/delete")
    @LogRecord(operationCode = "flowDelete", operationName = "流程配置删除", serviceType = ServiceTypeConst.BASE_CONFIG)
    public IdmResDTO delete(@RequestBody @Valid DeleteParam deleteParam) {
        tbFlowConfigService.delete(Lists.newArrayList(deleteParam.getId()));

        return IdmResDTO.success();
    }

    /**
     * 更新状态
     *
     * @param updateStatusParam
     * @return
     */
    @RequiresPermissions
    @PostMapping("/updateStatus")
    @LogRecord(operationCode = "flowUpdateStatus", operationName = "流程配置更新状态", serviceType = ServiceTypeConst.BASE_CONFIG)
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        tbFlowConfigService.updateStatus(updateStatusParam);

        return IdmResDTO.success();
    }

    /**
     * 批量操作
     */
    @RequiresPermissions
    @PostMapping("/batchedOperation")
    @LogRecord(operationCode = "flowBatchedOperation", operationName = "流程配置批量修改", serviceType = ServiceTypeConst.BASE_CONFIG)
    public IdmResDTO batchedOperation(@RequestBody @Valid BatcheOperation batcheOperation) {
        tbFlowConfigService.batchedOperation(batcheOperation);
        return IdmResDTO.success();
    }
}
