package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

import java.util.List;

/**
 * 查询门禁点列表v2 resp
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikDoorResp {
    /**
     * 记录总数
     */
    private Integer total;

    /**
     * 当前页码
     */
    private Integer pageNo;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 资源数据列表
     */
    private List<DataItem> list;

    /**
     * 资源数据对象
     */
    @Data
    public static class DataItem {

        /**
         * 资源唯一编码
         */
        private String indexCode;

        /**
         * 资源类型
         */
        private String resourceType;

        /**
         * 资源名称
         */
        private String name;

        /**
         * 门禁点编号
         */
        private String doorNo;

        /**
         * 通道号
         */
        private String channelNo;

        /**
         * 父级资源编号
         */
        private String parentIndexCode;

        /**
         * 一级控制器 ID
         */
        private String controlOneId;

        /**
         * 二级控制器 ID
         */
        private String controlTwoId;

        /**
         * 读卡器 1
         */
        private String readerInId;

        /**
         * 读卡器 2
         */
        private String readerOutId;

        /**
         * 门序号
         */
        private Integer doorSerial;

        /**
         * 接入协议
         */
        private String treatyType;

        /**
         * 所属区域
         */
        private String regionIndexCode;

        /**
         * 所属区域目录，符号 @ 分割，包含本节点
         */
        private String regionPath;

        /**
         * 创建时间，遵守 ISO8601 标准
         */
        private String createTime;

        /**
         * 更新时间，遵守 ISO8601 标准
         */
        private String updateTime;

        /**
         * 描述信息
         */
        private String description;

        /**
         * 通道类型，door：门禁点
         */
        private String channelType;

        /**
         * 区域名称
         */
        private String regionName;

        /**
         * 所属区域目录名，符号 @ 分隔
         */
        private String regionPathName;

        /**
         * 安装位置
         */
        private String installLocation;

        /**
         * 增量状态：小于0则代表资源已被删除
         */
        private Integer status;
    }
}
