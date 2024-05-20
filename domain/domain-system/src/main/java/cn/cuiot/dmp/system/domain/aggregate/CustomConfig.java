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
     * 档案类型
     */
    private Byte archiveType;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 自定义配置详情列表
     */
    private List<CustomConfigDetail> customConfigDetailList;

}
