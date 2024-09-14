package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import lombok.Data;

/**
 * 剩余车位信息
 * @author pengjian
 * @create 2024/9/9 9:37
 */
@Data
public class FreeSpaceNumVO {
    /**
     * 车场名称
     */
    private Integer parkId;

    /**
     * 车场名称
     */
    private String parkName;

    /**
     * 总车位数
     */
    private Integer totalNum;

    /**
     * 空闲车位数
     */
    private Integer freeSpaceNum;
}
