package cn.cuiot.dmp.baseconfig.flow.dto.work;

import cn.cuiot.dmp.baseconfig.flow.dto.AttachmentDTO;
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
    private String taskId;
    private String processInstanceId;
    private JSONObject formData;
    private List<AttachmentDTO> attachments;
    private String comments;
    private String signInfo;
    /**
     * 单个转办是存的用户信息
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
}
