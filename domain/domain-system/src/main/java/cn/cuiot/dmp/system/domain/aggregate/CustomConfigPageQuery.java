package cn.cuiot.dmp.system.domain.aggregate;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomConfigPageQuery extends PageQuery {

    private static final long serialVersionUID = -5278668239460215640L;

    /**
     * 自定义配置名称
     */
    private String name;

    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空")
    private Long companyId;

    /**
     * 档案类型
     */
    private Byte archiveType;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

}