package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

import java.util.List;

/**
 * 海康资源列表返回resp
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikAcsListResp {

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
     * 资源数据列表，根据 resourceType 返回对应的资源对象
     * 例如门禁点对象 DoorDTO
     */
    private List<DataItem> list;

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
         * 父级资源编号
         */
        private String parentIndexCode;

        /**
         * 门禁设备类型编码
         * 详见[附录A.7 门禁设备类型]
         */
        private String devTypeCode;

        /**
         * 门禁设备类型型号
         * 详见[附录A.7 门禁设备类型]
         */
        private String devTypeDesc;

        /**
         * 主动设备编号
         */
        private String deviceCode;

        /**
         * 设备型号
         */
        private String deviceModel;

        /**
         * 设备系列
         */
        private String deviceType;

        /**
         * 厂商
         */
        private String manufacturer;

        /**
         * 所属区域
         */
        private String regionIndexCode;

        /**
         * 所属区域目录，以 @ 符号分割，包含本节点
         */
        private String regionPath;

        /**
         * 接入协议
         * 详见[附录A.6 编码设备接入协议]
         */
        private String treatyType;

        /**
         * 设备卡容量
         */
        private Integer cardCapacity;

        /**
         * 指纹容量
         */
        private Integer fingerCapacity;

        /**
         * 指静脉容量
         */
        private Integer veinCapacity;

        /**
         * 人脸容量
         */
        private Integer faceCapacity;

        /**
         * 门容量
         */
        private Integer doorCapacity;

        /**
         * 拨码
         */
        private String deployId;

        /**
         * 创建时间
         */
        private String createTime;

        /**
         * 更新时间
         */
        private String updateTime;

        /**
         * 描述
         */
        private String description;

        /**
         * 所属网域
         */
        private String netZoneId;

        /**
         * 支持认证方式，数据为十进制
         */
        private String acsReaderVerifyModeAbility;

        /**
         * 区域名称
         */
        private String regionName;

        /**
         * 所属区域目录名，以"/"分隔
         */
        private String regionPathName;

        /**
         * 门禁设备IP
         */
        private String ip;

        /**
         * 门禁设备端口
         */
        private String port;

        /**
         * 设备能力集(含设备上的智能能力)
         * 详见[附录A.44 设备能力集]
         */
        private String capability;

        /**
         * 设备序列号
         */
        private String devSerialNum;

        /**
         * 版本号
         */
        private String dataVersion;
    }
}
