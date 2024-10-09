package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import lombok.Data;

/**
 * @author pengjian
 * @create 2024/9/10 14:42
 */
@Data
public class NodeListVO {

    /**
     * 通道编号
     */
    private int id;

    /**
     * 通道IP地址
     */
    private String nodeIp;

    /**
     * 通道名称
     */
    private String nodeName;

    /**
     * 使用类别
     */
    private Integer useType;

    /**
     * 区域编号
     */
    private Integer areaCode;

    /**
     * 离开的区域ID
     */
    private Integer preAreaId;

    /**
     * 进入的节点ID
     */
    private Integer  nextAreaId;
}
