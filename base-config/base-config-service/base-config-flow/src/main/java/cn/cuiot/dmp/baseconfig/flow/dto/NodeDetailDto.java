package cn.cuiot.dmp.baseconfig.flow.dto;

import cn.cuiot.dmp.baseconfig.flow.entity.WorkBusinessTypeInfoEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/5/7 11:38
 */
@Data
public class NodeDetailDto {
    /**
     * 节点id
     */
    private String nodeId;

    /**
     *  0 未超时  1已超时
     */
    private Byte timeOut;

    /**
     * 节点信息
     */
    private List<WorkBusinessTypeInfoEntity> nodeInfos;

    /**
     * 节点处理人员
     */
    private String businessName;
    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 节点排序
     */
    private Integer sort;
}
