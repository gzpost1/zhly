package cn.cuiot.dmp.content.dal.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hantingyao
 * @Description 模块管理-banner模块
 * @data 2024/6/3 16:29
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_content_module_banner")
public class ContentModuleBanner extends YjBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片
     */
    private String pic;

    /**
     * 生效开始时间
     */
    private Date effectiveStartTime;

    /**
     * 生效结束时间
     */
    private Date effectiveEndTime;

    /**
     * 是否跳转
     */
    private Byte skip;

    /**
     * 跳转类型
     */
    private Byte skipType;

    /**
     * 资源id
     */
    private String sourceId;

    /**
     * 跳转资源名称
     */
    private String sourceName;

    /**
     * 资源类型
     */
    private String sourceType;

    /**
     * 停启用
     */
    private Byte status;

    /**
     * 企业ID
     */
    private Long companyId;
}
