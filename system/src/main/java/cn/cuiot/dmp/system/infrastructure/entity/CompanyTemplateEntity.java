package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 后台-初始化管理-企业模板
 *
 * @Author: zc
 * @Date: 2024-11-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_company_template")
public class CompanyTemplateEntity extends YjBaseEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业id
     */
    private Long companyId;
}
