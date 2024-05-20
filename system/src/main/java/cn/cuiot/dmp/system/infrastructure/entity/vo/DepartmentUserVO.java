package cn.cuiot.dmp.system.infrastructure.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Data
public class DepartmentUserVO implements Serializable {

    private static final long serialVersionUID = -1555569866813384740L;

    /**
     * 组织或用户id
     */
    private Long id;

    /**
     * 组织或用户名
     */
    private String name;

    /**
     * 组织或用户名类型：dept/user
     */
    private String type;

}
