package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import cn.cuiot.dmp.base.infrastructure.dto.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jpedal.parser.shape.S;

/**
 * 岗位
 * @author: wuyongchong
 * @date: 2024/5/6 9:19
 */
@Data
@TableName(value = "tb_sys_post",autoResultMap = true)
public class SysPostEntity extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业账号ID
     */
    private Long orgId;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

}
