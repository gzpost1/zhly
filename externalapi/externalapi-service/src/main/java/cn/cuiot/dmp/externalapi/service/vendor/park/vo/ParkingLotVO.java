package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

/**
 * @author pengjian
 * @create 2024/9/10 9:51
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车场信息的封装类。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotVO {

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

    private String payRule;
}
