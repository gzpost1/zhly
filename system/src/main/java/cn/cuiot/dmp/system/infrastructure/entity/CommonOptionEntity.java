package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表单配置-常用选项表
 *
 * @author caorui
 * @date 2024/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "common_option", autoResultMap = true)
public class CommonOptionEntity extends BaseEntity {

    private static final long serialVersionUID = 4289795555387517771L;

    /**
     * 常用选项名称
     */
    private String name;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 分类ID
     */
    private Long typeId;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 初始化标志位(0:非初始化数据,1:初始化数据)
     */
    private Byte initFlag;

}
