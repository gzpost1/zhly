package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 提交的表单数据
 * @author pengjian
 * @since 2024-06-05
 */
@Data
@TableName("tb_commit_process")
public class CommitProcessEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;


    /**
     * 订单id
     */
    private Long procInstId;


    /**
     * 用户id
     */
    private Long userId;


    /**
     * 节点id
     */
    private String nodeId;


    /**
     * 表单/对象id
     */
    private Long dataId;


    /**
     * 提交的数据
     */
    private String commitProcess;

    /**
     * 操作记录表的id
     */
    private Long businessTypeId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
