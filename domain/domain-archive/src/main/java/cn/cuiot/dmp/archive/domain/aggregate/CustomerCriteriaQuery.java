package cn.cuiot.dmp.archive.domain.aggregate;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户条件查询
 *
 * @author: wuyongchong
 * @date: 2024/6/12 11:43
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCriteriaQuery extends PageQuery {

    /**
     * 客户ID
     */
    private Long id;

    /**
     * 排除客户ID
     */
    private Long excludeId;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 关键字-手机号
     */
    private String keywordPhone;

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
     * 企业ID
     */
    private Long companyId;
}
