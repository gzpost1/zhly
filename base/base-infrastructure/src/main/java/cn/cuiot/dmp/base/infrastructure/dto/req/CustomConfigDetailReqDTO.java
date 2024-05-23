package cn.cuiot.dmp.base.infrastructure.dto.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/22
 */
@Data
public class CustomConfigDetailReqDTO implements Serializable {

    private static final long serialVersionUID = 2986266777824175987L;

    /**
     * 自定义配置详情ID列表
     */
    @NotEmpty(message = "自定义配置详情ID列表不能为空")
    private List<Long> customConfigDetailIdList;

}
