package cn.cuiot.dmp.content.param.query;//	模板

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/4 19:02
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ModuleApplicationPageQuery extends PageQuery {

    /**
     * 模块id
     */
    @NotNull(message = "模块id不能为空")
    private Long moduleId;

    /**
     * 名称
     */
    private String name;

    /**
     * 停启用状态
     */
    private Byte status;
}
