package cn.cuiot.dmp.system.domain.aggregate;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/5/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommonOptionPageQuery extends PageQuery {

    private static final long serialVersionUID = 7894046328795123215L;

    /**
     * 常用选项名称
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
     * 选项类别
     */
    private Byte category;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

}
