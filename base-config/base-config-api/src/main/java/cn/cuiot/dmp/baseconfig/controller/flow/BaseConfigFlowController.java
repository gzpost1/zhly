package cn.cuiot.dmp.baseconfig.controller.flow;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.BatcheOperation;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.baseconfig.flow.dto.FlowEngineInsertDto;
import cn.cuiot.dmp.baseconfig.flow.dto.FlowEngineUpdateDto;
import cn.cuiot.dmp.baseconfig.flow.dto.TbFlowConfigQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.TbFlowPageDto;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfig;
import cn.cuiot.dmp.baseconfig.flow.service.TbFlowConfigService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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


    @Autowired
    private TbFlowConfigService tbFlowConfigService;

    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<TbFlowPageDto>> queryForPage(@RequestBody TbFlowConfigQuery query) {
        return IdmResDTO.success().body(tbFlowConfigService.queryForPage(query));
    }


    /**
     * 获取详情
     *
     * @param idParam
     * @return
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<TbFlowConfig> queryForDetail(@RequestBody @Valid IdParam idParam) {
        return IdmResDTO.success().body(tbFlowConfigService.getById(idParam.getId()));
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

        tbFlowConfigService.removeById(deleteParam.getId());

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
