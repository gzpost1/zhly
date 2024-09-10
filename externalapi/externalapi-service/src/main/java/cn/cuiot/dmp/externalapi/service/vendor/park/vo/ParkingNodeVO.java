package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import lombok.Data;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/9/9 14:31
 */
@Data
public class ParkingNodeVO {
    /**
     * 下标
     */
    private Integer pageIndex;

    /**
     * 页数
     */
    private Integer pageSize;

    /**
     * 总记录数
     */
    private Integer totalCount;

    private List<NodeList> nodeList;

    @Data
    public  static class  NodeList{
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
}
