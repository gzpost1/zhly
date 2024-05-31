package cn.cuiot.dmp.content.dal.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/28 10:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_content_img_text")
public class ContentImgTextEntity extends YjBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 组织
     */
    private String departments;

    /**
     * 楼盘
     */
    private String buildings;

    /**
     * 图文标题
     */
    private String title;

    /**
     * 图文类型
     */
    private Byte type;

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
     * 发布状态
     */
    private Byte publishStatus;

    /**
     * 企业ID
     */
    private Long companyId;
}
