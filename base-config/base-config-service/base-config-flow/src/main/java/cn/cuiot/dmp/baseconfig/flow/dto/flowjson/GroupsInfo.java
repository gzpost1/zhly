package cn.cuiot.dmp.baseconfig.flow.dto.flowjson;

import lombok.Data;

import java.util.List;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 18:57
 */
@Data
public class GroupsInfo {
    private String groupType;
    private List<ConditionInfo> conditions;
    private List<String> cids;
}
