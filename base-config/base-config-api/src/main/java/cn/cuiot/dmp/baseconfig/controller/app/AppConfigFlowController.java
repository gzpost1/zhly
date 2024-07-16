package cn.cuiot.dmp.baseconfig.controller.app;

import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.baseconfig.flow.dto.FlowEngineVo;
import cn.cuiot.dmp.baseconfig.flow.dto.TbFlowConfigQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.TbFlowPageDto;
import cn.cuiot.dmp.baseconfig.flow.service.TbFlowConfigService;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import java.util.Objects;

import static cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants.PROCESS_PREFIX;

/**
 * 小程序端流程配置
 *
 * @Description 小程序端流程配置
 * @Date 2024/6/4 10:20
 * @Created by libo
 */
@RestController
@RequestMapping("/app/configflow")
public class AppConfigFlowController {
    @Autowired
    private TbFlowConfigService tbFlowConfigService;
    @Resource
    private RepositoryService repositoryService;
    @Autowired
    private ApiSystemService apiSystemService;
    @Autowired
    private ApiArchiveService apiArchiveService;

    /**
     * 工单获取流程分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForWorkOrderPage")
    public IdmResDTO<IPage<TbFlowPageDto>> queryForWorkOrderPage(@RequestBody TbFlowConfigQuery query) {
        Long deptId = null;

        if (Objects.equals(LoginInfoHolder.getCurrentUserType(), UserTypeEnum.USER.getValue())) {
            deptId = LoginInfoHolder.getCurrentDeptId();
            query.setIsSelectAppUser(null);
        } else if (Objects.equals(LoginInfoHolder.getCurrentUserType(), UserTypeEnum.OWNER.getValue())) {
            //获取小区信息
            if (Objects.isNull(query.getCommunityId())) {
                query.setCommunityId(LoginInfoHolder.getCommunityId());
            }
            BuildingArchive buildingArchive = apiArchiveService.lookupBuildingArchiveInfo(LoginInfoHolder.getCommunityId());
            AssertUtil.isTrue(Objects.nonNull(buildingArchive) && Objects.nonNull(buildingArchive.getDepartmentId()), "小区不存在");
            query.setIsSelectAppUser(EntityConstants.YES);
            deptId = buildingArchive.getDepartmentId();
        }

        AssertUtil.notNull(deptId, "部门不存在");
        query.setOrgId(deptId);

        IPage<TbFlowPageDto> tbFlowPageDtoIPage = tbFlowConfigService.queryForWorkOrderPage(query);
        if (Objects.nonNull(tbFlowPageDtoIPage) && CollectionUtils.isNotEmpty(tbFlowPageDtoIPage.getRecords())) {
            tbFlowPageDtoIPage.getRecords().stream().forEach(e -> {
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(PROCESS_PREFIX + e.getId()).latestVersion().singleResult();
                AssertUtil.notNull(processDefinition, "该流程暂未接入Flowable,请重试");
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
        AssertUtil.notNull(flowEngineVo, "流程配置不存在");
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(PROCESS_PREFIX + flowEngineVo.getId()).latestVersion().singleResult();
        AssertUtil.notNull(processDefinition, "该流程暂未接入Flowable,请重试");
        flowEngineVo.setProcessDefinitionId(processDefinition.getId());
        return IdmResDTO.success().body(flowEngineVo);
    }

}
