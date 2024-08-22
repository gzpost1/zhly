package cn.cuiot.dmp.externalapi.service.entity.video;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 监控AI算法
 *
 * @Author: zc
 * @Date: 2024-08-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_video_ai_method")
public class VideoAIMethodEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * AI算法ID
     */
    private String methodId;

    /**
     * AI算法名称
     */
    private String methodName;

    /**
     * 算法类型（1：统计类 2：告警类 ）
     */
    private Integer type;

    /**
     * 启停用状态（0：停用；1：启用），默认启用，第三方不推送设备信息时状态设置为停用
     */
    private Byte status;
}
