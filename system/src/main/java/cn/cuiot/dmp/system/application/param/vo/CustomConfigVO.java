package cn.cuiot.dmp.system.application.param.vo;

import cn.cuiot.dmp.system.domain.aggregate.CustomConfigDetail;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/19
 */
@Data
public class CustomConfigVO implements Serializable {

    private static final long serialVersionUID = 989030494616412195L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 自定义配置名称
     */
    private String name;

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
    private List<CustomConfigDetail> customConfigDetailList;

}
