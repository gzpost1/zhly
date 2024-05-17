package cn.cuiot.dmp.baseconfig.flow.dto;

import lombok.Data;

/**
 * @Description 系统配置和组织机构的中间表
 * @Date 2024/5/9 16:38
 * @Created by libo
 */
@Data
public class FlowConfigOrgDto {
    /**
     * 数据ID
     */
    private Long dataId;
    /**
     * 组织机构ID，多个用逗号隔开
     */
    private String orgIdStr;
}
