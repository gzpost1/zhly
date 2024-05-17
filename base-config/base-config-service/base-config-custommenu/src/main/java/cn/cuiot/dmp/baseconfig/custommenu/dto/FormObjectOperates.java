package cn.cuiot.dmp.baseconfig.custommenu.dto;

import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 16:26
 */
@Data
public class FormObjectOperates {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 表单名称
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
     * 表单配置详情
     */
    private String formConfigDetail;
}
