package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

import java.util.List;

/**
 * 查询门禁点事件v2
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikDoorEventsResp {
    /**
     * 数据总数
     */
    private Integer total;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 页码
     */
    private Integer pageNo;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 返回数据集合
     */
    private List<DataItem> list;

    /**
     * 返回数据对象
     */
    @Data
    public static class DataItem {

        /**
         * 事件ID，唯一标识这个事件
         */
        private String eventId;

        /**
         * 事件名称
         */
        private String eventName;

        /**
         * 事件产生时间
         */
        private String eventTime;

        /**
         * 人员唯一编码
         */
        private String personId;

        /**
         * 卡号
         */
        private String cardNo;

        /**
         * 人员姓名
         */
        private String personName;

        /**
         * 人员所属组织编码
         */
        private String orgIndexCode;

        /**
         * 人员所属组织名称
         */
        private String orgName;

        /**
         * 门禁点名称
         */
        private String doorName;

        /**
         * 门禁点编码
         */
        private String doorIndexCode;

        /**
         * 门禁点所在区域编码
         */
        private String doorRegionIndexCode;

        /**
         * 抓拍图片地址
         */
        private String picUri;

        /**
         * 图片存储服务的唯一标识
         */
        private String svrIndexCode;

        /**
         * 事件类型
         */
        private Integer eventType;

        /**
         * 进出类型
         */
        private Integer inAndOutType;

        /**
         * 读卡器唯一标识
         */
        private String readerDevIndexCode;

        /**
         * 读卡器名称
         */
        private String readerDevName;

        /**
         * 控制器设备唯一标识
         */
        private String devIndexCode;

        /**
         * 控制器设备名称
         */
        private String devName;

        /**
         * 身份证图片地址
         */
        private String identityCardUri;

        /**
         * 事件入库时间
         */
        private String receiveTime;

        /**
         * 工号
         */
        private String jobNo;

        /**
         * 学号
         */
        private String studentId;

        /**
         * 证件号码
         */
        private String certNo;

        /**
         * （图片）非第三方返回，因业务处理设置的字段
         */
        private String picture;
    }
}
