package cn.cuiot.dmp.baseconfig.workorder.dto;

import cn.cuiot.dmp.baseconfig.workorder.dto.json.UserInfo;
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
}
