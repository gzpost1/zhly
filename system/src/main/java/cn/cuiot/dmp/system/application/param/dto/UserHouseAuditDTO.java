package cn.cuiot.dmp.system.application.param.dto;

import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import cn.cuiot.dmp.system.infrastructure.entity.UserHouseAuditEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户房屋信息
 * @author caorui
 * @date 2024/6/13
 */
@Data
public class UserHouseAuditDTO extends UserHouseAuditEntity {

    /**
     * 证件类型名称
     */
    private String cardTypeIdName;
    /**
     * 楼盘名称
     */
    private String buildingName;
    /**
     * 组织部门ID
     */
    private Long departmentId;
    /**
     *  组织部门名称
     */
    private String departmentName;
}
