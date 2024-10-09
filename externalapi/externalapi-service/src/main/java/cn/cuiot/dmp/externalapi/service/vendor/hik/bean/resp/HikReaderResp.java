package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

import java.util.List;

/**
 * 查询门禁读卡器列表
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikReaderResp {

    /**
     * 当前页码
     */
    private Integer pageNo;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 返回数据列表
     */
    private List<Resource> list;

    /**
     * 资源对象
     */
    @Data
    public static class Resource {

        /**
         * 资源类型，reader：门禁读卡器
         */
        private String resourceType;

        /**
         * 资源唯一标志
         */
        private String indexCode;

        /**
         * 资源名称
         */
        private String name;

        /**
         * 所属区域编号
         */
        private String regionIndexCode;

        /**
         * 所属区域目录，由唯一标识组成，最大10级
         */
        private String regionPath;

        /**
         * 设备IP
         */
        private String ip;

        /**
         * 设备端口
         */
        private String port;

        /**
         * 主动设备编号
         */
        private String deviceCode;

        /**
         * 设备驱动
         */
        private String deviceKey;

        /**
         * 设备型号
         */
        private String deviceModel;

        /**
         * 设备系列
         */
        private String deviceType;

        /**
         * 版本号
         */
        private String dataVersion;

        /**
         * 所属网域
         */
        private String netZoneId;

        /**
         * 拨码
         */
        private String deployId;

        /**
         * 通信方式，0：韦根; 1：RS232; 2：RS485
         */
        private String communicationMode;

        /**
         * 父级资源编号
         */
        private String parentIndexCode;

        /**
         * 显示顺序
         */
        private Integer sort;

        /**
         * 设备能力
         */
        private String capability;

        /**
         * 创建时间，ISO8601标准
         */
        private String createTime;

        /**
         * 更新时间，ISO8601标准
         */
        private String updateTime;

        /**
         * 描述
         */
        private String description;

        /**
         * 标签
         */
        private String comId;

        /**
         * 门禁点序号
         */
        private String doorNo;

        /**
         * 卡容量
         */
        private String acsReaderCardCapacity;

        /**
         * 指纹容量
         */
        private String acsReaderFingerCapacity;

        /**
         * 人脸容量
         */
        private String acsReaderFaceCapacity;

        /**
         * 所属区域目录名，符号"@"进行分隔
         */
        private String regionPathName;

        /**
         * 区域名称
         */
        private String regionName;
    }
}
