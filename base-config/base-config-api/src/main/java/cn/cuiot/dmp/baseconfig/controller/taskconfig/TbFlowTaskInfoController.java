package cn.cuiot.dmp.baseconfig.controller.taskconfig;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.BatcheOperation;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskConfigInsertDto;
import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskConfigUpdateDto;
import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskInfoPageDto;
import cn.cuiot.dmp.baseconfig.custommenu.dto.TbFlowTaskInfoQuery;
import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskInfo;
import cn.cuiot.dmp.baseconfig.custommenu.service.TbFlowTaskConfigService;
import cn.cuiot.dmp.baseconfig.custommenu.vo.FlowTaskConfigVo;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 基础功能-系统配置-初始化配置-任务配置
 * @Description 任务相关
 * @Date 2024/4/22 20:32
 * @Created by libo
 */
@RestController
@RequestMapping("/tb_flow_task_info")
public class TbFlowTaskInfoController {

    @Autowired
    private TbFlowTaskConfigService flowTaskConfigService;

    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<FlowTaskInfoPageDto>> queryForPage(@RequestBody TbFlowTaskInfoQuery query) {
        return IdmResDTO.success().body(flowTaskConfigService.queryForPage(query));
    }


    /**
     * 获取详情
     *
     * @param idParam
     * @return
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<FlowTaskConfigVo> queryForDetail(@RequestBody @Valid IdParam idParam) {
        return IdmResDTO.success().body(flowTaskConfigService.queryForDetail(idParam.getId()));
    }


    /**
     * 创建
     *
     * @param createDto
     * @return
     */
    @PostMapping("/create")
    public IdmResDTO create(@RequestBody @Valid FlowTaskConfigInsertDto createDto) {
        flowTaskConfigService.create(createDto);
        return IdmResDTO.success();
    }

    /**
     * 更新
     *
     * @param updateDto
     * @return
     */
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody @Valid FlowTaskConfigUpdateDto updateDto) {

        flowTaskConfigService.updateData(updateDto);

        return IdmResDTO.success();
    }

    /**
     * 删除
     *
     * @param deleteParam
     * @return
     */
    @PostMapping("/delete")
    public IdmResDTO delete(@RequestBody @Valid DeleteParam deleteParam) {
        flowTaskConfigService.delete(Lists.newArrayList(deleteParam.getId()));
        return IdmResDTO.success();
    }

    /**
     * 更新状态
     *
     * @param updateStatusParam
     * @return
     */
    @PostMapping("/updateStatus")
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        flowTaskConfigService.updateStatus(updateStatusParam);

        return IdmResDTO.success();
    }

    /**
     * 批量操作
     */
    @RequiresPermissions
    @PostMapping("/batchedOperation")
    public IdmResDTO batchedOperation(@RequestBody @Valid BatcheOperation batcheOperation) {
        flowTaskConfigService.batchedOperation(batcheOperation);
        return IdmResDTO.success();
    }
}

