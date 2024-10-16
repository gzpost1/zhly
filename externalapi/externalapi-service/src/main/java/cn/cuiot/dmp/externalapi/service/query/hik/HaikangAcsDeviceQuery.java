package cn.cuiot.dmp.externalapi.service.query.hik;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * 门禁设备查询参数
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:18
 */
@Data
public class HaikangAcsDeviceQuery extends PageQuery {

    /**
     * 企业ID-前端不用管
     */
    private Long companyId;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备编码
     */
    private String indexCode;

    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * 区域编号
     */
    private String regionIndexCode;

    /**
     * 设备状态（0离线，1在线）
     */
    private Byte status;

}
