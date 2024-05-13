package cn.cuiot.dmp.base.application.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description 查询业务类型和组织机构
 * @Date 2024/5/9 15:40
 * @Created by libo
 */
@Data
public class BusinessAndOrgDto {
    /**
     * 某行的ID
     */
    private Long dataId;

    /**
     * 业务类型ID列表
     */
    private List<Long> businessTypeIdList;

    /**
     * 组织ID列表
     */
    private List<Long> orgIds;

    /**
     * 企业ID
     */
    private Long companyId;
}
