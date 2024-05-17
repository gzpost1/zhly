package cn.cuiot.dmp.base.infrastructure.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description 批量操作
 * @Date 2024/5/6 9:29
 * @Created by libo
 */
@Data
public class BatcheOperation {
    /**
     * 操作类型 1移动 2删除 3停启用
     */
    @NotNull(message = "操作类型不能为空")
    private Byte operaTionType;

    /**
     * 批量操作id
     */
    @NotEmpty(message = "id不能为空")
    private List<Long> ids;

    /**
     * 操作状态
     */
    private Long operaToStatus;
}
