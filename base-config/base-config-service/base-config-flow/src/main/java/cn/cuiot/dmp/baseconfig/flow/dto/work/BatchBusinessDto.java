package cn.cuiot.dmp.baseconfig.flow.dto.work;

import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pengjian
 * @create 2024/5/14 20:38
 */
@Data
public class BatchBusinessDto {

    /**
     * 工单中心传
     */
    private List<String> processInstanceId ;

    /**
     * 补充说明
     */
    private String comments;

    /**
     * 原因
     */
    private String reason;

    /**
     * 业务类型
     */
    private Byte businessType;

    /**
     * 审批中心传
     */
    private List<String> taskIds;

    /**
     * 节点自选人
     */
    private Map<String, List<UserInfo>> processUsers;

}
