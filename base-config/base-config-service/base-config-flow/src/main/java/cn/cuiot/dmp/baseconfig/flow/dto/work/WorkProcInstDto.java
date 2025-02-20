package cn.cuiot.dmp.baseconfig.flow.dto.work;

import lombok.Data;
import org.jpedal.parser.shape.S;

import javax.validation.constraints.NotBlank;

/**
 * @author pengjian
 * @create 2024/4/26 14:25
 */
@Data
public class WorkProcInstDto {

    /**
     * 实例id
     */
    @NotBlank(message = "工单id不能为空")
    private String procInstId;

    /**
     * 查询节点类型
     */
    private String nodeType;
}
