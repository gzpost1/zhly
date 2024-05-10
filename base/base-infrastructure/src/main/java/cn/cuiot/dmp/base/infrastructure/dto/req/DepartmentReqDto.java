package cn.cuiot.dmp.base.infrastructure.dto.req;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 组织部门查询参数
 * @author: wuyongchong
 * @date: 2024/5/10 10:08
 */
@Data
public class DepartmentReqDto implements Serializable {

    /**
     * 部门ID列表
     */
    private List<Long> deptIdList;

}
