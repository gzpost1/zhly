package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 收费管理-催款计划 更新dto
 *
 * @Author: zc
 * @Date: 2024-06-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeCollectionPlanUpdateDto extends ChargeCollectionPlanCreateDto {

    @NotNull(message = "id不能为空")
    private Long id;
}