package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 目标数据查询参数
 *
 * @author: wuyongchong
 * @date: 2024/5/15 10:11
 */
@Data
public class TargetQuery implements Serializable {

    /**
     * 类型 0部门 1人员 2角色
     */
    @NotNull(message = "类型参数不能为空")
    private Integer type;

    /**
     * ID列表
     */
    @NotEmpty(message = "ID列表不能为空")
    private List<Long> ccIds;

}
