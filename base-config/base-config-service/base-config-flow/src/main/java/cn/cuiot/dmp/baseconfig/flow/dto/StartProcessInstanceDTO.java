package cn.cuiot.dmp.baseconfig.flow.dto;

import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author LoveMyOrange
 * @create 2022-10-14 23:27
 */
@Data
public class StartProcessInstanceDTO {

    private String processDefinitionId;

    private JSONObject formData;

    private Map<String, List<UserInfo>> processUsers;

    private UserInfo startUserInfo;

    /**
     * 工单来源
     */
    private Byte workSource;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 定时任务的创建人
     */
    private Long createUserId;
}
