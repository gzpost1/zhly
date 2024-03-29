package cn.cuiot.dmp.system.infrastructure.entity.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author wensq
 * @version 1.0
 * @description: 启用/禁用账户 入参
 * @date 2022/9/14 18:06
 */
@Data
public class OperateOrganizationDto {

    /**
     * 账户主键id
     */
    @NotNull(message = "账户主键id不能为空")
    private Long id;

    /**
     * 账户状态(0：禁用、 1：启用）
     */
    @NotNull(message = "账户状态不能为空")
    private Integer status;

    private String userId;
}
