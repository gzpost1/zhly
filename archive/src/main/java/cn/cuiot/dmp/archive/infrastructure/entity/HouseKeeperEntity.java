package cn.cuiot.dmp.archive.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 管家管理
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_house_keeper",autoResultMap = true)
public class HouseKeeperEntity extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

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
     * 所属楼盘ID
     */
    private Long communityId;

    /**
     * 所属员工ID
     */
    private Long staffId;


    /**
     * 管家昵称
     */
    private String nickName;


    /**
     * 联系方式
     */
    private String contactWay;


    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;


    /**
     * 备注
     */
    private String remark;

    /**
     * 楼盘名称
     */
    @TableField(exist = false)
    private String communityName;

    /**
     * 员工名称
     */
    @TableField(exist = false)
    private String staffName;


}
