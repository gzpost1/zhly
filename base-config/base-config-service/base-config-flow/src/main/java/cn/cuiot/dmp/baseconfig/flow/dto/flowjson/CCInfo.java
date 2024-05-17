package cn.cuiot.dmp.baseconfig.flow.dto.flowjson;

import lombok.Data;

import java.util.List;

/**
 * @Description 抄送配置
 * @Date 2024/4/28 16:46
 * @Created by libo
 */
@Data
public class CCInfo {
    /**
     * 抄送类型 0指定部门 1指定人员 2指定角色
     */
    private Byte type;
    /**
     * 抄送部门id/人员id/角色id
     */
    private List<String> ccIds;
}
