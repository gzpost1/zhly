package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author pengjian
 * @since 2024-05-22
 */
@Data
@TableName("tb_work_org_rel")
public class WorkOrgRelEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;


    /**
     * 工单id
     */
    private Long workId;


    /**
     * 组织id
     */
    private Long orgId;



}
