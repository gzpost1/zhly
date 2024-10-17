package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

import java.util.List;

/**
 * 查询区域列表v2 resp
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikRegionResp {

    /**
     * 查询数据记录总数
     */
    private Integer total;

    /**
     * 当前页码，范围 (0, ~)，最大为整数最大值
     * 若超过实际最大页码值，返回最后一页记录
     */
    private Integer pageNo;

    /**
     * 每页记录总数，范围 (0, 1000]，0 < pageSize ≤ 1000
     */
    private Integer pageSize;

    /**
     * 区域对象列表
     */
    private List<DataItem> list;

    /**
     * 区域信息对象
     */
    @Data
    public static class DataItem {

        /**
         * 区域编号
         */
        private String indexCode;

        /**
         * 区域名称
         */
        private String name;

        /**
         * 区域完整目录，含本节点，以 / 分割，上级节点在前
         */
        private String regionPath;

        /**
         * 区域完整目录，含本节点，以 / 分割，上级节点在前
         */
        private String regionPathName;

        /**
         * 父区域唯一标识码
         */
        private String parentIndexCode;

        /**
         * 标识区域节点是否有权限操作，true：有权限，false：无权限
         */
        private Boolean available;

        /**
         * true：是叶子节点，表示该区域下面未挂区域，false：不是叶子节点
         */
        private Boolean leaf;

        /**
         * 级联平台标识，多个级联编号以 @ 分隔，本级区域默认值为“0”
         */
        private String cascadeCode;

        /**
         * 区域标识，0：本级，1：级联，2：混合
         */
        private Integer cascadeType;

        /**
         * 区域类型，0：国标区域，1：雪亮工程区域，2：司法行政区域，9：自定义区域，10：历史兼容普通区域，11：历史兼容级联区域，12：楼栋单元
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
         * 本区域资源数量，统计本级挂的资源数量，不包含下级
         */
        private Integer localQuantity;

        /**
         * 本区域及下级区域资源数量，包含本级及下级
         */
        private Integer totalQuantity;

        /**
         * 创建时间，遵守ISO8601标准（2019-06-11T16:49:11.114+08:0）
         */
        private String createTime;

        /**
         * 更新时间，遵守ISO8601标准（2019-06-11T16:49:11.114+08:0）
         */
        private String updateTime;
    }
}
