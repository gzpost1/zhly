package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import cn.cuiot.dmp.common.enums.SystemOptionTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author caorui
 * @date 2024/5/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "custom_config", autoResultMap = true)
public class CustomConfigEntity extends BaseEntity {

    private static final long serialVersionUID = -2615769892863695934L;

    /**
     * 自定义配置名称
     */
    private String name;

    /**
     * 系统选项类型
     * @see SystemOptionTypeEnum
     */
    private Byte systemOptionType;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

}
