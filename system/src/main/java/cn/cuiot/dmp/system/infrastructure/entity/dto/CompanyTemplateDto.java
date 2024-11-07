package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 后台-初始化管理-企业模板 保存DTO
 *
 * @Author: zc
 * @Date: 2024-11-05
 */
@Data
public class CompanyTemplateDto {

    /**
     * 企业id
     */
    @NotNull(message = "模板企业不能为空")
    private Long companyId;
}
