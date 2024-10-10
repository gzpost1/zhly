package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

import java.util.List;

/**
 * 查询权限配置 resp
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@Data
public class HikAcpsAuthConfigSearchResp {
    /**
     * 总条数
     */
    private Integer total;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 页码
     */
    private Integer pageNo;

    /**
     * 权限配置信息列表
     */
    private List<PermissionConfig> list;

    /**
     * 权限配置信息
     */
    @Data
    public static class PermissionConfig {

        /**
         * 人员数据编号
         */
        private String personDataId;

        /**
         * 人员数据类型，person 表示人员，org 表示组织
         */
        private String personDataType;

        /**
         * 资源数据编号
         */
        private String resourceIndexCode;

        /**
         * 资源类型，详见设备通道类型
         */
        private String resourceType;

        /**
         * 资源数据类型
         */
        private String resourceDataType;

        /**
         * 通道号
         */
        private Integer channelNo;

        /**
         * 资源通道唯一标识
         */
        private String channelIndexCode;

        /**
         * 计划模板编号，为空时默认为全天候计划模板
         */
        private String templateId;

        /**
         * 开始日期
         */
        private String startTime;

        /**
         * 结束日期
         */
        private String endTime;

        /**
         * 人员扩展参数
         */
        private Object personExtendData;

        /**
         * 人员特性，1：普通人，2：来宾，3：黑名单，4:超级用户
         */
        private Integer personProperty;

        /**
         * 人员验证方式
         */
        private String userVerifyMode;

        /**
         * 最大认证次数，当且仅当personProperty是来宾时有效
         */
        private Integer maxOpenDoorTime;

        /**
         * 是否是首人
         */
        private Boolean leaderPersonEnabled;

        /**
         * 是否关闭延迟开门
         */
        private Boolean closeDelayEnabled;
    }
}
