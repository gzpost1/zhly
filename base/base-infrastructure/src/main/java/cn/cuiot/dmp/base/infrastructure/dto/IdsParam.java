package cn.cuiot.dmp.base.infrastructure.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author liujianyu
 * @description ID列表参数
 * @since 2024-05-16 10:24
 */
@Data
public class IdsParam implements Serializable {
    @NotEmpty(message = "ID列表不能为空")
    private List<Long> ids;
}
