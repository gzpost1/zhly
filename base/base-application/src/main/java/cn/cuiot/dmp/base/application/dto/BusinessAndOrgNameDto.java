package cn.cuiot.dmp.base.application.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description 查询业务类型和组织机构
 * @Date 2024/5/9 15:40
 * @Created by libo
 */
@Data
public class BusinessAndOrgNameDto {
    private Long dataId;
    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 组织名称
     */
    private String orgName;

}
