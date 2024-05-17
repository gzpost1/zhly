package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统信息表
 *
 * @author caorui
 * @date 2024/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "system_info", autoResultMap = true)
public class SystemInfoEntity extends BaseEntity {

    private static final long serialVersionUID = 1349154468727765757L;

    /**
     * 系统名称
     */
    private String name;

    /**
     * 系统logo
     */
    private String logoUrl;

    /**
     * 来源id（平台或者企业）
     */
    private Long sourceId;

    /**
     * 系统信息类型（0：平台 1：企业）
     */
    private Byte sourceType;

}
