package cn.cuiot.dmp.externalapi.service.vendor.park.query;

import lombok.Builder;
import lombok.Data;

/**
 * @author pengjian
 * @create 2024/9/3 15:12
 */
@Data
@Builder(toBuilder = true)
public class ParkingLotQuery {

    /**
     * 用户id，不允许为空
     */
    private String appId;

    /**
     * 验证码，参照安全性验证
     */
    private String key;

    /**
     * 服务代码，用于标识请求的服务类型
     */
    private String serviceCode;

    /**
     * 时间戳：单位是毫秒，详见(1.3默认请求参数)
     */
    private String ts;

    /**
     * 每次请求的唯一标识
     */
    private String reqId;

    /**
     * 第N页，从1开始，N>=1
     */
    private int pageIndex;

    /**
     * 每一页数量 >=1
     */
    private int pageSize;
}
