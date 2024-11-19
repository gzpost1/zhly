package cn.cuiot.dmp.externalapi.service.entity.gw.entranceguard;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 格物门禁-授权
 *
 * @Author: zc
 * @Date: 2024-09-14
 */
@Data
@TableName(value = "tb_gw_entrance_guard_authorize")
public class GwEntranceGuardAuthorizeEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 门禁id
     */
    @TableField(value = "entrance_guard_id")
    private Long entranceGuardId;

    /**
     * 门禁人员id
     */
    @TableField(value = "entrance_guard_person_id")
    private Long entranceGuardPersonId;

    private static final long serialVersionUID = 1L;
}