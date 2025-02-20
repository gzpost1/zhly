package cn.cuiot.dmp.baseconfig.controller.taskconfig;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.dto.BusinessAndOrgDto;
import cn.cuiot.dmp.base.application.dto.BusinessAndOrgNameDto;
import cn.cuiot.dmp.base.application.service.SystemUtilService;
import cn.cuiot.dmp.base.infrastructure.constants.OperationConstant;
import cn.cuiot.dmp.base.infrastructure.dto.BatcheOperation;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.syslog.LogContextHolder;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetData;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetInfo;
import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskConfigInsertDto;
import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskConfigUpdateDto;
import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskInfoPageDto;
import cn.cuiot.dmp.baseconfig.custommenu.dto.TbFlowTaskInfoQuery;
import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskConfig;
import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskInfo;
import cn.cuiot.dmp.baseconfig.custommenu.service.TbFlowTaskConfigService;
import cn.cuiot.dmp.baseconfig.custommenu.vo.FlowTaskConfigVo;
import cn.cuiot.dmp.baseconfig.custommenu.vo.FlowTaskInfoVo;
import cn.cuiot.dmp.baseconfig.flow.dto.TbFlowPageDto;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfig;
import cn.cuiot.dmp.baseconfig.flow.feign.SystemToFlowService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

/**
 * 基础功能-系统配置-初始化配置-任务配置
 * @Description 任务相关
 * @Date 2024/4/22 20:32
 * @Created by libo
 */
@RestController
@RequestMapping("/baseconfig/task")
public class TbFlowTaskInfoController {

    @Autowired
    private TbFlowTaskConfigService flowTaskConfigService;
    @Resource
    private SystemUtilService systemUtilService;
    @Autowired
    private SystemToFlowService systemToFlowService;

    public static final String SERVICETYPENAME = "任务配置";
    public static final String OPTTARGETINFONAME = "任务";


    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<FlowTaskInfoPageDto>> queryForPage(@RequestBody TbFlowTaskInfoQuery query) {
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());

        IPage<FlowTaskInfoPageDto> flowTaskInfoPageDtoIPage = flowTaskConfigService.queryForPage(query);
        systemUtilService.fillOrgNameAndBusinessName(flowTaskInfoPageDtoIPage);

