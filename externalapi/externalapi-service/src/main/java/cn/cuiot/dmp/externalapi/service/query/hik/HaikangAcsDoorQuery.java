package cn.cuiot.dmp.externalapi.service.query.hik;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * 门禁点查询参数
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:18
 */
@Data
public class HaikangAcsDoorQuery extends PageQuery {

    /**
     * 企业ID-前端不用管
     */
    private Long companyId;

    /**
     * 门禁点名称
     */
    private String name;

    /**
     * 门禁点编码
     */
    private String indexCode;

    /**
     * 门禁点编号
     */
    private String doorNo;

    /**
     * 区域编号
     */
    private String regionIndexCode;

    /**
     * 所属设备编码
     */
    private String parentIndexCode;


}
