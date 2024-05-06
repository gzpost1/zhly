package cn.cuiot.dmp.baseconfig.custommenu.vo;

import cn.cuiot.dmp.baseconfig.custommenu.dto.FlowTaskInfoUpdateDto;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 系统配置-任务配置
 */
@Data
public class FlowTaskConfigVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 任务配置名称
     */
    private String name;

    /**
     * 业务分类
     */
    private Long businessTypeId;

    /**
     * 所属组织
     */
    private Long orgId;

    /**
     * 任务描述
     */
    private String remark;

    /**
     * 任务对象信息
     */
    private List<FlowTaskInfoVo> taskInfoList;
}