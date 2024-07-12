package cn.cuiot.dmp.baseconfig.flow.dto.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/6/12 14:16
 */
@Data
public class RepairReportDetailDto {

    /**
     * 工单id
     */
    private Long processInstanceId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 流程名称
     */
    private String workName;

    /**
     * 处理进度 1已完成 2待处理 5已撤回
     */
    private Byte status;

    /**
     *提交的时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 用户提交的表单数据
     */
    private String commitProcess;

    /**
     * 表单数据 节点数据
     */
    private String process;

    /**
     * 提示内容
     */
    private String content;


    /**
     * 是否可撤回  1 可撤回
     */
    private Byte revokeType;


    /**
     * 1 重新提交
     */
    private Byte resubmit;

    /**
     * 评价 1展示
     */
    private Byte evaluate;

    /**
     * 当前所在节点 为空则表示流程已结束
     */
    private String currentNode;

    /**
     * 流程定义id
     */
    private  String processDefinitionId;

    /**
     * 组织ids
     */
    private List<Long> orgIds;


}
