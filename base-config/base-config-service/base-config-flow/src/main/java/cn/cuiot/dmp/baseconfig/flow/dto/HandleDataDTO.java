package cn.cuiot.dmp.baseconfig.flow.dto;

import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import com.alibaba.fastjson.JSONObject;

import lombok.Data;

import java.util.List;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 16:27
 */
@Data
public class HandleDataDTO {
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
     * 附件
     */
    private List<AttachmentDTO> attachments;
    /**
     * 意见
     */
    private String comments;
    /**
     * 签名信息
     */
    private String signInfo;
    /**
     * 转办用户信息
     */
    private UserInfo transferUserInfo;
    /**
     * 加签用户信息
     */
    private UserInfo multiAddUserInfo;
    /**
     * 退回节点id
     */
    private String rollbackId;
    /**
     * 当前用户信息
     */
    private UserInfo currentUserInfo;

    /**
     * 委派的人
     */
    private UserInfo delegateUserInfo;

    /**
     * 业务操作类型 0 评论 1督办
     */
    private Byte businessType;
}
