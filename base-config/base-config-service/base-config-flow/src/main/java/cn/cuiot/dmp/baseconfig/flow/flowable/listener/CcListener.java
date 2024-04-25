package cn.cuiot.dmp.baseconfig.flow.flowable.listener;

import cn.cuiot.dmp.base.infrastructure.utils.SpringContextHolder;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.ChildNode;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowCc;
import cn.cuiot.dmp.baseconfig.flow.service.TbFlowCcService;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void execute(DelegateExecution execution) {
        TbFlowCcService ccService = SpringContextHolder.getBean(TbFlowCcService.class);
        String currentActivityId = execution.getCurrentActivityId();
        List<String> selectUsers = (List<String>) execution.getVariable(currentActivityId);
        List<TbFlowCc> ccs= new ArrayList<>();
        if(CollUtil.isNotEmpty(selectUsers)){

            for (String selectUser : selectUsers) {
                TbFlowCc cc = new TbFlowCc();
                cc.setId(IdWorker.getId());
                cc.setUserId(Long.valueOf(selectUser));
                cc.setProcessInstanceId(execution.getProcessInstanceId());
                ccs.add(cc);
            }
        }

        ChildNode childNode = getNode(execution);
        ChildNode node = getChildNode(childNode, execution.getCurrentActivityId());
        List<UserInfo> value = node.getProps().getAssignedUser();

        if(value!=null){
            for (UserInfo userInfo : value) {
                if(userInfo.getType().equals("user")){
                    TbFlowCc cc = new TbFlowCc();
                    cc.setId(IdWorker.getId());
                    cc.setUserId(Long.valueOf(userInfo.getId()));
                    cc.setProcessInstanceId(execution.getProcessInstanceId());
                    ccs.add(cc);
                }
            }
        }

        if(CollUtil.isNotEmpty(ccs)){
            ccService.saveBatch(ccs);
        }

    }


    private ChildNode getNode(DelegateExecution execution) {

        Process mainProcess = repositoryService.getBpmnModel(execution.getProcessDefinitionId()).getMainProcess();
        String dingDing = mainProcess.getAttributeValue(FLOWABLE_NAME_SPACE, FLOWABLE_NAME_SPACE_NAME);
        JSONObject jsonObject = JSONObject.parseObject(dingDing, new TypeReference<JSONObject>() {
        });
        String processJson = jsonObject.getString(VIEW_PROCESS_JSON_NAME);
        ChildNode childNode = JSONObject.parseObject(processJson, new TypeReference<ChildNode>(){});
        return childNode;
    }
}
