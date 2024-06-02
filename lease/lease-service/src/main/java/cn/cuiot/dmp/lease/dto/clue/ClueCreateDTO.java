package cn.cuiot.dmp.lease.dto.clue;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/6/2
 */
@Data
public class ClueCreateDTO implements Serializable {

    private static final long serialVersionUID = 8722362790294074321L;

    /**
     * 线索名称
     */
    @NotBlank(message = "线索名称不能为空")
    private String name;

    /**
     * 部门ID
     */
    @NotNull(message = "部门ID不能为空")
    private Long departmentId;

    /**
     * 楼盘ID
     */
    @NotNull(message = "楼盘ID不能为空")
    private Long buildingId;

    /**
     * 线索来源（系统配置自定义）
     */
    @NotNull(message = "线索来源不能为空")
    private Long sourceId;

    /**
     * 关联客户ID
     */
    private Long customerUserId;

    /**
     * 线索表单配置详情
     */
    private String formConfigDetail;

}
