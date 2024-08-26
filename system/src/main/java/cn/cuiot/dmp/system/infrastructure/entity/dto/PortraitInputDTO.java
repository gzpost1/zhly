package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人像录入信息 查询VO
 *
 * @date 2024/8/22 14:50
 * @author gxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PortraitInputDTO extends PageQuery {
    private static final long serialVersionUID = -8770892455054780107L;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 名字
     */
    private String name;

    /**
     * 创建人名称
     */
    private String createUserName;

}