        return IdmResDTO.success().body(flowTaskInfoPageDtoIPage);
    }

    /**
     * 获取详情
     *
     * @param idParam
     * @return
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<FlowTaskConfigVo> queryForDetail(@RequestBody @Valid IdParam idParam) {
        FlowTaskConfigVo flowTaskConfigVo = flowTaskConfigService.queryForDetail(idParam.getId());

        //填充表单名称
        if(Objects.nonNull(flowTaskConfigVo) && CollectionUtils.isNotEmpty(flowTaskConfigVo.getTaskInfoList())){
            List<Long> taskMenuIds = flowTaskConfigVo.getTaskMenuIds();
            FormConfigReqDTO formConfigReqDTO = new FormConfigReqDTO();
            formConfigReqDTO.setIdList(taskMenuIds);
            List<FormConfigRspDTO> formConfigRspDTOS = systemToFlowService.batchQueryFormConfig(formConfigReqDTO);
            AssertUtil.isTrue(CollectionUtils.isNotEmpty(formConfigRspDTOS) && formConfigRspDTOS.size() == taskMenuIds.size(), "表单配置为空");

            //填充 flowTaskConfigVo.getTaskMenuIds()的名称
            for (FlowTaskInfoVo flowTaskInfoVo : flowTaskConfigVo.getTaskInfoList()) {
                for (FormConfigRspDTO formConfigRspDTO : formConfigRspDTOS) {
                    if (Objects.equals(flowTaskInfoVo.getFormId(), formConfigRspDTO.getId())) {
                        flowTaskInfoVo.setFormName(formConfigRspDTO.getName());
                        break;
                    }
                }
            }
        }
        return IdmResDTO.success().body(flowTaskConfigVo);
    }


    /**
     * 创建
     *
     * @param createDto
     * @return
     */
    @RequiresPermissions
    @PostMapping("/create")
    @LogRecord(operationCode = "taskCreate", operationName = "任务配置创建", serviceType = ServiceTypeConst.BASE_CONFIG,serviceTypeName = SERVICETYPENAME)
    public IdmResDTO create(@RequestBody @Valid FlowTaskConfigInsertDto createDto) {
        validateBusinessAndOrg(createDto.getName(),null);
        Long id = flowTaskConfigService.create(createDto);

        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name(OPTTARGETINFONAME)
                .targetDatas(Lists.newArrayList(new OptTargetData(createDto.getName(),id.toString())))
                .build());

        return IdmResDTO.success();
    }

    /**
     * 验证任务信息
     * @param name
     * @param id
     */
    private void validateBusinessAndOrg(String name,Long id) {
        //判断任务名称是否重复
        LambdaQueryWrapper<TbFlowTaskConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbFlowTaskConfig::getName, name);
        queryWrapper.eq(TbFlowTaskConfig::getCompanyId,LoginInfoHolder.getCurrentOrgId());
        if(Objects.nonNull(id)){
            queryWrapper.ne(TbFlowTaskConfig::getId, id);
        }
        long count = flowTaskConfigService.count(queryWrapper);
        AssertUtil.isFalse(count > 0, "任务名称已存在");
    }

    /**
     * 更新
     *
     * @param updateDto
     * @return
     */
    @RequiresPermissions
    @PostMapping("/update")
    @LogRecord(operationCode = "taskUpdate", operationName = "任务配置更新", serviceType = ServiceTypeConst.BASE_CONFIG,serviceTypeName = SERVICETYPENAME)
    public IdmResDTO update(@RequestBody @Valid FlowTaskConfigUpdateDto updateDto) {
        validateBusinessAndOrg(updateDto.getName(),updateDto.getId());

        flowTaskConfigService.updateData(updateDto);

        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name(OPTTARGETINFONAME)
                .targetDatas(Lists.newArrayList(new OptTargetData(updateDto.getName(),updateDto.getId().toString())))
                .build());

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
    @LogRecord(operationCode = "taskDelete", operationName = "任务配置删除", serviceType = ServiceTypeConst.BASE_CONFIG,serviceTypeName = SERVICETYPENAME)
    public IdmResDTO delete(@RequestBody @Valid DeleteParam deleteParam) {
        TbFlowTaskConfig taskConfig = flowTaskConfigService.getById(deleteParam.getId());
        AssertUtil.notNull(taskConfig,"流程配置不存在");

        flowTaskConfigService.delete(Lists.newArrayList(deleteParam.getId()));

        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name(OPTTARGETINFONAME)
                .targetDatas(Lists.newArrayList(new OptTargetData(taskConfig.getName(),deleteParam.getId().toString())))
                .build());
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
    @LogRecord(operationCode = "taskUpdateStatus", operationName = "任务配置更新状态", serviceType = ServiceTypeConst.BASE_CONFIG,serviceTypeName = SERVICETYPENAME)
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        TbFlowTaskConfig taskConfig = flowTaskConfigService.getById(updateStatusParam.getId());
        AssertUtil.notNull(taskConfig,"流程配置不存在");

        flowTaskConfigService.updateStatus(updateStatusParam);

        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name(OPTTARGETINFONAME)
                .targetDatas(Lists.newArrayList(new OptTargetData(taskConfig.getName(),updateStatusParam.getId().toString())))
                .build());

        return IdmResDTO.success();
    }

    /**
     * 批量操作
     */
    @RequiresPermissions
    @PostMapping("/batchedOperation")
    @LogRecord(operationCode = "flowBatchedOperation", operationName = "任务配置批量修改", serviceType = ServiceTypeConst.BASE_CONFIG,serviceTypeName = SERVICETYPENAME)
    public IdmResDTO batchedOperation(@RequestBody @Valid BatcheOperation batcheOperation) {
        flowTaskConfigService.batchedOperation(batcheOperation);
        if(Objects.equals(batcheOperation.getOperaTionType(), OperationConstant.DELETE)){
            LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                    .name(OPTTARGETINFONAME)
                    .targetDatas(Lists.newArrayList(new OptTargetData("批量删除", StringUtils.join(batcheOperation.getIds(),","))))
                    .build());
        }else if(Objects.equals(batcheOperation.getOperaTionType(), OperationConstant.STATUS)){
            LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                    .name(OPTTARGETINFONAME)
                    .targetDatas(Lists.newArrayList(new OptTargetData("批量删除", StringUtils.join(batcheOperation.getIds(),","))))
                    .build());
        }else if(Objects.equals(batcheOperation.getOperaTionType(), OperationConstant.MOVE)){
            LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                    .name(OPTTARGETINFONAME)
                    .targetDatas(Lists.newArrayList(new OptTargetData("批量删除", StringUtils.join(batcheOperation.getIds(),","))))
                    .build());
        }

        return IdmResDTO.success();
    }
}

