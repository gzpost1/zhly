package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/23
 */
@Data
public class CustomConfigRspDTO implements Serializable {

    private static final long serialVersionUID = 7970321900337319699L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 自定义配置名称
     */
    private String name;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 系统选项类型
     */
    private Byte systemOptionType;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 自定义配置详情列表
     */
    private List<CustomConfigDetailRspDTO> customConfigDetailList;

}
