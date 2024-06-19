package cn.cuiot.dmp.baseconfig.flow.dto.app.query;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author pengjian
 * @create 2024/6/4 15:03
 */
@Data
public class NodeTypeQuery {

    /**
     * 0 未处理 1已处理
     */
    @NotNull(message = "查询类型不能为空")
    private Byte queryType;
}
