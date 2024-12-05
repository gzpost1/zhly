package cn.cuiot.dmp.archive.application.param.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * 客户查询参数
 *
 * @author: wuyongchong
 * @date: 2024/6/12 11:18
 */
@Data
public class CustomerQuery extends PageQuery {

    /**
     * 客户ID
     */
    private Long id;

    /**
     * 排除客户ID
     */
    private Long excludeId;

    /**
     * 包含客户ID
     */
    private Long includeId;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系人手机号
     */
    private String contactPhone;

    /**
     * 状态
     */
    private Byte status;


    /**
     * 房屋ID
     */
    private Long houseId;

    /**
     * 楼盘ID
     */
    private Long loupanId;


    /**
     * 企业ID-前端不用管
     */
    private Long companyId;

    /**
     * 客户类型
     */
    private String customerType;
    /**
     * 公司性质
     */
    private String companyNature;
    /**
     * 所属行业
     */
    private String companyIndustry;
}
