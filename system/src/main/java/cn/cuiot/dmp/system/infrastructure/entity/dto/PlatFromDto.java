package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author pengjian
 * @create 2024/7/18 20:48
 */
@Data
public class PlatFromDto {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 平台id
     */
    private String platformId;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 对接系统参数json数据
     */
    private String data;
}
