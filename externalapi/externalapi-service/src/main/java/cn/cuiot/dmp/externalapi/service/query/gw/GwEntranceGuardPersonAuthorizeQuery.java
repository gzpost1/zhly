package cn.cuiot.dmp.externalapi.service.query.gw;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 格物门禁 人员授权
 *
 * @Author: zc
 * @Date: 2024-09-09
 */
@Data
public class GwEntranceGuardPersonAuthorizeQuery {

    /**
     * 人员id
     */
    @NotEmpty(message = "人员不能为空")
    @Size(min = 1)
    private List<Long> personIds;

    /**
     * 门禁id
     */
    @NotEmpty(message = "人员不能为空")
    @Size(min = 1)
    private List<Long> entranceGuardIds;
}
