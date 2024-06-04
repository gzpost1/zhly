package cn.cuiot.dmp.system.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Data
public class FormConfig implements Serializable {

    private static final long serialVersionUID = -340520698436360336L;

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
     * 分类名称（合并后的层级名称，e.g.巡检>设备巡检）
     */
    private String typeName;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 初始化标志位(0:非初始化数据,1:初始化数据)
     */
    private Byte initFlag;

    /**
     * 表单配置详情
     */
    private String formConfigDetail;

    /**
     * 创建人
     */
    private String createdName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

}
