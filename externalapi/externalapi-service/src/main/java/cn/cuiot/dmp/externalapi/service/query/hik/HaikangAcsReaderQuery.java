package cn.cuiot.dmp.externalapi.service.query.hik;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * 门禁读卡器查询参数
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:18
 */
@Data
public class HaikangAcsReaderQuery extends PageQuery {

    /**
     * 企业ID-前端不用管
     */
    private Long companyId;

    /**
     * 读卡器名称
     */
    private String name;

    /**
     * 读卡器编码
     */
    private String indexCode;

    /**
     * 区域编号
     */
    private String regionIndexCode;

    /**
     * 所属设备编码
     */
    private String parentIndexCode;

    /**
     * 所属门禁点编码
     */
    private String channelIndexCode;

    /**
     * 设备型号
     */
    private String deviceModel;

}
