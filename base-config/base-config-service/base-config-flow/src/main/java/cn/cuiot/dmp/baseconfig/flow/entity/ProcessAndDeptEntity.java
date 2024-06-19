package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pengjian
 * @since 2024-05-22
 */
@Data
@TableName("tb_process_dept_rel")
public class ProcessAndDeptEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;


    /**
     *流程定义id
     */
    private String processDefinitionId;


    /**
     * 组织id
     */
    private Long orgId;



}
