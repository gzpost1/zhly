package cn.cuiot.dmp.baseconfig.flow.dto.vo;

import cn.cuiot.dmp.baseconfig.flow.dto.NodeDetailDto;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.ChildNode;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfig;
import cn.cuiot.dmp.baseconfig.flow.entity.WorkBusinessTypeInfoEntity;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author pengjian
 * @create 2022-10-15 16:27
 */
@Data
public class HandleDataVO {
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 表单数据
     */
    private JSONObject formData;
    /**
     * 前端是否打开 签名板
     */
    private Boolean signFlag;
    /**
     * 流程模板
     */
    private TbFlowConfig processTemplates;
    /**
     * 当前节点json数据 如果有taskId的话才返回
     */
    private ChildNode currentNode;
    /**
     * 任务详情
     */
    private Map<String,List<TaskDetailVO>> detailVOList;

    /**
     * 节点详情
     */
    private Map<String,NodeDetailDto> nodeList;
    /**
     * 已经结束的节点
     */
    List<String> endList;
    /**
     * 正在运行的节点
     */
    List<String> runningList;
    /**
     * 还没运行的节点
     */
    List<String> noTakeList;
}
