package cn.cuiot.dmp.content.dal.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/28 10:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_content_img_text", autoResultMap = true)
public class ContentImgTextEntity extends YjBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 组织
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<String> departments;

    /**
     * 楼盘
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<String> buildings;

    /**
     * 图文标题
     */
    private String title;

    /**
     * 图文类型
     */
    private Long type;

    /**
     * 图文封面
     */
    private String coverPic;

    /**
     * 摘要
     */
    private String digest;

    /**
     * 详情
     */
    private String detail;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 审核状态
     */
    private Byte auditStatus;

    /**
     * 企业ID
     */
    private Long companyId;
}
