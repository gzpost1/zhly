package cn.cuiot.dmp.system.domain.query;

import cn.cuiot.dmp.common.enums.OrgTypeEnum;
import cn.cuiot.dmp.domain.types.id.OrganizationId;
import cn.cuiot.dmp.system.domain.types.enums.OrderByTypeEnum;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

/**
 * @author pengcg
 * @description 条件查询入参
 * @date 2023/9/4
 */
@Data
@Builder
public class OrganizationCommonQuery {

    @Singular("id")
    private List<OrganizationId> idList;

    /**
     * 开始时间
     */
    private LocalDateTime deleteStartTime;

    /**
     * 结束时间
     */
    private LocalDateTime deleteEndTime;

    /**
     * 账户名称
     */
    private String orgName;

    /**
     *  父级账户id
     **/
    private Long parentId;

    /**
     * 统一社会信用代码
     */
    private String socialCreditCode;

    /**
     * 账户key
     */
    private String orgKey;

    /**
     * 排序方式 0：asc,  1：desc
     */
    private OrderByTypeEnum orderByType;

    /**
     * 排序参数
     */
    private Long orderByParam;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 账户(租户)类型
     */
    private OrgTypeEnum orgTypeId;

    /**
     * 账户状态（0：禁用、1：正常）
     */
    private Integer status;


    /**
     * 创建结束时间
     */
    private LocalDateTime createdEndTime;


}
