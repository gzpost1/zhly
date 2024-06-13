package cn.cuiot.dmp.system.domain.aggregate;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private Long companyId;

    /**
     * 系统选项类型
     */
    private Byte systemOptionType;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

}
