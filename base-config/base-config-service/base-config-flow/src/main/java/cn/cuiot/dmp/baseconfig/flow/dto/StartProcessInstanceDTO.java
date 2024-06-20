package cn.cuiot.dmp.baseconfig.flow.dto;

import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import cn.cuiot.dmp.baseconfig.flow.entity.CommitProcessEntity;
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
    /**
     * 提交的表单数据
     */
    private JSONObject formData;
    /**
     * 用户信息
     */
    private Map<String, List<UserInfo>> processUsers;

    /**
     * 启动的用户信息
     */
    private UserInfo startUserInfo;

    /**
     * 工单来源0 计划生成  1 自查报事  2客户提单 3 代录客单
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
     * 报单用户id
     */
    private Long actualUserId;

    /**
     * 报单客户id
     */
    private Long customerId;


    /**
     * 楼盘id
     */
    private Long propertyId;

    /**
     * 新增的提交的表单信息
     */
    private CommitProcessEntity commitProcess;
}
