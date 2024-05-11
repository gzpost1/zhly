package cn.cuiot.dmp.base.infrastructure.dto.req;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 用户查询参数
 * @author: wuyongchong
 * @date: 2024/5/10 10:08
 */
@Data
public class BaseUserReqDto implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;

    /**
     * 部门ID列表
     */
    private List<Long> deptIdList;

}
