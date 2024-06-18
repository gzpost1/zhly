package cn.cuiot.dmp.baseconfig.flow.flowable.listener;

import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.CCInfo;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.ChildNode;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowCc;
import cn.cuiot.dmp.baseconfig.flow.enums.FlowCCEnums;
import cn.cuiot.dmp.baseconfig.flow.feign.SystemToFlowService;
import cn.cuiot.dmp.baseconfig.flow.flowable.msg.MsgSendService;
import cn.cuiot.dmp.baseconfig.flow.service.TbFlowCcService;
import cn.cuiot.dmp.baseconfig.flow.service.TbFlowConfigOrgService;
import cn.cuiot.dmp.common.utils.AssertUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants.*;
import static cn.cuiot.dmp.baseconfig.flow.utils.BpmnModelUtils.getChildNode;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 19:47
 */
@Component("ccListener")
public class CcListener implements JavaDelegate {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private TbFlowCcService flowCcService;
    @Autowired
    private SystemToFlowService systemToFlowService;
    @Autowired
    private TbFlowConfigOrgService flowConfigOrgService;
    @Autowired
    private MsgSendService msgSendService;

    @Override
    public void execute(DelegateExecution execution) {
        ChildNode childNode = getNode(execution);
        ChildNode node = getChildNode(childNode, execution.getCurrentActivityId());
        CCInfo ccInfo = node.getProps().getCcInfo();
        String nodeId = node.getId();

        String flowConfigId = StringUtils.substringBefore(execution.getProcessDefinitionId(), ":").replace(PROCESS_PREFIX, "");


        if (Objects.nonNull(ccInfo)) {
            List<Long> ccUserIds = new ArrayList<>();
            if (Objects.equals(ccInfo.getType(), FlowCCEnums.PERSON.getCode())) {
                ccUserIds = ccInfo.getCcIds().stream().map(e -> Long.parseLong(e)).collect(Collectors.toList());
            } else if (Objects.equals(ccInfo.getType(), FlowCCEnums.ROLE.getCode())) {
                List<Long> flowCOnfigDeptIds = flowConfigOrgService.queryOrgIdsByFlowConfigId(Long.valueOf(flowConfigId));
                AssertUtil.notEmpty(flowCOnfigDeptIds, "该流程暂未配置组织,请重试");

                ccUserIds = systemToFlowService.getUserIdByRole(ccInfo.getCcIds().stream().map(e -> Long.parseLong(e)).collect(Collectors.toList()), flowCOnfigDeptIds);
            } else if (Objects.equals(ccInfo.getType(), FlowCCEnums.DEPARTMENT.getCode())) {
                ccUserIds = systemToFlowService.getUserIdByDept(ccInfo.getCcIds().stream().map(e -> Long.parseLong(e)).collect(Collectors.toList()));
            }

            if (CollectionUtils.isNotEmpty(ccUserIds)) {
                List<TbFlowCc> collect = ccUserIds.stream().map(e -> {
                    TbFlowCc cc = new TbFlowCc();
                    cc.setId(IdWorker.getId());
                    cc.setUserId(e);
                    cc.setProcessInstanceId(execution.getProcessInstanceId());
                    cc.setNodeId(nodeId);
                    return cc;
                }).collect(Collectors.toList());

                flowCcService.saveBatch(collect);

                msgSendService.sendCCMsg(execution.getProcessInstanceId(), ccUserIds,execution.getProcessDefinitionId());
            }
        }
    }


    private ChildNode getNode(DelegateExecution execution) {

        Process mainProcess = repositoryService.getBpmnModel(execution.getProcessDefinitionId()).getMainProcess();
        String dingDing = mainProcess.getAttributeValue(FLOWABLE_NAME_SPACE, FLOWABLE_NAME_SPACE_NAME);
        JSONObject jsonObject = JSONObject.parseObject(dingDing, new TypeReference<JSONObject>() {
        });
        String processJson = jsonObject.getString(VIEW_PROCESS_JSON_NAME);
        ChildNode childNode = JSONObject.parseObject(processJson, new TypeReference<ChildNode>() {
        });
        return childNode;
    }
}
