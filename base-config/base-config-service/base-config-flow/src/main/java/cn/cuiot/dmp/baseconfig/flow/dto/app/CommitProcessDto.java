package cn.cuiot.dmp.baseconfig.flow.dto.app;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author pengjian
 * @create 2024/6/5 15:13
 */
@Data
public class CommitProcessDto {

    /**
     * 主键id
     */

    private Long id;


    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空")
    private Long procInstId;


    /**
     * 用户id
     */
    private Long userId;


    /**
     * 节点id
     */
    @NotBlank(message = "节点id不能为空")
    private String nodeId;


    /**
     * 表单/对象id
     */
    @NotNull(message = "对象id不能为空")
    private Long dataId;


    /**
     * 提交的数据
     */
    @NotBlank(message = "数据不能为空")
    private String commitProcess;
}
