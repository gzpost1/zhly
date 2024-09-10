package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/9/9 15:31
 */
@Data
public class ParkNodeStatusVO {


    private List<NodeLists> nodeList;

    @Data
    public static class NodeLists{
        /**
         * 通道id
         */
        private Integer nodeId;

        /**
         * 道闸状态：1是常抬，0是正常；-1：状态未知
         */
        private Integer status;
    }

}
