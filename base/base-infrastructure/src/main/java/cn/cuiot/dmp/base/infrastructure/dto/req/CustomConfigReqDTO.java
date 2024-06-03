package cn.cuiot.dmp.base.infrastructure.dto.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/23
 */
@Data
public class CustomConfigReqDTO implements Serializable {

    private static final long serialVersionUID = 2986266777824175987L;

    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空")
    private Long companyId;

    /**
     * 系统选项类型
     */
    private Byte systemOptionType;

}
