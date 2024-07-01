package cn.cuiot.dmp.base.infrastructure.dto.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/28
 */
@Data
public class UserHouseAuditBuildingReqDTO implements Serializable {

    private static final long serialVersionUID = 8017133658657973507L;

    /**
     * 楼盘编码列表
     */
    @NotEmpty(message = "楼盘编码列表不能为空")
    private List<Long> buildingIds;

}
