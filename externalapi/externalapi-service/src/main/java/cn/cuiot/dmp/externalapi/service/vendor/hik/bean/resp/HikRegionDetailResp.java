package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

import java.util.List;

/**
 * 根据编号获取区域详细信息
 *
 * @Author: zc
 * @Date: 2024-10-12
 */
@Data
public class HikRegionDetailResp {

    /**
     * 结果总数
     */
    private Integer total;

    /**
     * 区域信息列表
     */
    private List<RegionInfo> list;

    @Data
    public static class RegionInfo {

        /**
         * 区域唯一标识码
         */
        private String indexCode;

        /**
         * 区域名称
         */
        private String name;

        /**
         * 所属区域目录,以@符号分割，包含本节点
         */
        private String regionPath;

        /**
         * 父区域唯一标识码
         */
        private String parentIndexCode;

        /**
         * 用于标识区域节点是否有权限操作
         */
        private Boolean available;

        /**
         * 标识区域节点是否叶子节点
         */
        private Boolean leaf;

        /**
         * 级联平台标识
         */
        private String cascadeCode;

        /**
         * 区域标识 0：本级 1：级联 2：混合
         */
        private Integer cascadeType;

        /**
         * 区域类型
         */
        private Integer catalogType;

        /**
         * 外码(如：国际码)
         */
        private String externalIndexCode;

        /**
         * 父外码(如：国际码)
         */
        private String parentExternalIndexCode;

        /**
         * 同级区域顺序
         */
        private Integer sort;

        /**
         * 创建时间，ISO8601标准
         */
        private String createTime;

        /**
         * 更新时间，ISO8601标准
         */
        private String updateTime;
    }
}
