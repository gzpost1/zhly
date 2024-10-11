package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;


import lombok.Data;

import java.util.List;

/**
 * 查询资源列表v2 resp
 *
 * @Author: zc
 * @Date: 2024-10-11
 */
@Data
public class HikIrdsResourcesByParamsResp {

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
    private List<ResourceData> list;

    @Data
    public static class ResourceData {

        /**
         * 唯一标识
         */
        private String indexCode;

        /**
         * 名称
         */
        private String name;

        /**
         * 资源类型
         */
        private String resourceType;

        /**
         * 门禁点编号
         */
        private Integer doorNo;

        /**
         * 描述
         */
        private String description;

        /**
         * 父级资源编号
         */
        private String parentIndexCodes;

        /**
         * 所属区域唯一标识
         */
        private String regionIndexCode;

        /**
         * 所属区域目录
         */
        private String regionPath;

        /**
         * 通道类型
         */
        private String channelType;

        /**
         * 通道号
         */
        private String channelNo;

        /**
         * 安装位置
         */
        private String installLocation;

        /**
         * 设备能力集
         */
        private String capabilitySet;

        /**
         * 一级控制器id
         */
        private String controlOneId;

        /**
         * 二级控制器id
         */
        private String controlTwoId;

        /**
         * 读卡器1
         */
        private String readerInId;

        /**
         * 读卡器2
         */
        private String readerOutId;

        /**
         * 组件标志
         */
        private String comId;

        /**
         * 创建时间
         */
        private String createTime;

        /**
         * 更新时间
         */
        private String updateTime;
    }
}
