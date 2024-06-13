package cn.cuiot.dmp.baseconfig.flow.dto.work;

import lombok.Data;
import cn.cuiot.dmp.baseconfig.flow.dto.AttachmentDTO;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/6/13 11:16
 */
@Data
public class HandleDataDTO {
    private String taskId;
    private String processInstanceId;
    private JSONObject formData;
    private List<AttachmentDTO> attachments;
    private String comments;
    private String signInfo;
    /**
     * 保存的是用户信息
     */

    private UserInfo transferUserInfo;
    private UserInfo multiAddUserInfo;
    private String rollbackId;
    private UserInfo currentUserInfo;
    private UserInfo delegateUserInfo;




    private Byte BusinessType;

    /**
     * 转办的任务信息
     */
    private List<Long> userIds;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 补充说明
     */
    private String reason;
}
