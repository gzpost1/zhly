package cn.cuiot.dmp.system.domain.aggregate;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/5/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FormConfigPageQuery extends PageQuery {

    private static final long serialVersionUID = -8399309507223284047L;

    /**
     * 表单名称
     */
    private String name;

    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空")
    private Long companyId;

    /**
     * 分类ID
     */
    private Long typeId;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

}
