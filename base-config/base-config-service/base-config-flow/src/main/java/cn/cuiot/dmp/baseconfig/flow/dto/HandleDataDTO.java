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
    private String taskId;
    private String processInstanceId;
    private JSONObject formData;
    private List<AttachmentDTO> attachments;
    private String comments;
    private String signInfo;
    private UserInfo transferUserInfo;
    private UserInfo multiAddUserInfo;
    private String rollbackId;
    private UserInfo currentUserInfo;
    private UserInfo delegateUserInfo;

    private Byte BusinessType;
}
