package cn.cuiot.dmp.baseconfig.workorder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pengjian
 * @since 2024-04-23
 */
@Data
@TableName("tb_work_info")
public class WorkInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;


    /**
     * 业务类型
     */
    private String businessType;


    /**
     * 所属组织
     */
    private String org;


    /**
     * 工单名称
     */
    private String workName;


    /**
     * 工单来源
     */
    private String workSouce;


    /**
     * 用户id
     */
    private String userId;


    /**
     * 可执行的操作
     */
    private Byte status;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 创建的用户id
     */
    private String createUser;
}
