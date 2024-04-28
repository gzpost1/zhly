package cn.cuiot.dmp.baseconfig.controller.flow;

import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.baseconfig.flow.dto.FlowEngineInsertDto;
import cn.cuiot.dmp.baseconfig.flow.dto.FlowEngineUpdateDto;
import cn.cuiot.dmp.baseconfig.flow.dto.TbFlowConfigQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.TbFlowPageDto;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfig;
import cn.cuiot.dmp.baseconfig.flow.service.TbFlowConfigService;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    @PostMapping("/create")
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
    @PostMapping("/update")
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
    @PostMapping("/delete")
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
    @PostMapping("/updateStatus")
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        TbFlowConfig entity = tbFlowConfigService.getById(updateStatusParam.getId());
        entity.setStatus(updateStatusParam.getStatus());
        tbFlowConfigService.updateById(entity);
        return IdmResDTO.success();
    }
}
