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

    /**
     * 流程定义id
     */
    private String processDefinitionId;

    private JSONObject formData;
    /**
     * 用户信息
     */
    private Map<String, List<UserInfo>> processUsers;

    private UserInfo startUserInfo;

    /**
     * 工单来源0 计划生成  1 自查报事  2客户提单(必传) 3 代录客单(必传)
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

    /**
     * 保单客户id
     */
    private Long actualUserId;


    /**
     * 楼盘id
     */
    private Long propertyId;
}
