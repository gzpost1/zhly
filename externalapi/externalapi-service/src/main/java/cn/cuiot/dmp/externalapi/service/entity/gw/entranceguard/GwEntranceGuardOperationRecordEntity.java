package cn.cuiot.dmp.externalapi.service.entity.gw.entranceguard;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 格物门禁-开门记录
 *
 * @Author: zc
 * @Date: 2024-09-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_gw_entrance_guard_operation_record")
public class GwEntranceGuardOperationRecordEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 执行状态（0：失败，1：成功，2：执行中）
     */
    @TableField(value = "execution_status")
    private Byte executionStatus;

    /**
     * 门禁id
     */
    @TableField(value = "entrance_guard_id")
    private Long entranceGuardId;

    /**
     * 备注
     */
    @TableField(value = "device_secret")
    private String deviceSecret;

    /**
     * 操作类型（1：开门，2：重启）
     */
    @TableField(value = "type")
    private Byte type;

    /**
     * 失败原因
     */
    @TableField(value = "fail_msg")
    private String failMsg;

    private static final long serialVersionUID = 1L;
}