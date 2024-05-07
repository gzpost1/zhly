package cn.cuiot.dmp.baseconfig.flow.dto.work;

import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfig;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/4/30 16:03
 */
@Data
public class WorkFlowDetailDto {
    /**
     * 节点id
     */
    private String  id;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 流程模板
     */
    private TbFlowConfig tbFlowConfig;

    /**
     * 是否超时 0未超时 1已超时
     */
    private Byte timeOut;

    /**
     * 处理人
     */
    private String assignedUser;

    /**
     * 子节点
     */
    private WorkFlowDetailDto children;

    /**
     * 节点详情
     */
    private List<WorkBusinessRecord> commentVOList;
}
