package cn.cuiot.dmp.externalapi.service.query.gw;


import java.math.BigDecimal;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author wuyongchong
 * @since 2024-10-23
 */
@Getter
@Setter
public class GwSmogQuery extends PageQuery {

    /**
    * 设备名称
    */
    private String name;

    /**
    * 设备IMEI号，全局唯一
    */
    private String imei;


    /**
    * 状态 1-启用 0-停用
    */
    private Byte status;

    /**
    * 设备状态 (0: 在线，1: 离线，2: 未激活）
    */
    private String equipStatus;

}
