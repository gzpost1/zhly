package cn.cuiot.dmp.base.infrastructure.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonOptionTypeReqDTO implements Serializable {

    private static final long serialVersionUID = -5111498545780365357L;

    /**
     * 常用选项类型ID列表
     */
    @NotEmpty(message = "常用选项类型ID列表不能为空")
    private List<Long> commonOptionTypeIdList;

}
