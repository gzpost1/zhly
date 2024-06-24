package cn.cuiot.dmp.lease.dto.price;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PriceManageUpdateDTO extends PriceManageCreateDTO {

    private static final long serialVersionUID = -5030181292143444647L;

    /**
     * 定价单id
     */
    @NotNull(message = "定价单id不能为空")
    private Long id;

}
