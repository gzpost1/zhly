package cn.cuiot.dmp.base.infrastructure.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description 分页基础对象
 * @Date 2024/5/13 9:47
 * @Created by libo
 */
@Data
public class PageInfoBaseDto {
    /**
     * id
     */
    private Long id;

    /**
     * 业务分类
     */
    private Long businessTypeId;

    /**
     * 业务分类名称
     */
    private String businessTypeName;

    /**
     * 所属组织
     */
    private List<Long> orgIds;

    /**
     * 所属组织名称
     */
    private String orgName;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 企业ID
     */
    private Long companyId;
}
