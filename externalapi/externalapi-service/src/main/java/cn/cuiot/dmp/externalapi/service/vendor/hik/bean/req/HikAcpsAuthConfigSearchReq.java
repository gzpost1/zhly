package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 查询权限配置 req
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HikAcpsAuthConfigSearchReq {

    /**
     * 人员数据编号
     */
    private List<String> personDataIds;

    /**
     * 人员数据类型，person 表示人员，org 表示组织
     */
    private String personDataType;

    /**
     * 设备通道对象列表
     */
    private List<ResourceInfo> resourceInfos;

    /**
     * 查询资源类型，resource 表示设备通道，resourceGroup 表示设备通道分组
     */
    private String resourceDataType;

    /**
     * 页码，pageNo > 0
     */
    private int pageNo;

    /**
     * 页大小，默认值为 20
     */
    private int pageSize;

    /**
     * 设备通道对象
     */
    @Data
    public static class ResourceInfo {

        /**
         * 资源的唯一标识
         */
        private String resourceIndexCode;

        /**
         * 资源类型，详见设备通道类型
         */
        private String resourceType;

        /**
         * 通道号
         */
        private List<Integer> channelNos;
    }
}
