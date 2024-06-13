package cn.cuiot.dmp.content.dal.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author hantingyao
 * @Description 模块配置-应用
 * @data 2024/6/3 16:29
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_content_module_application")
public class ContentModuleApplication extends YjBaseEntity implements Serializable {

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
     * 排序
     */
    private Byte sort;

    /**
     * 停启用
     */
    private Byte status;

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
     * 企业ID
     */
    private Long companyId;
}
