package cn.cuiot.dmp.system.domain.aggregate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/17
 */
@Data
public class CustomConfig implements Serializable {

    private static final long serialVersionUID = -554221353582144093L;

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
     * 初始化标志位(0:非初始化数据,1:初始化数据)
     */
    private Byte initFlag;

    /**
     * 自定义配置详情列表
     */
    private List<CustomConfigDetail> customConfigDetailList;

}
