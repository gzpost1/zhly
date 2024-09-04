package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/9/3 15:08
 */
@Data
public class ParkingPage {
    /**
     * 页数：必须大于等于1
     */
    private int pageIndex;
    /**
     * 每页显示的记录数
     */
    private int pageSize;
    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 车场列表
     */
    private List<ParkingLot> parkingList;

    /**
     * 车场信息的封装类。
     */
    @Data
    public static class ParkingLot {
        /**
         * 车场id
         */
        private int parkId;
        /**
         * 车场名称
         */
        private String parkName;
        /**
         * 总车位数
         */
        private int totalSpaceNum;
        /**
         * 限长：0表示不限制
         */
        private int limitL;
        /**
         * 限宽：0表示不限制
         */
        private int limitW;
        /**
         * 限高：0表示不限制
         */
        private int limitH;
        /**
         * 车场所在经度
         */
        private String lon;
        /**
         * 车场所在纬度
         */
        private String lat;
        /**
         * 地址
         */
        private String addr;
        /**
         * 电话
         */
        private String tel;
        /**
         * 停车场图片
         */
        private String imgUrl;
        /**
         * 营业时间
         */
        private String workTime;


    }
}
